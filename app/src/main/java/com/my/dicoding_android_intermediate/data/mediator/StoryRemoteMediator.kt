package com.my.dicoding_android_intermediate.data.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.my.dicoding_android_intermediate.data.database.StoryDatabase
import com.my.dicoding_android_intermediate.data.remote.model.StoryLocalModel
import com.my.dicoding_android_intermediate.data.remote.model.StoryRemoteKey
import com.my.dicoding_android_intermediate.data.remote.network.ApiService

@ExperimentalPagingApi
class StoryRemoteMediator(
    private val apiService: ApiService,
    private val storyDatabase: StoryDatabase,
    private val token: String,
) : RemoteMediator<Int, StoryLocalModel>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, StoryLocalModel>
    ): MediatorResult {

        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: INITIAL_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        try {
            val responseData = apiService.getAllStories(token, page, state.config.pageSize)
            val endOfPaginationReached = responseData.storyResponseItems.isEmpty()
            val storyLocalModelList: MutableList<StoryLocalModel> = mutableListOf()

            storyDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    storyDatabase.storyRemoteKeysDao().deleteRemoteKeys()
                    storyDatabase.storyDao().deleteAll()
                }

                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1

                val keys = responseData.storyResponseItems.map {
                    StoryRemoteKey(id = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                storyDatabase.storyRemoteKeysDao().insertAll(keys)

                responseData.storyResponseItems.forEach { result ->
                    val storyLocalModel = StoryLocalModel.fromResponse(result)
                    storyLocalModelList.add(storyLocalModel)
                }
                storyDatabase.storyDao().insertStories(storyLocalModelList)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: Exception) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, StoryLocalModel>): StoryRemoteKey? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            storyDatabase.storyRemoteKeysDao().getRemoteKeysId(data.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, StoryLocalModel>): StoryRemoteKey? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            storyDatabase.storyRemoteKeysDao().getRemoteKeysId(data.id)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, StoryLocalModel>): StoryRemoteKey? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                storyDatabase.storyRemoteKeysDao().getRemoteKeysId(id)
            }
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

}
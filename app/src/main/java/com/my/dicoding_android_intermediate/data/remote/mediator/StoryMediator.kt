package com.my.dicoding_android_intermediate.data.remote.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.my.dicoding_android_intermediate.data.local.entity.KeysRemote
import com.my.dicoding_android_intermediate.data.local.entity.Story
import com.my.dicoding_android_intermediate.data.local.room.StoryDb
import com.my.dicoding_android_intermediate.data.remote.network.ApiService
import com.my.dicoding_android_intermediate.utils.wrapEspressoIdlingResource

@ExperimentalPagingApi
class StoryMediator(
    private val database: StoryDb,
    private val apiService: ApiService,
    private val token: String
) : RemoteMediator<Int, Story>() {

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Story>): MediatorResult {

        // Determine page value based on LoadType
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: INITIAL_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey ?: return MediatorResult.Success(
                    endOfPaginationReached = remoteKeys != null
                )
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeysForLastItem(state)
                val nextKey = remoteKeys?.nextKey ?: return MediatorResult.Success(
                    endOfPaginationReached = remoteKeys != null
                )
                nextKey
            }
        }

        wrapEspressoIdlingResource {
            try {
                val responseData = apiService.getAllStories(token, page, state.config.pageSize)
                val endOfPaginationReached = responseData.storyResponseItems.isEmpty()

                database.withTransaction {
                    if (loadType == LoadType.REFRESH) {
                        database.keysRemoteDao().deleteRemoteKeys()
                        database.storyDao().deleteAll()
                    }

                    val prevKey = if (page == 1) null else page - 1
                    val nextKey = if (endOfPaginationReached) null else page + 1
                    val keys = responseData.storyResponseItems.map {
                        KeysRemote(id = it.id, prevKey = prevKey, nextKey = nextKey)
                    }

                    database.keysRemoteDao().insertAll(keys)

                    responseData.storyResponseItems.forEach { storyResponseItem ->
                        val story = Story(
                            storyResponseItem.id,
                            storyResponseItem.name,
                            storyResponseItem.description,
                            storyResponseItem.createdAt,
                            storyResponseItem.photoUrl,
                            storyResponseItem.lon,
                            storyResponseItem.lat
                        )

                        // Save Story to the local database
                        database.storyDao().insertStory(story)
                    }
                }

                return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)

            } catch (e: Exception) {
                return MediatorResult.Error(e)
            }
        }
    }

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    private suspend fun getRemoteKeysForLastItem(state: PagingState<Int, Story>): KeysRemote? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            database.keysRemoteDao().getRemoteKeysId(data.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Story>): KeysRemote? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            database.keysRemoteDao().getRemoteKeysId(data.id)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, Story>): KeysRemote? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                database.keysRemoteDao().getRemoteKeysId(id)
            }
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}
package com.my.dicoding_android_intermediate.data.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.my.dicoding_android_intermediate.data.database.StoryDatabase
import com.my.dicoding_android_intermediate.data.remote.model.StoryLocalModel
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
        val page = INITIAL_PAGE_INDEX
        try {
            val responseData = apiService.getAllStories(token, page, state.config.pageSize)
            val endOfPaginationReached = responseData.storyResponseItems.isEmpty()
            val storyLocalModelList: List<StoryLocalModel>
            storyDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    storyDatabase.storyDao().deleteAll()
                }

                storyDatabase.storyDao().insertStories(responseData)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: Exception) {
            return MediatorResult.Error(exception)
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

}
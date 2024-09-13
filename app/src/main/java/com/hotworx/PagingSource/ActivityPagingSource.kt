package com.hotworx.PagingSource

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.hotworx.Extensions.await
import com.passio.modulepassio.Singletons.ApiHeaderSingleton
import com.hotworx.activities.DockActivity
import com.hotworx.models.NewActivityModels.NinetyDaysActivity
import com.hotworx.retrofit.WebService

class ActivityPagingSource(val webService: WebService, val context: DockActivity, val session_type: String, val showLoader: Boolean): PagingSource<Int, NinetyDaysActivity>() {


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NinetyDaysActivity> {

        return try{
            val position = params.key ?: 1
            val response = webService.ActivityByLifeTime(
                ApiHeaderSingleton.apiHeader(context),
                position,3, session_type
            ).await(context,showLoader)

            LoadResult.Page(
                data = response.activities ?: emptyList(),
                prevKey = if (position == 1) null else position - 1,
                nextKey = if (position == response.no_of_pages || response.no_of_pages == 0) null else position + 1
            )
        }catch (e: Exception){
            Log.d("Error Paging: ", e.toString())
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, NinetyDaysActivity>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(
                anchorPosition
            )?.prevKey?.plus(1) ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
package com.example.heal_go.data.network.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.heal_go.data.network.api.ApiService
import com.example.heal_go.data.network.response.DiscoverItem
import com.example.heal_go.data.network.response.DiscoverResponse
import java.lang.Exception

class DiscoverPagingSource(
    private val apiService: ApiService,
    private val token: String,
    private val destination: String?,
    private val category: String?
) :
    PagingSource<Int, DiscoverItem>() {
    override fun getRefreshKey(state: PagingState<Int, DiscoverItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DiscoverItem> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val response = apiService.getDataDiscover(token, position-1, destination, category)

            LoadResult.Page(
                data = response.data,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (response.data.isNullOrEmpty()) null else position + 1
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}
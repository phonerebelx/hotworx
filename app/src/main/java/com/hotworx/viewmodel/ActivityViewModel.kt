package com.hotworx.viewmodel



import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.hotworx.PagingSource.ActivityPagingSource
import com.hotworx.activities.DockActivity
import com.hotworx.retrofit.WebService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest

class ActivityViewModel(val webService: WebService, val context: DockActivity, val showLoader: Boolean) : ViewModel() {


    private val sessionTypeFlow = MutableStateFlow("")

    fun setSessionType(sessionType: String) {
        sessionTypeFlow.value = sessionType
    }


    private val pagingConfig = PagingConfig(pageSize = 3, maxSize = 100, enablePlaceholders = false)


    val list = sessionTypeFlow.flatMapLatest { sessionType ->
        Pager(
            config = pagingConfig,
            pagingSourceFactory = { ActivityPagingSource(webService, context, sessionType, showLoader) }
        ).flow
    }.cachedIn(viewModelScope)
}

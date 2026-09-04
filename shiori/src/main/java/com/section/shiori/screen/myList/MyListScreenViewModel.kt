package com.section.shiori.screen.myList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.section.ori.model.ListStatus
import com.section.ori.model.MediaListEntry
import com.section.ori.model.MediaType
import com.section.ori.model.SortOption
import com.section.ori.repository.MediaListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class MyListScreenViewModel @Inject constructor(
    private val mediaListRepository: MediaListRepository
) : ViewModel() {
    private val _filterState = MutableStateFlow(MyListFilterState())
    val filterState: StateFlow<MyListFilterState> = _filterState.asStateFlow()

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    // StateFlow already conflates equal values, so no distinctUntilChanged is needed here:
    // re-selecting the media type or sort that's already active won't rebuild the Pager.
    val pagingDataFlow: Flow<PagingData<MediaListEntry>> = _filterState
        .flatMapLatest { filter ->
            mediaListRepository.observeList(
                mediaType = filter.mediaType,
                status = filter.status,
                sort = filter.sort,
                ascending = filter.ascending
            )
        }
        .cachedIn(viewModelScope)

    fun onMediaTypeSelected(mediaType: MediaType) {
        _filterState.value = _filterState.value.copy(mediaType = mediaType, status = null)
    }

    fun onStatusSelected(status: ListStatus?) {
        _filterState.value = _filterState.value.copy(status = status)
    }

    fun onSortSelected(sort: SortOption, ascending: Boolean) {
        _filterState.value = _filterState.value.copy(sort = sort, ascending = ascending)
    }

    fun onSortDirectionToggled() {
        _filterState.value = _filterState.value.copy(ascending = !_filterState.value.ascending)
    }
}

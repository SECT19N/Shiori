package com.section.ori.repository

import androidx.paging.PagingData
import com.section.ori.model.ListStatus
import com.section.ori.model.MediaListEntry
import com.section.ori.model.MediaType
import com.section.ori.model.SortOption
import kotlinx.coroutines.flow.Flow

interface MediaListRepository {
    fun observeList(
        mediaType: MediaType,
        status: ListStatus?,
        sort: SortOption,
        ascending: Boolean
    ): Flow<PagingData<MediaListEntry>>
}

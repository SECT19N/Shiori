package com.section.shiori.screen.myList

import com.section.ori.model.ListStatus
import com.section.ori.model.MediaType
import com.section.ori.model.SortOption

data class MyListFilterState(
    val mediaType: MediaType = MediaType.ANIME,
    val status: ListStatus? = null,
    val sort: SortOption = SortOption.UPDATED_AT,
    val ascending: Boolean = false
)

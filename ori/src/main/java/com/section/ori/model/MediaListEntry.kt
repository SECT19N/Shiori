package com.section.ori.model

data class MediaListEntry(
    val id: Long,
    val title: String,
    val mainPictureUrl: String?,
    val mediaType: MediaType,
    val format: MediaFormat,
    val seasonYear: Int?,
    val season: String?,
    val totalUnits: Int?,
    val status: ListStatus,
    val score: Int,
    val progress: Int,
    val updatedAtEpochMillis: Long
)

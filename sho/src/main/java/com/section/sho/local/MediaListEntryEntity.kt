package com.section.sho.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.section.ori.model.ListStatus
import com.section.ori.model.MediaFormat
import com.section.ori.model.MediaListEntry
import com.section.ori.model.MediaType

@Entity(tableName = "media_list_entries", primaryKeys = ["id", "mediaType"])
data class MediaListEntryEntity(
    val id: Long,
    val mediaType: String,
    @ColumnInfo(collate = ColumnInfo.NOCASE)
    val title: String,
    val mainPictureUrl: String?,
    val format: String,
    val seasonYear: Int?,
    val season: String?,
    val totalUnits: Int?,
    val status: String,
    val score: Int,
    val progress: Int,
    val updatedAtEpochMillis: Long
)

fun MediaListEntry.toEntity(): MediaListEntryEntity {
    return MediaListEntryEntity(
        id = id,
        mediaType = mediaType.name,
        title = title,
        mainPictureUrl = mainPictureUrl,
        format = format.name,
        seasonYear = seasonYear,
        season = season,
        totalUnits = totalUnits,
        status = status.name,
        score = score,
        progress = progress,
        updatedAtEpochMillis = updatedAtEpochMillis
    )
}

fun MediaListEntryEntity.toDomain(): MediaListEntry {
    return MediaListEntry(
        id = id,
        title = title,
        mainPictureUrl = mainPictureUrl,
        mediaType = MediaType.valueOf(mediaType),
        format = MediaFormat.valueOf(format),
        seasonYear = seasonYear,
        season = season,
        totalUnits = totalUnits,
        status = ListStatus.valueOf(status),
        score = score,
        progress = progress,
        updatedAtEpochMillis = updatedAtEpochMillis
    )
}

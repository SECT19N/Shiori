package com.section.sho.remote.dto

import com.section.ori.model.ListStatus
import com.section.ori.model.MediaFormat
import com.section.ori.model.MediaListEntry
import com.section.ori.model.MediaType
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.Instant
import java.time.format.DateTimeParseException

@JsonClass(generateAdapter = true)
data class MediaListResponseDto(
    @Json(name = "data") val data: List<MediaListNodeDto>,
    @Json(name = "paging") val paging: MediaListPagingDto?
)

@JsonClass(generateAdapter = true)
data class MediaListPagingDto(
    @Json(name = "next") val next: String?
)

@JsonClass(generateAdapter = true)
data class MediaListNodeDto(
    @Json(name = "node") val node: MediaNodeDto,
    @Json(name = "list_status") val listStatus: MediaListStatusDto
)

@JsonClass(generateAdapter = true)
data class MediaNodeDto(
    @Json(name = "id") val id: Long,
    @Json(name = "title") val title: String,
    @Json(name = "main_picture") val mainPicture: MainPictureDto?,
    @Json(name = "media_type") val mediaType: String?,
    @Json(name = "num_episodes") val numEpisodes: Int?,
    @Json(name = "num_chapters") val numChapters: Int?,
    @Json(name = "num_volumes") val numVolumes: Int?,
    @Json(name = "start_season") val startSeason: StartSeasonDto?
)

@JsonClass(generateAdapter = true)
data class MainPictureDto(
    @Json(name = "medium") val medium: String?,
    @Json(name = "large") val large: String?
)

@JsonClass(generateAdapter = true)
data class StartSeasonDto(
    @Json(name = "year") val year: Int?,
    @Json(name = "season") val season: String?
)

@JsonClass(generateAdapter = true)
data class MediaListStatusDto(
    @Json(name = "status") val status: String?,
    @Json(name = "score") val score: Int?,
    @Json(name = "num_episodes_watched") val numEpisodesWatched: Int?,
    @Json(name = "num_chapters_read") val numChaptersRead: Int?,
    @Json(name = "updated_at") val updatedAt: String?
)

fun MediaListNodeDto.toDomain(mediaType: MediaType): MediaListEntry {
    val progress = when (mediaType) {
        MediaType.ANIME -> listStatus.numEpisodesWatched ?: 0
        MediaType.MANGA -> listStatus.numChaptersRead ?: 0
    }
    val totalUnits = when (mediaType) {
        MediaType.ANIME -> node.numEpisodes?.takeIf { it > 0 }
        MediaType.MANGA -> node.numChapters?.takeIf { it > 0 }
    }

    return MediaListEntry(
        id = node.id,
        title = node.title,
        mainPictureUrl = node.mainPicture?.large ?: node.mainPicture?.medium,
        mediaType = mediaType,
        format = node.mediaType.toMediaFormat(),
        seasonYear = node.startSeason?.year,
        season = node.startSeason?.season?.replaceFirstChar { it.uppercase() },
        totalUnits = totalUnits,
        status = listStatus.status.toListStatus(mediaType),
        score = listStatus.score ?: 0,
        progress = progress,
        updatedAtEpochMillis = listStatus.updatedAt.toEpochMillisOrZero()
    )
}

private fun String?.toMediaFormat(): MediaFormat = when (this) {
    "tv" -> MediaFormat.TV
    "movie" -> MediaFormat.MOVIE
    "ova" -> MediaFormat.OVA
    "ona" -> MediaFormat.ONA
    "special" -> MediaFormat.SPECIAL
    "music" -> MediaFormat.MUSIC
    "manga" -> MediaFormat.MANGA
    "novel", "light_novel" -> MediaFormat.NOVEL
    "one_shot" -> MediaFormat.ONE_SHOT
    "manhwa" -> MediaFormat.MANHWA
    "manhua" -> MediaFormat.MANHUA
    "oel" -> MediaFormat.OEL
    else -> MediaFormat.UNKNOWN
}

private fun String?.toListStatus(mediaType: MediaType): ListStatus = when (this) {
    "watching" -> ListStatus.WATCHING
    "reading" -> ListStatus.READING
    "completed" -> ListStatus.COMPLETED
    "on_hold" -> ListStatus.ON_HOLD
    "dropped" -> ListStatus.DROPPED
    "plan_to_watch" -> ListStatus.PLAN_TO_WATCH
    "plan_to_read" -> ListStatus.PLAN_TO_READ
    else -> if (mediaType == MediaType.ANIME) ListStatus.PLAN_TO_WATCH else ListStatus.PLAN_TO_READ
}

private fun String?.toEpochMillisOrZero(): Long {
    if (this.isNullOrEmpty()) return 0L
    return try {
        Instant.parse(this).toEpochMilli()
    } catch (e: DateTimeParseException) {
        0L
    }
}

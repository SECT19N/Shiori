package com.section.shiori.screen.myList

import androidx.compose.ui.graphics.Color
import com.section.ori.model.ListStatus
import com.section.ori.model.MediaFormat
import com.section.ori.model.MediaType
import com.section.ori.model.SortOption
import com.section.shiori.ui.theme.StatusCompleted
import com.section.shiori.ui.theme.StatusDropped
import com.section.shiori.ui.theme.StatusOnHold
import com.section.shiori.ui.theme.StatusPlanned
import com.section.shiori.ui.theme.StatusWatching

fun ListStatus.color(): Color = when (this) {
    ListStatus.WATCHING, ListStatus.READING -> StatusWatching
    ListStatus.COMPLETED -> StatusCompleted
    ListStatus.PLAN_TO_WATCH, ListStatus.PLAN_TO_READ -> StatusPlanned
    ListStatus.DROPPED -> StatusDropped
    ListStatus.ON_HOLD -> StatusOnHold
}

fun ListStatus.displayLabel(): String = when (this) {
    ListStatus.WATCHING -> "Watching"
    ListStatus.READING -> "Reading"
    ListStatus.COMPLETED -> "Completed"
    ListStatus.ON_HOLD -> "On Hold"
    ListStatus.DROPPED -> "Dropped"
    ListStatus.PLAN_TO_WATCH -> "Plan to Watch"
    ListStatus.PLAN_TO_READ -> "Plan to Read"
}

fun statusFilters(mediaType: MediaType): List<Pair<ListStatus?, String>> {
    val inProgress = if (mediaType == MediaType.ANIME) ListStatus.WATCHING else ListStatus.READING
    val planned = if (mediaType == MediaType.ANIME) ListStatus.PLAN_TO_WATCH else ListStatus.PLAN_TO_READ

    return listOf(
        null to "All",
        inProgress to inProgress.displayLabel(),
        ListStatus.COMPLETED to ListStatus.COMPLETED.displayLabel(),
        ListStatus.ON_HOLD to ListStatus.ON_HOLD.displayLabel(),
        ListStatus.DROPPED to ListStatus.DROPPED.displayLabel(),
        planned to planned.displayLabel()
    )
}

fun MediaFormat.displayLabel(): String = when (this) {
    MediaFormat.TV -> "TV"
    MediaFormat.MOVIE -> "Movie"
    MediaFormat.OVA -> "OVA"
    MediaFormat.ONA -> "ONA"
    MediaFormat.SPECIAL -> "Special"
    MediaFormat.MUSIC -> "Music"
    MediaFormat.MANGA -> "Manga"
    MediaFormat.NOVEL -> "Light Novel"
    MediaFormat.ONE_SHOT -> "One Shot"
    MediaFormat.MANHWA -> "Manhwa"
    MediaFormat.MANHUA -> "Manhua"
    MediaFormat.OEL -> "OEL"
    MediaFormat.UNKNOWN -> "Unknown"
}

fun SortOption.displayLabel(): String = when (this) {
    SortOption.SCORE -> "Score"
    SortOption.UPDATED_AT -> "Last Updated"
    SortOption.TITLE -> "Title"
    SortOption.START_DATE -> "Start Date"
}

fun SortOption.directionLabel(ascending: Boolean): String = when (this) {
    SortOption.SCORE -> if (ascending) "Lowest first" else "Highest first"
    SortOption.UPDATED_AT -> if (ascending) "Oldest first" else "Newest first"
    SortOption.TITLE -> if (ascending) "A–Z" else "Z–A"
    SortOption.START_DATE -> if (ascending) "Oldest first" else "Newest first"
}

fun progressUnitLabel(mediaType: MediaType): String = when (mediaType) {
    MediaType.ANIME -> "Ep"
    MediaType.MANGA -> "Ch"
}

package com.section.shiori.screen.myList

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.ButtonShapes
import androidx.compose.material3.Card
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButton
import androidx.compose.material3.ToggleButtonShapes
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil3.compose.AsyncImage
import com.section.ori.model.ListStatus
import com.section.ori.model.MediaListEntry
import com.section.ori.model.MediaType
import com.section.ori.model.SortOption
import com.section.shiori.nav.ShellScreen

private val CardContentHeight = 108.dp

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MyListScreen(
    rootNavController: NavController,
    viewModel: MyListScreenViewModel = hiltViewModel(),
) {
    val filterState by viewModel.filterState.collectAsState()
    val lazyPagingItems = viewModel.pagingDataFlow.collectAsLazyPagingItems()
    var showFiltersSheet by remember { mutableStateOf(false) }
    var showAddTitleDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(if (filterState.mediaType == MediaType.ANIME) "My Anime" else "My Manga")
                },
                actions = {
                    IconButton(onClick = { showFiltersSheet = true }) {
                        Icon(Icons.Filled.FilterList, contentDescription = "Filters")
                    }
                    IconButton(onClick = { rootNavController.navigate(ShellScreen.Profile.route) }) {
                        Icon(Icons.Filled.AccountCircle, contentDescription = "Profile")
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showAddTitleDialog = true },
                icon = { Icon(Icons.Filled.Add, contentDescription = null) },
                text = { Text("Add Title") }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(ButtonGroupDefaults.ConnectedSpaceBetween)
            ) {
                val mediaTypes = listOf(
                    MediaType.ANIME to "Anime",
                    MediaType.MANGA to "Manga"
                )
                mediaTypes.forEachIndexed { index, (mediaType, label) ->
                    val leading = index == 0
                    ToggleButton(
                        checked = mediaType == filterState.mediaType,
                        onCheckedChange = { if (it) viewModel.onMediaTypeSelected(mediaType) },
                        modifier = Modifier.weight(1f),
                        shapes = ToggleButtonShapes(
                            shape = if (leading) {
                                ButtonGroupDefaults.connectedLeadingButtonShape
                            } else {
                                ButtonGroupDefaults.connectedTrailingButtonShape
                            },
                            pressedShape = if (leading) {
                                ButtonGroupDefaults.connectedLeadingButtonPressShape
                            } else {
                                ButtonGroupDefaults.connectedTrailingButtonPressShape
                            },
                            checkedShape = ButtonGroupDefaults.connectedButtonCheckedShape
                        )
                    ) {
                        Text(label)
                    }
                }
            }

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                itemsIndexed(statusFilters(filterState.mediaType)) { _, (status, label) ->
                    FilterChip(
                        contentPadding = PaddingValues(horizontal = 10.dp),
                        selected = status == filterState.status,
                        onClick = { viewModel.onStatusSelected(status) },
                        label = { Text(label) },
                        shape = RoundedCornerShape(24.dp),
                        leadingIcon = if (status != null) {
                            {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(status.color())
                                )
                            }
                        } else null
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = viewModel::onSortDirectionToggled,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = if (filterState.ascending) {
                            Icons.Filled.ArrowUpward
                        } else {
                            Icons.Filled.ArrowDownward
                        },
                        contentDescription = if (filterState.ascending) {
                            "Sorting ascending, tap to sort descending"
                        } else {
                            "Sorting descending, tap to sort ascending"
                        },
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Sorted by ${filterState.sort.displayLabel()} · ${
                        filterState.sort.directionLabel(
                            filterState.ascending
                        )
                    }",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { showFiltersSheet = true }
                        .padding(vertical = 8.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${lazyPagingItems.itemCount} shown",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            val isRefreshing = lazyPagingItems.loadState.refresh is LoadState.Loading
            val pullToRefreshState = rememberPullToRefreshState()
            PullToRefreshBox(
                isRefreshing = isRefreshing,
                onRefresh = { lazyPagingItems.refresh() },
                modifier = Modifier.fillMaxSize(),
                state = pullToRefreshState,
                indicator = {
                    PullToRefreshDefaults.LoadingIndicator(
                        state = pullToRefreshState,
                        isRefreshing = isRefreshing,
                        modifier = Modifier.align(Alignment.TopCenter)
                    )
                }
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 96.dp)
                ) {
                    items(
                        count = lazyPagingItems.itemCount,
                        key = lazyPagingItems.itemKey { it.id },
                        contentType = { "entry" }
                    ) { index ->
                        val entry = lazyPagingItems[index]
                        if (entry != null) {
                            Column {
                                // Under "All", rows come back grouped by status; label where
                                // each group starts so the sections read as sections instead of
                                // one merged list. peek() avoids triggering a page load.
                                if (filterState.status == null) {
                                    val previousStatus = if (index == 0) {
                                        null
                                    } else {
                                        lazyPagingItems.peek(index - 1)?.status
                                    }
                                    if (previousStatus != entry.status) {
                                        StatusSectionHeader(status = entry.status)
                                    }
                                }
                                MyListCard(
                                    entry = entry,
                                    mediaType = filterState.mediaType,
                                    onIncrementClick = { /* TODO: wire up progress +1 with the edit sheet work */ },
                                    onEditClick = { /* TODO: opens edit sheet, follow-up task */ }
                                )
                            }
                        }
                    }

                    if (lazyPagingItems.loadState.append is LoadState.Loading) {
                        item(contentType = "appendSpinner") {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                ContainedLoadingIndicator()
                            }
                        }
                    }
                }

                // First load of a media type we have no cache for: show an explicit indicator
                // rather than an empty list, so the Anime/Manga switch reads as loading.
                if (isRefreshing && lazyPagingItems.itemCount == 0) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        ContainedLoadingIndicator()
                    }
                }
            }
        }
    }

    if (showFiltersSheet) {
        val sheetState = rememberBottomSheetState(initialValue = SheetValue.Hidden)
        ModalBottomSheet(
            onDismissRequest = { showFiltersSheet = false },
            sheetState = sheetState
        ) {
            MyListFiltersSheetContent(
                selectedSort = filterState.sort,
                ascending = filterState.ascending,
                onSortSelected = viewModel::onSortSelected
            )
        }
    }

    if (showAddTitleDialog) {
        AddTitleDialog(
            mediaType = filterState.mediaType,
            onDismissRequest = { showAddTitleDialog = false }
        )
    }
}

/**
 * Full-screen "add a title to my list" flow. Stub for now — search + add lands with the
 * edit-sheet work.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddTitleDialog(
    mediaType: MediaType,
    onDismissRequest: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(modifier = Modifier.fillMaxSize()) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                if (mediaType == MediaType.ANIME) "Add Anime" else "Add Manga"
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = onDismissRequest) {
                                Icon(Icons.Filled.Close, contentDescription = "Close")
                            }
                        }
                    )
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Search and add coming soon.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun MyListFiltersSheetContent(
    selectedSort: SortOption,
    ascending: Boolean,
    onSortSelected: (SortOption, Boolean) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Sort by", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        SortOption.entries.forEach { sortOption ->
            Text(
                text = sortOption.displayLabel(),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            ) {
                listOf(false, true).forEach { candidateAscending ->
                    FilterChip(
                        selected = selectedSort == sortOption && ascending == candidateAscending,
                        onClick = { onSortSelected(sortOption, candidateAscending) },
                        label = { Text(sortOption.directionLabel(candidateAscending)) }
                    )
                }
            }
        }
    }
}

@Composable
private fun StatusSectionHeader(status: ListStatus) {
    val statusColor = status.color()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(statusColor)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = status.displayLabel(),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(12.dp))
        HorizontalDivider(color = statusColor.copy(alpha = 0.35f))
    }
}

@Composable
private fun MyListCard(
    entry: MediaListEntry,
    mediaType: MediaType,
    onIncrementClick: () -> Unit,
    onEditClick: () -> Unit,
) {
    // The card itself stays on the app's blue theme surface — only the accents (dot, score,
    // progress bar, action buttons) carry the status color, so a green entry doesn't fight
    // the blue background.
    val statusColor = entry.status.color()
    val onStatusColor = contentColorForStatus(statusColor)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(28.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = entry.mainPictureUrl,
                contentDescription = entry.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(CardContentHeight)
                    .width(76.dp)
                    .clip(RoundedCornerShape(16.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .height(CardContentHeight)
                    .padding(vertical = 2.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = entry.title,
                        style = MaterialTheme.typography.titleSmall,
                        // Two lines, then ellipsize. The row is width-bounded by weight(1f)
                        // and height-bounded here, so a long title can never push the meta
                        // and progress rows out of the card.
                        maxLines = 2,
                        minLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 18.sp,
                        modifier = Modifier.weight(1f)
                    )
                    if (entry.score > 0) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "★ ${entry.score}",
                            style = MaterialTheme.typography.labelMedium,
                            color = statusColor
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(statusColor)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = mediaSubtitle(entry, mediaType),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    val totalLabel = entry.totalUnits?.toString() ?: "?"
                    Text(
                        text = "${progressUnitLabel(mediaType)} ${entry.progress}/$totalLabel",
                        style = MaterialTheme.typography.labelMedium,
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    LinearWavyProgressIndicator(
                        progress = { progressFraction(entry) },
                        modifier = Modifier.weight(1f),
                        color = statusColor,
                        trackColor = statusColor.copy(alpha = 0.24f),
                        // Amplitude is a 0..1 fraction of the component's max wave height;
                        // the default is a flat 1f, which is far too tall at this size.
                        amplitude = { 0.3f }
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Vertical connected button group: taller increment on top, shorter edit below.
            // Both morph and pop on press, the way an M3 expressive ButtonGroup child does.
            Column(
                modifier = Modifier
                    .width(48.dp)
                    .height(CardContentHeight),
                verticalArrangement = Arrangement.spacedBy(ButtonGroupDefaults.ConnectedSpaceBetween)
            ) {
                PoppingActionButton(
                    onClick = onIncrementClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1.6f),
                    shapes = ButtonShapes(
                        shape = RoundedCornerShape(
                            topStart = 24.dp,
                            topEnd = 24.dp,
                            bottomStart = 6.dp,
                            bottomEnd = 6.dp
                        ),
                        pressedShape = RoundedCornerShape(
                            topStart = 12.dp,
                            topEnd = 12.dp,
                            bottomStart = 20.dp,
                            bottomEnd = 20.dp
                        )
                    ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = statusColor,
                        contentColor = onStatusColor
                    )
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Increment progress")
                }

                PoppingActionButton(
                    onClick = onEditClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    shapes = ButtonShapes(
                        shape = RoundedCornerShape(
                            topStart = 6.dp,
                            topEnd = 6.dp,
                            bottomStart = 24.dp,
                            bottomEnd = 24.dp
                        ),
                        pressedShape = RoundedCornerShape(
                            topStart = 20.dp,
                            topEnd = 20.dp,
                            bottomStart = 12.dp,
                            bottomEnd = 12.dp
                        )
                    ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = statusColor.copy(alpha = 0.18f)
                            .compositeOver(MaterialTheme.colorScheme.surfaceContainerHighest),
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Icon(
                        Icons.Filled.Edit,
                        contentDescription = "Edit entry",
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

/**
 * A [Button] that morphs its corners while pressed. That shape morph is the whole animation:
 * an extra scale on top of it read as janky in the list, so it's deliberately not there.
 */
@Composable
private fun PoppingActionButton(
    onClick: () -> Unit,
    shapes: ButtonShapes,
    colors: ButtonColors,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    Button(
        onClick = onClick,
        shapes = shapes,
        modifier = modifier,
        colors = colors,
        contentPadding = PaddingValues(0.dp),
        content = content
    )
}

/** Black or white, whichever stays legible on top of a status color. */
private fun contentColorForStatus(statusColor: Color): Color =
    if (statusColor.luminance() > 0.5f) Color.Black else Color.White

private fun mediaSubtitle(entry: MediaListEntry, mediaType: MediaType): String {
    val seasonText = if (entry.season != null && entry.seasonYear != null) {
        "${entry.season} ${entry.seasonYear}"
    } else {
        entry.seasonYear?.toString()
    }
    val unitsText = entry.totalUnits?.let { total ->
        val unit = if (mediaType == MediaType.ANIME) "ep" else "ch"
        "$total $unit${if (total != 1) "s" else ""}"
    }

    return listOfNotNull(entry.format.displayLabel(), seasonText, unitsText).joinToString(" · ")
}

private fun progressFraction(entry: MediaListEntry): Float {
    val total = entry.totalUnits ?: return 0f
    if (total <= 0) return 0f
    return (entry.progress.toFloat() / total).coerceIn(0f, 1f)
}

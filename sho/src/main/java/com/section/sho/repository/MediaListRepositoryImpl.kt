package com.section.sho.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.section.ori.model.ListStatus
import com.section.ori.model.MediaListEntry
import com.section.ori.model.MediaType
import com.section.ori.model.SortOption
import com.section.ori.repository.MediaListRepository
import com.section.sho.local.MediaListDao
import com.section.sho.local.MediaListEntryEntity
import com.section.sho.local.toDomain
import com.section.sho.remote.MediaListRemoteMediator
import com.section.sho.remote.service.MediaListApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MediaListRepositoryImpl @Inject constructor(
    private val mediaListApiService: MediaListApiService,
    private val mediaListDao: MediaListDao
) : MediaListRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun observeList(
        mediaType: MediaType,
        status: ListStatus?,
        sort: SortOption,
        ascending: Boolean
    ): Flow<PagingData<MediaListEntry>> {
        val statusColumn = status?.name

        val pagingSourceFactory = {
            when (sort) {
                SortOption.SCORE ->
                    mediaListDao.pagingSourceByScore(mediaType.name, statusColumn, ascending)

                SortOption.UPDATED_AT ->
                    mediaListDao.pagingSourceByUpdatedAt(mediaType.name, statusColumn, ascending)

                SortOption.TITLE ->
                    mediaListDao.pagingSourceByTitle(mediaType.name, statusColumn, ascending)

                SortOption.START_DATE ->
                    mediaListDao.pagingSourceByStartDate(mediaType.name, statusColumn, ascending)
            }
        }

        return Pager(
            config = PagingConfig(
                pageSize = MediaListApiService.PAGE_SIZE,
                // Paging defaults initialLoadSize to 3 * pageSize, i.e. a 300-row Room read
                // before the first frame can be drawn. Switching media type rebuilds the Pager,
                // so that read is on the critical path of every Anime/Manga toggle — keep it
                // to a single page and prefetch more lazily instead.
                initialLoadSize = MediaListApiService.PAGE_SIZE,
                prefetchDistance = 20,
                enablePlaceholders = false
            ),
            remoteMediator = MediaListRemoteMediator(mediaType, mediaListApiService, mediaListDao),
            pagingSourceFactory = pagingSourceFactory
        ).flow.map { pagingData: PagingData<MediaListEntryEntity> ->
            pagingData.map { it.toDomain() }
        }
    }
}

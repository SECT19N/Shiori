package com.section.sho.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.section.ori.model.MediaType
import com.section.sho.local.MediaListDao
import com.section.sho.local.MediaListEntryEntity
import com.section.sho.local.toEntity
import com.section.sho.remote.dto.toDomain
import com.section.sho.remote.service.MediaListApiService
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class MediaListRemoteMediator(
    private val mediaType: MediaType,
    private val mediaListApiService: MediaListApiService,
    private val mediaListDao: MediaListDao
) : RemoteMediator<Int, MediaListEntryEntity>() {

    override suspend fun initialize(): InitializeAction {
        val hasCachedData = mediaListDao.countByMediaType(mediaType.name) > 0
        return if (hasCachedData) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MediaListEntryEntity>
    ): MediatorResult {
        return try {
            val offset = when (loadType) {
                LoadType.REFRESH -> 0
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> mediaListDao.countByMediaType(mediaType.name)
            }

            val response = when (mediaType) {
                MediaType.ANIME -> mediaListApiService.getAnimeList(offset = offset)
                MediaType.MANGA -> mediaListApiService.getMangaList(offset = offset)
            }

            val entities = response.data.map { it.toDomain(mediaType).toEntity() }

            if (loadType == LoadType.REFRESH) {
                mediaListDao.clearByMediaType(mediaType.name)
            }
            mediaListDao.insertAll(entities)

            MediatorResult.Success(endOfPaginationReached = response.paging?.next == null)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}

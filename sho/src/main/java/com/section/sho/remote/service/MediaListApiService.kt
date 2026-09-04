package com.section.sho.remote.service

import com.section.sho.remote.dto.MediaListResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface MediaListApiService {
    @GET("users/@me/animelist")
    suspend fun getAnimeList(
        @Query("fields") fields: String = LIST_FIELDS,
        @Query("limit") limit: Int = PAGE_SIZE,
        @Query("offset") offset: Int
    ): MediaListResponseDto

    @GET("users/@me/mangalist")
    suspend fun getMangaList(
        @Query("fields") fields: String = LIST_FIELDS,
        @Query("limit") limit: Int = PAGE_SIZE,
        @Query("offset") offset: Int
    ): MediaListResponseDto

    companion object {
        const val PAGE_SIZE = 100
        private const val LIST_FIELDS =
            "list_status,media_type,num_episodes,num_chapters,num_volumes,start_season"
    }
}

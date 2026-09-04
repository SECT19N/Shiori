package com.section.sho.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * Every paging query groups by status first, in the same order the status filter chips use
 * (in progress → completed → on hold → dropped → planned), and only then applies the user's
 * chosen sort within each group. When a single status is filtered the grouping term is a no-op.
 */
private const val STATUS_ORDER = """
    CASE status
        WHEN 'WATCHING' THEN 0
        WHEN 'READING' THEN 0
        WHEN 'COMPLETED' THEN 1
        WHEN 'ON_HOLD' THEN 2
        WHEN 'DROPPED' THEN 3
        ELSE 4
    END ASC
"""

@Dao
interface MediaListDao {
    @Query(
        """
        SELECT * FROM media_list_entries
        WHERE mediaType = :mediaType AND (:status IS NULL OR status = :status)
        ORDER BY
            $STATUS_ORDER,
            CASE WHEN :ascending THEN score END ASC,
            CASE WHEN NOT :ascending THEN score END DESC
        """
    )
    fun pagingSourceByScore(
        mediaType: String,
        status: String?,
        ascending: Boolean
    ): PagingSource<Int, MediaListEntryEntity>

    @Query(
        """
        SELECT * FROM media_list_entries
        WHERE mediaType = :mediaType AND (:status IS NULL OR status = :status)
        ORDER BY
            $STATUS_ORDER,
            CASE WHEN :ascending THEN updatedAtEpochMillis END ASC,
            CASE WHEN NOT :ascending THEN updatedAtEpochMillis END DESC
        """
    )
    fun pagingSourceByUpdatedAt(
        mediaType: String,
        status: String?,
        ascending: Boolean
    ): PagingSource<Int, MediaListEntryEntity>

    @Query(
        """
        SELECT * FROM media_list_entries
        WHERE mediaType = :mediaType AND (:status IS NULL OR status = :status)
        ORDER BY
            $STATUS_ORDER,
            CASE WHEN :ascending THEN title END ASC,
            CASE WHEN NOT :ascending THEN title END DESC
        """
    )
    fun pagingSourceByTitle(
        mediaType: String,
        status: String?,
        ascending: Boolean
    ): PagingSource<Int, MediaListEntryEntity>

    @Query(
        """
        SELECT * FROM media_list_entries
        WHERE mediaType = :mediaType AND (:status IS NULL OR status = :status)
        ORDER BY
            $STATUS_ORDER,
            CASE WHEN :ascending THEN seasonYear END ASC,
            CASE WHEN NOT :ascending THEN seasonYear END DESC
        """
    )
    fun pagingSourceByStartDate(
        mediaType: String,
        status: String?,
        ascending: Boolean
    ): PagingSource<Int, MediaListEntryEntity>

    @Query("SELECT COUNT(*) FROM media_list_entries WHERE mediaType = :mediaType")
    suspend fun countByMediaType(mediaType: String): Int

    @Query("DELETE FROM media_list_entries WHERE mediaType = :mediaType")
    suspend fun clearByMediaType(mediaType: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entries: List<MediaListEntryEntity>)
}

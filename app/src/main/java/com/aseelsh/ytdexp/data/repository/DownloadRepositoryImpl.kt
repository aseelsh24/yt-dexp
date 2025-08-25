package com.aseelsh.ytdexp.data.repository

import android.util.Log
import com.aseelsh.ytdexp.data.db.DownloadDao
import com.aseelsh.ytdexp.data.db.DownloadEntity
import com.aseelsh.ytdexp.data.extractor.VideoExtractor
import com.aseelsh.ytdexp.domain.model.DownloadItem
import com.aseelsh.ytdexp.domain.model.DownloadStatus
import com.aseelsh.ytdexp.domain.repository.DownloadRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DownloadRepositoryImpl
    @Inject
    constructor(
        private val downloadDao: DownloadDao,
        private val videoExtractor: VideoExtractor,
    ) : DownloadRepository {
        override suspend fun addDownload(
            url: String,
            format: String,
        ): Result<DownloadItem> =
            try {
                val videoInfo = videoExtractor.extractVideoInfo(url)
                val downloadId = UUID.randomUUID().toString()

                val entity =
                    DownloadEntity(
                        id = downloadId,
                        url = url,
                        title = videoInfo.title,
                        thumbnail = videoInfo.thumbnail,
                        format = format,
                        size = 0L,
                        progress = 0,
                        status = DownloadStatus.PENDING.name,
                    )

                downloadDao.insertDownload(entity)

                Result.success(entity.toDownloadItem())
            } catch (e: IOException) {
                Log.e(TAG, "Failed to add download", e)
                Result.failure(e)
            }

        override suspend fun pauseDownload(id: String) {
            downloadDao.getDownloadById(id)?.let { entity ->
                downloadDao.updateDownload(
                    entity.copy(status = DownloadStatus.PAUSED.name),
                )
            }
        }

        override suspend fun resumeDownload(id: String) {
            downloadDao.getDownloadById(id)?.let { entity ->
                downloadDao.updateDownload(
                    entity.copy(status = DownloadStatus.DOWNLOADING.name),
                )
            }
        }

        override suspend fun cancelDownload(id: String) {
            downloadDao.getDownloadById(id)?.let { entity ->
                downloadDao.deleteDownload(entity)
            }
        }

        override suspend fun getDownload(id: String): DownloadItem? = downloadDao.getDownloadById(id)?.toDownloadItem()

        override fun getAllDownloads(): Flow<List<DownloadItem>> =
            downloadDao
                .getAllDownloads()
                .map { entities -> entities.map { it.toDownloadItem() } }

        override fun getDownloadProgress(id: String): Flow<Int> =
            downloadDao
                .getAllDownloads()
                .map { downloads ->
                    downloads.find { it.id == id }?.progress ?: 0
                }

        private fun DownloadEntity.toDownloadItem(): DownloadItem =
            DownloadItem(
                id = id,
                url = url,
                title = title,
                thumbnail = thumbnail,
                format = format,
                size = size,
                progress = progress,
                status = DownloadStatus.valueOf(status),
            )

        companion object {
            private const val TAG = "DownloadRepositoryImpl"
        }
    }

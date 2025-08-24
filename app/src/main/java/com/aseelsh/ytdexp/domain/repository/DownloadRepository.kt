package com.aseelsh.ytdexp.domain.repository

import com.aseelsh.ytdexp.domain.model.DownloadItem
import kotlinx.coroutines.flow.Flow

interface DownloadRepository {
    suspend fun addDownload(url: String, format: String): Result<DownloadItem>
    suspend fun pauseDownload(id: String)
    suspend fun resumeDownload(id: String)
    suspend fun cancelDownload(id: String)
    suspend fun getDownload(id: String): DownloadItem?
    fun getAllDownloads(): Flow<List<DownloadItem>>
    fun getDownloadProgress(id: String): Flow<Int>
}

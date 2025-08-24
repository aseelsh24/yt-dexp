package com.aseelsh.ytdexp.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "downloads")
data class DownloadEntity(
    @PrimaryKey
    val id: String,
    val url: String,
    val title: String,
    val thumbnail: String?,
    val format: String,
    val size: Long,
    val progress: Int,
    val status: String,
    val timestamp: Long = System.currentTimeMillis(),
    val filePath: String? = null,
)

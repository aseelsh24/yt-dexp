package com.aseelsh.ytdexp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aseelsh.ytdexp.domain.model.DownloadItem
import com.aseelsh.ytdexp.domain.model.DownloadStatus

@Composable
fun DownloadsScreen(
    downloads: List<DownloadItem> = emptyList(),
    onPauseResume: (String) -> Unit = {},
    onCancel: (String) -> Unit = {}
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(downloads) { download ->
            DownloadItemCard(
                downloadItem = download,
                onPauseResume = onPauseResume,
                onCancel = onCancel
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DownloadItemCard(
    downloadItem: DownloadItem,
    onPauseResume: (String) -> Unit,
    onCancel: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = downloadItem.title,
                style = MaterialTheme.typography.titleMedium
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            LinearProgressIndicator(
                progress = downloadItem.progress / 100f,
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${downloadItem.progress}%",
                    style = MaterialTheme.typography.bodyMedium
                )
                
                Row {
                    IconButton(onClick = { onPauseResume(downloadItem.id) }) {
                        Icon(
                            if (downloadItem.status == DownloadStatus.DOWNLOADING) 
                                Icons.Default.Pause 
                            else 
                                Icons.Default.PlayArrow,
                            contentDescription = "Pause/Resume"
                        )
                    }
                    
                    IconButton(onClick = { onCancel(downloadItem.id) }) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Cancel"
                        )
                    }
                }
            }
        }
    }
}

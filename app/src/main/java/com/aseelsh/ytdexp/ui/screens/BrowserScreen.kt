package com.aseelsh.ytdexp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrowserScreen(
    url: String,
    onUrlChange: (String) -> Unit,
    onDownloadClick: (String, String) -> Unit,
    isUrlValid: Boolean,
    modifier: Modifier = Modifier,
) {
    var showFormatDialog by remember { mutableStateOf(false) }
    var currentVideoUrl by remember { mutableStateOf("") }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        OutlinedTextField(
            value = url,
            onValueChange = onUrlChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            label = { Text("Enter URL") },
            trailingIcon = {
                if (isUrlValid) {
                    IconButton(onClick = {
                        currentVideoUrl = url
                        showFormatDialog = true
                    }) {
                        Icon(Icons.Default.Download, "Download")
                    }
                }
            },
        )

        if (url.isNotEmpty()) {
            val webViewState = rememberWebViewState(url = url)
            WebView(
                state = webViewState,
                modifier = Modifier.weight(1f),
                onCreated = { webView ->
                    webView.settings.apply {
                        javaScriptEnabled = true
                        domStorageEnabled = true
                        databaseEnabled = true
                    }
                },
            )
        }
    }

    if (showFormatDialog) {
        FormatChooserDialog(
            onDismissRequest = { showFormatDialog = false },
            onDownloadClick = onDownloadClick,
            url = currentVideoUrl
        )
    }
}

@Composable
private fun FormatChooserDialog(
    onDismissRequest: () -> Unit,
    onDownloadClick: (String, String) -> Unit,
    url: String
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Choose Format") },
        text = {
            Column {
                Text("Select download format:")
                Spacer(modifier = Modifier.height(8.dp))
                FormatButton("MP4 - Video", "mp4", url, onDownloadClick)
                FormatButton("MP3 - Audio", "mp3", url, onDownloadClick)
                FormatButton("WEBM - Video", "webm", url, onDownloadClick)
            }
        },
        confirmButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cancel")
            }
        },
    )
}

@Composable
private fun FormatButton(
    text: String,
    format: String,
    url: String,
    onDownloadClick: (String, String) -> Unit,
) {
    Button(
        onClick = {
            onDownloadClick(url, format)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
    ) {
        Text(text)
    }
}

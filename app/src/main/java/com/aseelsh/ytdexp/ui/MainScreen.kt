package com.aseelsh.ytdexp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel()
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Browser", "Downloads", "Settings")
    
    val downloadState by viewModel.downloadState.collectAsState()
    val currentUrl by viewModel.currentUrl.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("YT-Dexp") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors()
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // URL Input Field
            OutlinedTextField(
                value = currentUrl,
                onValueChange = { viewModel.updateUrl(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                label = { Text("Enter URL") },
                trailingIcon = {
                    IconButton(onClick = { viewModel.startDownload() }) {
                        Icon(Icons.Default.Download, "Download")
                    }
                }
            )
            
            TabRow(selectedTabIndex = selectedTab) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { 
                            Text(
                                text = title,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        },
                        icon = {
                            when (index) {
                                0 -> Icon(Icons.Default.Search, contentDescription = null)
                                1 -> Icon(Icons.Default.Download, contentDescription = null)
                                2 -> Icon(Icons.Default.Settings, contentDescription = null)
                            }
                        }
                    )
                }
            }

            when (selectedTab) {
                0 -> BrowserScreen()
                1 -> DownloadsScreen()
                2 -> SettingsScreen()
            }
        }
    }
}

@Composable
fun BrowserScreen() {
    // TODO: Implement browser screen with WebView
}

@Composable
fun DownloadsScreen() {
    // TODO: Implement downloads screen
}

@Composable
fun SettingsScreen() {
    // TODO: Implement settings screen
}

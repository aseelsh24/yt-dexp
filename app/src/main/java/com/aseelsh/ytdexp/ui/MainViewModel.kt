package com.aseelsh.ytdexp.ui

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel
    @Inject
    constructor(
        private val downloadRepository: DownloadRepository,
    ) : ViewModel() {
        private val _currentUrl = MutableStateFlow("")
        val currentUrl = _currentUrl.asStateFlow()

        private val _downloadState = MutableStateFlow<List<DownloadItem>>(emptyList())
        val downloadState = _downloadState.asStateFlow()

        init {
            viewModelScope.launch {
                downloadRepository
                    .getAllDownloads()
                    .collect { downloads ->
                        _downloadState.value = downloads
                    }
            }
        }

        fun updateUrl(url: String) {
            _currentUrl.value = url
        }

        fun startDownload() {
            val url = _currentUrl.value
            if (url.isNotBlank()) {
                viewModelScope.launch {
                    downloadRepository.addDownload(url, "mp4")
                    _currentUrl.value = "" // Clear the input field
                }
            }
        }

        fun pauseDownload(id: String) {
            viewModelScope.launch {
                downloadRepository.pauseDownload(id)
            }
        }

        fun resumeDownload(id: String) {
            viewModelScope.launch {
                downloadRepository.resumeDownload(id)
            }
        }

        fun cancelDownload(id: String) {
            viewModelScope.launch {
                downloadRepository.cancelDownload(id)
            }
        }
    }

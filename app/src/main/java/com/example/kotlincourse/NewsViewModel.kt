package com.example.kotlincourse

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(private val repository: NewsRepository) : ViewModel() {
    var articles by mutableStateOf<List<Article>>(emptyList())
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    init {
        fetchNews()
    }

      fun fetchNews() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                articles = repository.getTopHeadlines()
            } catch (e: Exception) {
                errorMessage = "Failed to load news: ${e.message}"
            }
            isLoading = false
        }
    }
}
package com.example.kotlincourse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NewsApp()
        }
    }
}

@Composable
fun NewsApp(viewModel: NewsViewModel = viewModel(factory = NewsViewModelFactory(NewsRepository()))){
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column {
            when {
                viewModel.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.wrapContentSize())
                }
                viewModel.errorMessage != null -> {
                    Text(
                        text = viewModel.errorMessage ?: "Unknown error",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                viewModel.articles.isEmpty() -> {
                    Text("No articles found", modifier = Modifier.padding(16.dp))
                }
                else -> {
                    LazyColumn {
                        items(viewModel.articles) { article ->
                            NewsItem(article)
                        }
                    }
                    Button(onClick = { viewModel.fetchNews() }) { Text("Refresh") }
                }
            }
        }
    }
}

@Composable
fun NewsItem(article: Article) {
    Row(modifier = Modifier.padding(8.dp)) {
        AsyncImage(
            model = article.urlToImage ?: "https://via.placeholder.com/100",
            contentDescription = article.title,
            modifier = Modifier.size(100.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = article.title ?: "No title available",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}



class NewsViewModelFactory(private val repository: NewsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NewsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
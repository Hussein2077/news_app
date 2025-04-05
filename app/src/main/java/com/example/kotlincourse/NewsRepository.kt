package com.example.kotlincourse

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


class NewsRepository  {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://newsapi.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val newsApi = retrofit.create(NewsApi::class.java)

    suspend fun getTopHeadlines(): List<Article> {
        val response = newsApi.getTopHeadlines("us", "95b779f2a8014ca19f744e868885223d") // Replace with your API key
        return response.articles
    }
}
  interface NewsApi {
      @GET("v2/top-headlines")
      suspend fun getTopHeadlines(
          @Query("country") country: String,
          @Query("apiKey") apiKey: String
      ): NewsResponse
}

data class Article(val title: String?, val urlToImage: String?)
data class NewsResponse(val articles: List<Article>)

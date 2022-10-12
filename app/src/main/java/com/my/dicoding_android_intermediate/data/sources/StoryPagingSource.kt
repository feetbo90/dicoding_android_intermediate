package com.my.dicoding_android_intermediate.data.sources

class StoryPagingSource constructor(private val apiService: ApiService) : PagingSource<Int, QuoteResponseItem>() {

}
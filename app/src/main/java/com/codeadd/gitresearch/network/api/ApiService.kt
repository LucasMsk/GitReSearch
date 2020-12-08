package com.codeadd.gitresearch.network.api

import com.codeadd.gitresearch.model.SearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("search/repositories?page=all&")
    suspend fun getSearchList(@Query("q") searchString : String) : Response<SearchResponse>
}
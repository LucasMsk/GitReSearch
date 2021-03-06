package com.codeadd.gitresearch.network.api

import com.codeadd.gitresearch.model.CommitResponse
import com.codeadd.gitresearch.model.SearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/repositories?page=all&")
    suspend fun getSearchList(@Query("q") searchString : String, @Query("page") searchPage : String) : Response<SearchResponse>

    @GET("repos/{fullName}")
    suspend fun getCommitList(@Path("fullName", encoded = true) fullName: String) : Response<List<CommitResponse>>
}
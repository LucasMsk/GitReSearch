package com.codeadd.gitresearch.network.repository

import com.codeadd.gitresearch.model.SearchResponse
import com.codeadd.gitresearch.network.api.RetrofitObject
import com.codeadd.gitresearch.utils.Result

class SearchRepository {

    suspend fun searchRequest(searchString: String, searchPage: String): Result<SearchResponse> {
        return RetrofitObject.apiCall(call = { RetrofitObject.apiService.getSearchList(searchString, searchPage) })
    }
}
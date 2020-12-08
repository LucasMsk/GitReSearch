package com.codeadd.gitresearch.network.repository

import com.codeadd.gitresearch.model.CommitResponse
import com.codeadd.gitresearch.network.api.RetrofitObject
import com.codeadd.gitresearch.utils.Result

class DetailRepository {

    suspend fun commitRequest(fullName: String): Result<List<CommitResponse>> {
        return RetrofitObject.apiCall(call = { RetrofitObject.apiService.getCommitList(fullName+"/commits") })
    }
}

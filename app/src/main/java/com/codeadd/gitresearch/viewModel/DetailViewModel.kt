package com.codeadd.gitresearch.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codeadd.gitresearch.model.CommitResponse
import com.codeadd.gitresearch.network.repository.DetailRepository
import kotlinx.coroutines.launch
import com.codeadd.gitresearch.utils.Result

class DetailViewModel : ViewModel() {

    val detailRepository = DetailRepository()
    val commitList = MutableLiveData<List<CommitResponse>>()
    val errorMsg = MutableLiveData<String>()

    fun getCommitList(fullName: String) {
        viewModelScope.launch {
            when (val retrofitPost = detailRepository.commitRequest(fullName)) {
                is Result.Success -> {
                    commitList.postValue(retrofitPost.data.take(3))
                }
                is Result.Error -> {
                    errorMsg.postValue(retrofitPost.exception)
                }
            }
        }
    }
}
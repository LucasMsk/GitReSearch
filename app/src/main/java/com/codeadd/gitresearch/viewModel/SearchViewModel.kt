package com.codeadd.gitresearch.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codeadd.gitresearch.model.SearchResponse
import com.codeadd.gitresearch.network.repository.SearchRepository
import kotlinx.coroutines.launch
import com.codeadd.gitresearch.utils.Result

class SearchViewModel : ViewModel() {

    val searchRepository = SearchRepository()
    val repoList = MutableLiveData<SearchResponse>()
    val errorMsg = MutableLiveData<String>()

    fun getRepoList(searchString: String) {
        viewModelScope.launch {
            val retrofitPost = searchRepository.searchRequest(searchString)
            when (retrofitPost) {
                is Result.Success -> {
                    repoList.postValue(retrofitPost.data)
                }
                is Result.Error -> {
                    errorMsg.postValue(retrofitPost.exception)
                }
            }
        }
    }
}
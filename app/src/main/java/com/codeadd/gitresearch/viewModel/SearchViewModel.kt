package com.codeadd.gitresearch.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codeadd.gitresearch.model.SearchResponse
import com.codeadd.gitresearch.network.repository.SearchRepository
import kotlinx.coroutines.launch
import com.codeadd.gitresearch.utils.Result

class SearchViewModel : ViewModel() {

    val repoList = MutableLiveData<SearchResponse>()
    val errorMsg = MutableLiveData<String>()
    var lastSearch = MutableLiveData<String>()
    val isLoading = MutableLiveData(false)
    val isLastPage = MutableLiveData(false)
    val shouldScrollTop = MutableLiveData(false)
    private val searchRepository = SearchRepository()
    private val totalItemsPerQuery = 30
    private var paginatedRepoList: SearchResponse? = null
    private var searchPage = 1

    fun handlePagination(searchString: String, firstCall: Boolean) {
        if (firstCall) {
            isLastPage.postValue(false)
            searchPage = 1
            paginatedRepoList = null
            getRepoList(searchString)
        }
        else {
            if(repoList.value != null) {
                val totalPages = Math.ceil(repoList.value!!.total_count.toDouble()/totalItemsPerQuery).toInt()
                if (searchPage in 2..totalPages && !isLoading.value!!) {
                    getRepoList(searchString)
                    if(searchPage==totalPages)
                        isLastPage.postValue(true)
                }
                else
                    isLastPage.postValue(true)
            }
        }
    }
     fun getRepoList(searchString: String) {

        isLoading.postValue(true)
        viewModelScope.launch {
            when (val retrofitPost = searchRepository.searchRequest(searchString, searchPage.toString())) {
                is Result.Success -> {
                    isLoading.postValue(false)
                    if(paginatedRepoList == null) {
                        paginatedRepoList = retrofitPost.data
                        searchPage++
                    }
                    else {
                        if(paginatedRepoList != null && !retrofitPost.data.items.isNullOrEmpty()) {
                            searchPage++
                            paginatedRepoList!!.items.addAll(retrofitPost.data.items)
                        }
                    }
                    repoList.postValue(paginatedRepoList ?: retrofitPost.data)
                }
                is Result.Error -> {
                    isLoading.postValue(false)
                    errorMsg.postValue(retrofitPost.exception)
                }
            }
        }
    }
}
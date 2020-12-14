package com.codeadd.gitresearch

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.codeadd.gitresearch.viewModel.SearchViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.*
import kotlinx.coroutines.runBlocking

class SearchViewModelTest {

    @JvmField
    @Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    val viewModel = SearchViewModel()
    var searchString = ""

    @ExperimentalCoroutinesApi
    @Test
    fun `should return search response from API` () =
        runBlocking {
            searchString = "test"

            viewModel.handlePagination(searchString, true)
            delay(5000)

            assert(viewModel.repoList.value != null)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `should paginate response from next page` () =
        runBlocking {
            searchString = "test2"

            viewModel.handlePagination(searchString, true)
            delay(5000)
            viewModel.handlePagination(searchString, false)
            delay(5000)

            assertEquals(viewModel.repoList.value?.items?.size,60)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `should not paginate for response that doesn't have next page` () =
        runBlocking {
            searchString = "rtwetbwugtwygtgwyt4/trw/t3254q!@#"

            viewModel.handlePagination(searchString, true)
            delay(5000)
            viewModel.handlePagination(searchString, false)

            assertTrue(viewModel.isLastPage.value!!)
    }
}
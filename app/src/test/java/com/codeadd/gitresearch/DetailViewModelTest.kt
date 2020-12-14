package com.codeadd.gitresearch

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.codeadd.gitresearch.viewModel.DetailViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

class DetailViewModelTest {

    @JvmField
    @Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    val viewModel = DetailViewModel()

    @ExperimentalCoroutinesApi
    @Test
    fun `should return first 3 commits from API response` () =
        runBlocking {
            val fullName = "ruby/ruby"

            viewModel.getCommitList(fullName)
            delay(5000)

            assertEquals(viewModel.commitList.value?.size, 3)
    }
}
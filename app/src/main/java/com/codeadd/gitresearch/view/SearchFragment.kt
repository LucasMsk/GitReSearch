package com.codeadd.gitresearch.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.*
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.CheckResult
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import kotlinx.android.synthetic.main.search_fragment.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codeadd.gitresearch.R
import com.codeadd.gitresearch.adapter.SearchAdapter
import com.codeadd.gitresearch.utils.SoftKeyboard
import com.codeadd.gitresearch.viewModel.SearchViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*


class SearchFragment : Fragment() {

    companion object {
        fun newInstance() = SearchFragment()
    }

    private lateinit var viewModel: SearchViewModel
    private lateinit var searchAdapter: SearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.search_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        setupRecyclerView()
        setupObservers()
        setupListeners()
    }

    private fun setupRecyclerView() {
        recyclerView_search.layoutManager = LinearLayoutManager(context)
        searchAdapter = SearchAdapter(requireContext())
        recyclerView_search.adapter = searchAdapter
    }

    private fun setupObservers() {
        viewModel.repoList.observe(viewLifecycleOwner, Observer {
            searchAdapter.setRepoList(it.items)
        })
        viewModel.errorMsg.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            Toast.makeText(requireContext(),it, Toast.LENGTH_LONG).show()
        })

        viewModel.isLoading.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if(it) {
                progressBar_search.visibility = View.VISIBLE
            }
            else {
                progressBar_search.visibility = View.INVISIBLE
            }
        })

        viewModel.isLastPage.observe(viewLifecycleOwner, androidx.lifecycle.Observer {

            if(it) recyclerView_search.setPadding(0,0,0,0)
            else recyclerView_search.setPadding(0,0,0,52)
        })
    }


    @FlowPreview
    @ExperimentalCoroutinesApi
    private fun setupListeners() {
        //Live text change search. Using Flow eliminates need to include rxjava package for debounce feature
        txt_searchBar.textChanged()
                .distinctUntilChanged()
                .filterNot { it.isBlank() }
                .debounce(500)
                .distinctUntilChanged()
                .onEach {
                    //Do not search again going back from DetailFragment
                    if(viewModel.lastSearch.value != it) {
                    //Eliminate case e.g. we search "test" go to repo details go back and search for "tester" than search again for "test"
                        viewModel.lastSearch.value = ""
                        viewModel.handlePagination(it,true)
                    }}
                .launchIn(lifecycleScope)

        //Hide keyboard when click/scroll on recycler view
        recyclerView_search.setOnTouchListener { v, event ->
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> SoftKeyboard.hide(requireActivity(), requireView())
                MotionEvent.ACTION_UP -> SoftKeyboard.hide(requireActivity(), requireView())
            }
            v.performClick()
            v?.onTouchEvent(event) ?: true
        }
        //Hide when touched outside of search edit text
        fragment_search.setOnClickListener { v -> SoftKeyboard.hide(requireActivity(),v) }

        //Paginate request listener
        var scrolling = false
        val scrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val firstItem = layoutManager.findFirstVisibleItemPosition()
                val visibleItems = layoutManager.childCount
                val totalItems = layoutManager.itemCount
                val lastItem = firstItem + visibleItems >= totalItems
                if(lastItem && firstItem > 0 && scrolling && !txt_searchBar.text.isNullOrBlank()) {
                    viewModel.handlePagination(txt_searchBar.text.toString(), false)
                    scrolling = false
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                scrolling = true
            }
        }
        recyclerView_search.addOnScrollListener(scrollListener)
    }

    //Using Flow for debounce feature
    @ExperimentalCoroutinesApi
    @CheckResult
    fun EditText.textChanged(): Flow<String> = channelFlow {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                offer(p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        }
        addTextChangedListener(textWatcher)
        awaitClose {
            removeTextChangedListener(textWatcher)
        }
    }

    override fun onDestroyView() {
        //Do not search again going back from DetailFragment
        viewModel.lastSearch.value = txt_searchBar.text.toString()
        super.onDestroyView()
    }
}
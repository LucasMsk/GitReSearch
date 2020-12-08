package com.codeadd.gitresearch.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.*
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.search_fragment.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.codeadd.gitresearch.R
import com.codeadd.gitresearch.adapter.SearchAdapter
import com.codeadd.gitresearch.viewModel.SearchViewModel

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
    }

    private fun setupRecyclerView() {
        recyclerView_search.layoutManager = LinearLayoutManager(context)
        searchAdapter = SearchAdapter(requireContext())
        recyclerView_search.adapter = searchAdapter
    }

}
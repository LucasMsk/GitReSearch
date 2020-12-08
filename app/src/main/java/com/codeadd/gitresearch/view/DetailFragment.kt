package com.codeadd.gitresearch.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.codeadd.gitresearch.viewModel.DetailViewModel
import com.codeadd.gitresearch.R
import com.codeadd.gitresearch.utils.SoftKeyboard
import kotlinx.android.synthetic.main.detail_fragment.*

class DetailFragment : Fragment() {

    companion object {
        fun newInstance() = DetailFragment()
    }

    private lateinit var viewModel: DetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.detail_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
        SoftKeyboard.hide(requireActivity())
        setupListeners()
    }

    private fun setupListeners() {
        btn_back_fragment.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.actionTo_searchFragment)
        }
    }

}
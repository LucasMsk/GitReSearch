package com.codeadd.gitresearch.view

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.app.ShareCompat
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.codeadd.gitresearch.viewModel.DetailViewModel
import com.codeadd.gitresearch.R
import com.codeadd.gitresearch.model.Detail
import com.codeadd.gitresearch.utils.SoftKeyboard
import kotlinx.android.synthetic.main.detail_fragment.*
import kotlinx.android.synthetic.main.detail_fragment.view.*

class DetailFragment : Fragment() {

    companion object {
        fun newInstance() = DetailFragment()
    }

    private lateinit var viewModel: DetailViewModel
    private val args: DetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.window?.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        return inflater.inflate(R.layout.detail_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
        SoftKeyboard.hide(requireActivity())
        setupListeners(args.detail.repoUrl)
        populateViews(requireView(), args.detail)
    }

    private fun populateViews(view: View, detail: Detail) {
        val nameArr = detail.fullName.split("/")
        if(nameArr.size == 2) {
            view.txt_details_author.text = nameArr[0]
            view.txt_details_repo_title.text = nameArr[1] }
        else {
            view.txt_details_author.text = detail.fullName
            view.txt_details_repo_title.text = detail.fullName
        }
        view.txt_details_stars.text = detail.starCount.toString()
        Glide.with(requireContext())
            .load(detail.avatarUrl)
            .into(view.img_details_avatar)
    }


    private fun setupListeners(url: String) {
        btn_back_fragment.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.actionTo_searchFragment)
        }
        btn_view_online.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
        btn_share_repo.setOnClickListener {
            ShareCompat.IntentBuilder.from(requireActivity())
                    .setType("text/plain")
                    .setChooserTitle("Share Repo")
                    .setText(url)
                    .startChooser();
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }
}
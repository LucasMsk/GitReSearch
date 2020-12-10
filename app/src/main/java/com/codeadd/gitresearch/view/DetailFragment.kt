package com.codeadd.gitresearch.view

import android.content.Intent
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codeadd.gitresearch.viewModel.DetailViewModel
import com.codeadd.gitresearch.R
import com.codeadd.gitresearch.adapter.CommitAdapter
import com.codeadd.gitresearch.model.Detail
import com.codeadd.gitresearch.utils.GlideApp
import com.codeadd.gitresearch.utils.SoftKeyboard
import kotlinx.android.synthetic.main.detail_fragment.*
import kotlinx.android.synthetic.main.detail_fragment.view.*

class DetailFragment : Fragment() {

    companion object {
        fun newInstance() = DetailFragment()
    }

    private lateinit var viewModel: DetailViewModel
    private lateinit var commitAdapter: CommitAdapter
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
        val detail = args.detail
        setupListeners(detail.repoUrl)
        populateViews(requireView(), detail)
        setupRecyclerView()
        setupObservers()
        viewModel.getCommitList(detail.fullName)
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
        view.txt_details_stars.text = getString(R.string.number_stars,detail.starCount)
        GlideApp.with(requireContext())
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

    private fun setupRecyclerView() {
        recyclerView_commit.layoutManager = LinearLayoutManager(context)
        recyclerView_commit.addItemDecoration(DividerItemDecorator(ContextCompat.getDrawable(requireContext(),R.drawable.divider)))
        commitAdapter = CommitAdapter(requireContext())
        recyclerView_commit.adapter = commitAdapter
    }

    private fun setupObservers() {
        viewModel.commitList.observe(viewLifecycleOwner, Observer {
            commitAdapter.setCommitList(it)
        })
        viewModel.errorMsg.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            Toast.makeText(requireContext(),it, Toast.LENGTH_LONG).show()
        })
    }

    //Remove last divider
    class DividerItemDecorator(private val divider: Drawable?) : RecyclerView.ItemDecoration() {

        override fun onDrawOver(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
            val dividerLeft = parent.paddingLeft
            val dividerRight = parent.width - parent.paddingRight
            val childCount = parent.childCount
            for (i in 0..childCount - 2) {
                val child: View = parent.getChildAt(i)
                val params =
                    child.layoutParams as RecyclerView.LayoutParams
                val dividerTop: Int = child.bottom + params.bottomMargin
                val dividerBottom = dividerTop + (divider?.intrinsicHeight?:0)
                divider?.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom)
                divider?.draw(canvas)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }
}
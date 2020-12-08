package com.codeadd.gitresearch.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.codeadd.gitresearch.R
import com.codeadd.gitresearch.model.Detail
import com.codeadd.gitresearch.model.Repo
import com.codeadd.gitresearch.view.SearchFragmentDirections.actionToDetailFragment
import kotlinx.android.synthetic.main.rv_search_item.view.*

class SearchAdapter(private val context: Context) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    private var repoList: List<Repo> = ArrayList()

    fun setRepoList(list: List<Repo>) {
        this.repoList = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val repoName = view.txt_repo_name
        val starsCount = view.txt_stars_count
        val avatar = view.img_avatar
        init {
            view.setOnClickListener{
                val action = actionToDetailFragment(
                    Detail(repoList[adapterPosition].full_name,
                        repoList[adapterPosition].owner.avatar_url,
                        repoList[adapterPosition].html_url,
                        repoList[adapterPosition].stargazers_count)
                )
                Navigation.findNavController(view).navigate(action)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.rv_search_item,parent,false))
    }

    override fun onBindViewHolder(holder: SearchAdapter.ViewHolder, position: Int) {
        holder.repoName.text = repoList[position].name
        holder.starsCount.text = repoList[position].stargazers_count.toString()
        Glide.with(context)
            .load(repoList[position].owner.avatar_url)
            .placeholder(R.drawable.shape_round)
            .transform(RoundedCorners(8))
            .override(60,60)
            .into(holder.avatar)
    }

    override fun getItemCount(): Int {
        return repoList.size
    }

}
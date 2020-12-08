package com.codeadd.gitresearch.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.codeadd.gitresearch.R
import com.codeadd.gitresearch.model.CommitResponse
import kotlinx.android.synthetic.main.rv_commit_item.view.*

class CommitAdapter(private val context: Context) : RecyclerView.Adapter<CommitAdapter.ViewHolder>()  {

    private var commitList: List<CommitResponse> = ArrayList()

    fun setCommitList(list: List<CommitResponse>) {
        this.commitList = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val authorName = view.txt_commit_authorName
        val authorEmail = view.txt_commit_authorEmail
        val commitMsg = view.txt_commit_msg
        val commitNumber = view.btn_commit_number
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommitAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.rv_commit_item,parent,false))
    }

    override fun getItemCount(): Int {
        return commitList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.authorName.text = commitList[position].commit.author.name
        holder.authorEmail.text = commitList[position].commit.author.email
        holder.commitMsg.text = commitList[position].commit.message.trim()
        holder.commitNumber.text = (position+1).toString()
    }
}

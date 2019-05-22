package com.example.mvvm.ui.pullrequests

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvm.databinding.PullRequestsListItemBinding
import com.example.mvvm.model.PullRequest

class PullRequestsAdapter(private val onPullRequestsClickedListener: OnPullRequestsClickedListener) :
    RecyclerView.Adapter<PullRequestsAdapter.PullRequestsViewHolder>() {

    private val pullRequestList: MutableList<PullRequest> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PullRequestsViewHolder {
        return PullRequestsViewHolder(
            PullRequestsListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = pullRequestList.size

    override fun onBindViewHolder(holder: PullRequestsViewHolder, position: Int) {
        holder.bind(pullRequestList[position])
    }

    fun addPullRequests(pullRequestList: List<PullRequest>) {
        this.pullRequestList.addAll(pullRequestList)
        notifyDataSetChanged()
    }

    fun clearPullRequests() {
        this.pullRequestList.clear()
        notifyDataSetChanged()
    }

    inner class PullRequestsViewHolder(private val binding: PullRequestsListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(pullRequest: PullRequest) {
            binding.apply {
                setPullRequest(pullRequest)
                onPullRequestsClickedListener = this@PullRequestsAdapter.onPullRequestsClickedListener
                executePendingBindings()
            }
        }
    }

    interface OnPullRequestsClickedListener {
        fun onPullRequestsClicked(pullRequest: PullRequest)
    }

}
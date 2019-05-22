package com.example.mvvm.ui.pullrequests


import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.mvvm.R
import com.example.mvvm.base.BaseFragment
import com.example.mvvm.custom.LoadingDialog
import com.example.mvvm.databinding.FragmentPullRequestsBinding
import com.example.mvvm.model.PullRequest
import com.example.mvvm.model.Response
import com.example.mvvm.ui.NavigationActivity
import com.example.mvvm.utils.EndlessRecyclerOnScrollListener
import com.example.mvvm.utils.SingleEventObserver
import com.example.mvvm.utils.getErrorMessage
import com.example.mvvm.utils.toast
import kotlinx.android.synthetic.main.fragment_pull_requests.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class PullRequestsFragment : BaseFragment<FragmentPullRequestsBinding>(),
    PullRequestsAdapter.OnPullRequestsClickedListener {

    private val viewModel by viewModel<PullRequestsViewModel>()
    private val pullRequestsAdapter by lazy { PullRequestsAdapter(this) }
    private val loadingDialog by lazy {
        LoadingDialog(requireActivity()).apply {
            lifecycle.addObserver(this)
        }
    }

    private val endlessRecyclerOnScrollListener by lazy {
        object : EndlessRecyclerOnScrollListener() {
            override fun onLoadMore(current_page: Int) {
                viewModel.getPullRequests(current_page)
            }
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_pull_requests

    override fun onPullRequestsClicked(pullRequest: PullRequest) {
        if (findNavController().currentDestination?.id == R.id.pullRequestsFragment) {
            findNavController().navigate(
                PullRequestsFragmentDirections.actionPullRequestsFragmentToWebViewFragment(
                    pullRequest.html_url
                )
            )
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        getDataBinding().pullRequestsAdapter = pullRequestsAdapter
        getDataBinding().viewModel = viewModel

        viewModel.clearPullRequestsLiveData.observe(this, SingleEventObserver { status ->
            if (status) {
                pullRequestsAdapter.clearPullRequests()
                endlessRecyclerOnScrollListener.reset()
            }
        })

        viewModel.pullRequestsLiveData.observe(this, Observer { response ->

            when (response) {
                is Response.Loading -> {
                    if (!pullRequestsRefresh.isRefreshing) {
                        loadingDialog.toggle(response.status)
                    }
                }
                is Response.Success -> {
                    pullRequestsAdapter.addPullRequests(response.data)
                    pullRequestsRefresh.isRefreshing = false
                    loadingDialog.toggle(false)
                }

                is Response.Error -> {
                    response.throwable?.getErrorMessage(requireContext())?.toast(requireContext())
                    response.data?.let { pullRequestsList ->
                        pullRequestsAdapter.addPullRequests(pullRequestsList)
                    }
                    pullRequestsRefresh.isRefreshing = false
                    loadingDialog.toggle(false)
                }

            }

        })

        viewModel.changeRepositoryLiveData.observe(this, SingleEventObserver { response ->

            when (response) {
                is Response.Loading -> {
                    loadingDialog.toggle(response.status)
                }
                is Response.Success -> {
                    loadingDialog.toggle(false)
                    if (response.data) {
                        findNavController().navigate(R.id.action_pullRequestsFragment_to_repoFragment)
                    }
                }

                is Response.Error -> {
                    response.throwable?.getErrorMessage(requireContext())?.toast(requireContext())
                    loadingDialog.toggle(false)
                }

            }


        })

        viewModel.repositoryNameLiveData.observe(viewLifecycleOwner, Observer { repoName ->
            (requireActivity() as NavigationActivity).supportActionBar?.apply {
                show()
                title = repoName
            }
        })

        pullRequestsRecyclerView.addOnScrollListener(endlessRecyclerOnScrollListener)

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_pull_requests, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.actionChangeRepository)
            viewModel.changeRepository()
        return super.onOptionsItemSelected(item)
    }
}

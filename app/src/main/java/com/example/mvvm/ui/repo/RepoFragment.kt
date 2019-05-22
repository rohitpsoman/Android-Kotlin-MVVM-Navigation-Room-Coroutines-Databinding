package com.example.mvvm.ui.repo


import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.mvvm.R
import com.example.mvvm.base.BaseFragment
import com.example.mvvm.custom.LoadingDialog
import com.example.mvvm.databinding.FragmentRepoBinding
import com.example.mvvm.model.Response
import com.example.mvvm.ui.NavigationActivity
import com.example.mvvm.utils.getErrorMessage
import com.example.mvvm.utils.toast
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.HttpException


class RepoFragment : BaseFragment<FragmentRepoBinding>() {

    private val viewModel by viewModel<RepoViewModel>()
    private val loadingDialog by lazy {
        LoadingDialog(requireActivity()).apply {
            lifecycle.addObserver(this)
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_repo

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (requireActivity() as NavigationActivity).supportActionBar?.hide()
        getDataBinding().viewModel = viewModel

        viewModel.responseLiveData.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Response.Loading -> loadingDialog.toggle(response.status)
                is Response.Error -> {
                    if ((response.throwable as? HttpException)?.code() == 404) {
                        getString(R.string.repo_found_error).toast(requireContext())
                    } else {
                        response.throwable?.getErrorMessage(requireContext())?.toast(requireContext())
                    }
                    loadingDialog.toggle(false)
                }
                is Response.Success -> {
                    findNavController().navigate(R.id.action_repoFragment_to_pullRequestsFragment)
                    loadingDialog.toggle(false)
                }
            }
        })
    }
}

package com.example.mvvm.ui.splash


import android.os.Bundle
import androidx.navigation.fragment.findNavController
import com.example.mvvm.R
import com.example.mvvm.base.BaseFragment
import com.example.mvvm.databinding.FragmentSplashBinding
import com.example.mvvm.utils.SingleEventObserver
import org.koin.androidx.viewmodel.ext.android.viewModel


class SplashFragment : BaseFragment<FragmentSplashBinding>() {

    private val splashViewModel by viewModel<SplashViewModel>()

    override fun getLayoutId(): Int = R.layout.fragment_splash

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        splashViewModel.repoSelectedLiveData.observe(this, SingleEventObserver { status ->
            if (!status)
                findNavController().navigate(R.id.action_splashFragment_to_repoFragment)
            else
                findNavController().navigate(R.id.action_splashFragment_to_pullRequestsFragment)
        })
    }
}

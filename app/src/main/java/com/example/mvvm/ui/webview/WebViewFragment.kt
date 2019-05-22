package com.example.mvvm.ui.webview


import android.os.Bundle
import com.example.mvvm.R
import com.example.mvvm.base.BaseFragment
import com.example.mvvm.custom.LoadingDialog
import com.example.mvvm.databinding.FragmentWebViewBinding
import com.example.mvvm.utils.doOnProgressCompleted
import kotlinx.android.synthetic.main.fragment_web_view.*

class WebViewFragment : BaseFragment<FragmentWebViewBinding>() {

    private val loadingDialog by lazy {
        LoadingDialog(requireActivity()).apply {
            setCancelable(false)
            lifecycle.addObserver(this)
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_web_view

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        arguments?.let { bundle ->
            getDataBinding().url = WebViewFragmentArgs.fromBundle(bundle).url
        }

        loadingDialog.toggle(true)
        webView.doOnProgressCompleted { loadingDialog.toggle(false) }
    }
}

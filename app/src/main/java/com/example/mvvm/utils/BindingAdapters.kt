package com.example.mvvm.utils

import android.content.res.Resources.NotFoundException
import android.webkit.WebView
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.mvvm.R
import com.example.mvvm.ui.pullrequests.PullRequestsAdapter
import com.squareup.picasso.Picasso

/**
 * An adapter to bind errors to EditText
 */

@BindingAdapter("error")
fun EditText.setError(error: Int) {
    try {
        setError(context.getString(error))
        requestFocus()
    } catch (e: NotFoundException) {
        setError(null)
        clearFocus()
    }

}

@BindingAdapter("pullRequestsAdapter")
fun RecyclerView.setPullRequestsAdapter(adapter: PullRequestsAdapter?) {
    layoutManager = LinearLayoutManager(context)
    setAdapter(adapter)
}

@BindingAdapter("onRefresh")
inline fun SwipeRefreshLayout.setOnRefreshListener(crossinline onRefresh: () -> Unit) {
    setOnRefreshListener { onRefresh() }
}

@BindingAdapter("imageUrl")
fun ImageView.setImageByUrl(imageUrl: String?) {
    Picasso.get()
        .load(imageUrl).fit()
        .placeholder(R.drawable.ic_avatar)
        .error(R.drawable.ic_avatar)
        .into(this)
}

@BindingAdapter("requestState")
fun TextView.setState(requestState: String) {
    text = requestState
    ViewCompat.setBackground(
        this,
        if (requestState == context.getString(R.string.open))
            ContextCompat.getDrawable(context, R.drawable.red_rect_filled)
        else ContextCompat.getDrawable(context, R.drawable.green_rect_filled)
    )
}

@BindingAdapter(value = ["requestState", "requestNumber", "createdDate", "updatedDate"])
fun TextView.setRequestDate(
    requestState: String, requestNumber: Long,
    createdDate: String, updatedDate: String
) {
    text = if (requestState == context.getString(R.string.open)) {
        "#$requestNumber opened on ${createdDate.getFormattedDate()}"
    } else {
        "#$requestNumber was merged on ${updatedDate.getFormattedDate()}"
    }
}

@BindingAdapter("url")
fun WebView.loadRepoUrl(url: String) {
    run { loadUrl(url) }
}
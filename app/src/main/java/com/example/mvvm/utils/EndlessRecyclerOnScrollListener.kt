package com.example.mvvm.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Set pagination for RecyclerView
 */
abstract class EndlessRecyclerOnScrollListener(
    var visibleThreshold: Int = VISIBLE_THRESH_HOLD, var startPage: Int = START_PAGE
) : RecyclerView.OnScrollListener() {

    companion object {
        const val VISIBLE_THRESH_HOLD = 5
        const val START_PAGE = 1
    }

    private var previousTotal = 0 // The total number of items in the dataset after the last load
    private var loading = true // True if we are still waiting for the last set of data to load.
    private var firstVisibleItem: Int = 0
    private var visibleItemCount: Int = 0
    private var totalItemCount: Int = 0


    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        visibleItemCount = recyclerView.childCount
        (recyclerView.layoutManager as? LinearLayoutManager)?.let {
            totalItemCount = it.itemCount
            firstVisibleItem = it.findFirstVisibleItemPosition()
        }

        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false
                previousTotal = totalItemCount
            }
        }

        if (isLoadingRequired()) {
            // End has been reached
            startPage++
            onLoadMore(startPage)
            loading = true
        }
    }

    private fun isLoadingRequired() =
        (!loading && totalItemCount - visibleItemCount <= firstVisibleItem + visibleThreshold)

    abstract fun onLoadMore(current_page: Int)

    fun reset() {
        previousTotal = 0
        loading = true
        startPage = START_PAGE
    }
}

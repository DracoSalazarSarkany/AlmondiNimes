package com.example.almondinimes.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class EndlessScrollListener(
    private val layoutManager: LinearLayoutManager,
    private val loadMore: () -> Unit
) : RecyclerView.OnScrollListener() {

    private var isLoading = false

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        if (dy <= 0) return // No scroll o scroll hacia arriba

        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

        if (!isLoading) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount) {
                isLoading = true
                loadMore()
            }
        }
    }

    fun setLoaded() {
        isLoading = false
    }
}
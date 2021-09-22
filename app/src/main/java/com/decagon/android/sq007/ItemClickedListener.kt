package com.decagon.android.sq007

interface ItemClickedListener {
    fun onRecyclerViewItemClicked(
        id: String?,
        name: String?,
        phoneNumber: String?
    )
}
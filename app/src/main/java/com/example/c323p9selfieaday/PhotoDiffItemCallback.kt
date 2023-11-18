package com.example.c323p9selfieaday

import androidx.recyclerview.widget.DiffUtil

class PhotoDiffItemCallback: DiffUtil.ItemCallback<Photo>() {
    /*
    methods to detect whether two photos are the same or have the same contents
     */
    override fun areItemsTheSame(oldItem: Photo, newItem: Photo)
            = (oldItem.creationTimeMs == newItem.creationTimeMs)
    override fun areContentsTheSame(oldItem: Photo, newItem: Photo)
            = (oldItem == newItem)
}
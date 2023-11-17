package com.example.c323p9selfieaday

import androidx.recyclerview.widget.DiffUtil

class PhotoDiffItemCallback: DiffUtil.ItemCallback<Photo>() {
    override fun areItemsTheSame(oldItem: Photo, newItem: Photo)
            = (oldItem.creationTimeMs == newItem.creationTimeMs)
    override fun areContentsTheSame(oldItem: Photo, newItem: Photo)
            = (oldItem == newItem)
}
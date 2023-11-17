package com.example.c323p9selfieaday

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.c323p9selfieaday.databinding.PhotoItemBinding

class PhotosAdapter(val context: Context, //val clickListener:(photo:Photo)->Unit):
):
ListAdapter<Photo, PhotosAdapter.PhotoItemViewHolder>(PhotoDiffItemCallback()){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PhotoItemViewHolder = PhotoItemViewHolder.inflateFrom(parent)

    override fun onBindViewHolder(holder: PhotoItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, context)//, clickListener)
    }
    class PhotoItemViewHolder(val binding: PhotoItemBinding)
        :RecyclerView.ViewHolder(binding.root){
            companion object{
                fun inflateFrom(parent:ViewGroup): PhotoItemViewHolder{
                    val layoutInflater = LayoutInflater.from(parent.context)
                    val binding = PhotoItemBinding.inflate(layoutInflater, parent, false)
                    return PhotoItemViewHolder(binding)
                }
            }
        fun bind(photo:Photo, context:Context){//}, clickListener:(photo:Photo)->Unit){
            val username = photo.user?.email as String
            Glide.with(context)
                .load(photo.imageUrl)
                .apply(RequestOptions.overrideOf(400,400))
                .into(binding.ivPhoto)
            //binding.photo = photo
            //binding.root.setOnClickListener { clickListener(photo) }
        }
        }
}
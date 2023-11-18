package com.example.c323p9selfieaday

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.c323p9selfieaday.databinding.FragmentPhotoZoomBinding

class PhotoZoomFragment :Fragment(){
    //variables
    private var _binding:FragmentPhotoZoomBinding?=null
    private val binding get() = _binding!!
    val viewModel:PhotosViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPhotoZoomBinding.inflate(inflater, container, false)
        val view = binding.root

        //creates photo view using Glide
        val photoUrl = PhotoZoomFragmentArgs.fromBundle(requireArguments()).imageUrl
        Glide.with(requireContext())
            .load(photoUrl)
            .into(binding.zoomImage)
        viewModel.imageUrl = photoUrl
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.onPhotoNav()

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.example.c323p9selfieaday

import android.hardware.SensorManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.c323p9selfieaday.databinding.FragmentPhotosBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class PhotosFragment : Fragment() {
    private var uri: Uri? = null
    val TAG = "PhotosFragment"

    private var _binding:FragmentPhotosBinding? = null
    private val binding get() = _binding!!

    private lateinit var sensorManager: SensorManager

    private lateinit var firestoreDb:FirebaseFirestore
    private lateinit var storageRef:StorageReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPhotosBinding.inflate(inflater, container, false)
        val view = binding.root
        val viewModel:PhotosViewModel by activityViewModels()
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner


        viewModel.initializeSensors(AccelerometerSensor(this.requireContext()))
        viewModel.shake.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it) {
                    view.findNavController()
                        .navigate(R.id.action_photosFragment_to_takePhotoFragment)
                }
                viewModel.onShaked()
            }
        })
        fun photoClick(photo: Photo){
            viewModel.onPhotoClick(photo)
        }

        viewModel.navToPhoto.observe(viewLifecycleOwner, Observer { url->
            url?.let {
                val action = PhotosFragmentDirections
                    .actionPhotosFragmentToPhotoZoomFragment(url)
                this.findNavController().navigate(action)
                viewModel.onPhotoNav()
            }
        })


        firestoreDb = FirebaseFirestore.getInstance()
        storageRef = FirebaseStorage.getInstance().reference

        //val adapter = PhotosAdapter(this.requireContext())//, ::photoClick)
        //binding.rvPhotos.adapter = adapter

        viewModel.photos.observe(viewLifecycleOwner, Observer{photos->
            val adapter = PhotosAdapter(requireContext())//, ::photoClick)
            adapter.submitList(photos)
            binding.rvPhotos.adapter = adapter
        })
        viewModel.getPhotoUrls()

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
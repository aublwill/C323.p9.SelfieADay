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
    //photos variables
    private var _binding:FragmentPhotosBinding? = null
    private val binding get() = _binding!!

    //database and storage
    private lateinit var firestoreDb:FirebaseFirestore
    private lateinit var storageRef:StorageReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //binding and view
        _binding = FragmentPhotosBinding.inflate(inflater, container, false)
        val view = binding.root
        val viewModel:PhotosViewModel by activityViewModels()
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.getPhotoUrls()
        viewModel.onNavigatedToPhotos()//////////////////////////////////////////////

        //initialize accelerometer sensors
        viewModel.initializeSensors(AccelerometerSensor(this.requireContext()))
        //if shake is detected, navigate to take photo fragment (camera)
        viewModel.shake.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it) {
                    view.findNavController()
                        .navigate(R.id.action_photosFragment_to_takePhotoFragment)
                }
            }
        })
        /*
        @param photo:Photo that was clicked
         */
        fun photoClick(photo: Photo){
            viewModel.onPhotoClick(photo)
        }

        //navigates to selected photo(photo zoom fragment)
        //this blows the selected photo into full view/full screen
        viewModel.navToPhoto.observe(viewLifecycleOwner, Observer { url->
            url?.let {
                val action = PhotosFragmentDirections
                    .actionPhotosFragmentToPhotoZoomFragment(url)
                this.findNavController().navigate(action)
                viewModel.onPhotoNav()
            }
        })

        //database/storage
        firestoreDb = FirebaseFirestore.getInstance()
        storageRef = FirebaseStorage.getInstance().reference

        //update photos/recycler view when new photo is added
        viewModel.photos.observe(viewLifecycleOwner, Observer{photos->
            val adapter = PhotosAdapter(requireContext(), ::photoClick)
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
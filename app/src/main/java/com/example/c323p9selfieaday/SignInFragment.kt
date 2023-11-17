package com.example.c323p9selfieaday

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.example.c323p9selfieaday.databinding.FragmentSignInBinding

class SignInFragment : Fragment(){
    val TAG = "SignInFragment"
    //binding variables
    private var _binding : FragmentSignInBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //variables
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        val view = binding.root
        val viewModel : PhotosViewModel by activityViewModels()
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        //navigate back to home screen
        viewModel.navigateToList.observe(viewLifecycleOwner, Observer { navigate->
            if (navigate) {
                view.findNavController()
                    .navigate(R.id.action_signInFragment_to_photosFragment)
                viewModel.onNavigatedToList()
            }
        })
        //navigate to sign up
        viewModel.navigateToSignUp.observe(viewLifecycleOwner, Observer {navigate->
            if (navigate){
                view.findNavController()
                    .navigate(R.id.action_signInFragment_to_signUpFragment)
                viewModel.onNavigatedToSignUp()
            }
        })
        //creates toast message if an error happened
        viewModel.errorHappened.observe(viewLifecycleOwner, Observer {error->
            error?.let{
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
            }
        })
        return view
    }
}
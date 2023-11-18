# C323.p9.SelfieADay
This project functions as a camera/photos app.

## Functionality 
The following **required** functionality is completed:
* [] user is promted to login or signup using email/password combination
* [] user sees a screen with (maybe) photos on it
* [] if there is a photo, user can click on it to view full screen
* [] user can use the back button to exit photo zoom and go back to photos
* [] user can shake the phone, taking them to a camera view
* [] user is prompted with permission requests to use the camera
* [] if accepted, user can take a photo using the 'take photo' button
* [] once a photo is taken, user is taken back to the photo screen

The folowing **extensions** are implemented:

* import android.content.Context
* import android.content.pm.PackageManager
* import android.hardware.Sensor
* import android.hardware.SensorEvent
* import android.hardware.SensorEventListener
* import android.hardware.SensorManager
* import androidx.appcompat.app.AppCompatActivity
* import android.os.Bundle
* import com.google.firebase.firestore.PropertyName
* import androidx.recyclerview.widget.DiffUtil
* import android.view.LayoutInflater
* import android.view.ViewGroup
* import androidx.recyclerview.widget.ListAdapter
* import androidx.recyclerview.widget.RecyclerView
* import com.bumptech.glide.Glide
* import com.bumptech.glide.request.RequestOptions
* import com.example.c323p9selfieaday.databinding.PhotoItemBinding
* import android.view.View
* import androidx.fragment.app.Fragment
* import androidx.fragment.app.activityViewModels
* import androidx.lifecycle.Observer
* import androidx.navigation.findNavController
* import androidx.navigation.fragment.findNavController
* import com.example.c323p9selfieaday.databinding.FragmentPhotosBinding
* import com.google.firebase.firestore.FirebaseFirestore
* import com.google.firebase.storage.FirebaseStorage
* import com.google.firebase.storage.StorageReference
* import android.util.Log
* import androidx.lifecycle.LiveData
* import androidx.lifecycle.MutableLiveData
* import com.google.firebase.auth.FirebaseAuth
* import com.google.firebase.auth.ktx.auth
* import com.google.firebase.firestore.FirebaseFirestore
* import com.google.firebase.ktx.Firebase
* import kotlin.math.sqrt
* import androidx.camera.core.CameraSelector
* import androidx.camera.core.ImageCapture
* import androidx.camera.core.ImageCaptureException
* import androidx.camera.core.Preview
* import androidx.camera.lifecycle.ProcessCameraProvider
  
## Video Walkthrough 




https://github.com/aublwill/C323.p9.SelfieADay/assets/143005409/3d2dd70f-5e9c-4e9c-8d5c-33ce82c1c4c7



## Notes
* For new photo to show up, screen must be reloaded (by clicking a photo and exiting, or taking another photo)

## License
Copyright [2023] [Aubrey Williams]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

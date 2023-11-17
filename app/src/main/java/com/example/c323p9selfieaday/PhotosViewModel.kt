package com.example.c323p9selfieaday

import android.hardware.SensorManager
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObjects
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlin.math.sqrt

class PhotosViewModel:ViewModel() {
    val TAG = "PhotosViewModel"
    var signedInUser:User? = null
    var photo = MutableLiveData<Photo>()
    private val _photos:MutableLiveData<MutableList<Photo>> = MutableLiveData()
    val photos:LiveData<List<Photo>>
        get() = _photos as LiveData<List<Photo>>

    private lateinit var accSensor: MeasurableSensor
    private var accData = floatArrayOf(
        SensorManager.GRAVITY_EARTH, SensorManager.GRAVITY_EARTH, 0.0F
    )
    private var _shake:MutableLiveData<Boolean> = MutableLiveData(false)
    val shake:LiveData<Boolean>
        get() = _shake
    fun onShaked() {
        _shake.value = false
    }

    fun initializeSensors(sAcc:MeasurableSensor){
        accSensor = sAcc
        accSensor.startListening()
        accSensor.setOnSensorValuesChangedListener { a->
            val x:Float = a[0]
            val y:Float = a[1]
            val z:Float = a[2]
            accData[1] = accData[0]
            accData[0] = sqrt((x*x).toDouble()+y*y+z*z).toFloat()
            val delta:Float = accData[0] - accData[1]
            accData[2] = accData[2]*0.9f + delta
            if (accData[2] > 8) {
                _shake.value = true
                Log.i(TAG, "${accData[0]}, ${accData[1]}, ${accData[2]}")
                Log.i(TAG, "Shake detected!")
            }
        }
    }

    val firestoreDb = FirebaseFirestore.getInstance()
    val storageRef = FirebaseStorage.getInstance().reference
    fun getPhotoUrls(){
        firestoreDb.collection("photos")
            .get()
            .addOnSuccessListener { documents->
                val photos = mutableListOf<Photo>()
                for (document in documents){
                    val imageUrl = document.getString("imageUrl")
                    val creationTimeMs = document.getLong("creation_time_ms")?:0
                    val userMap = document.get("user") as? Map<*,*>
                    val userEmail = userMap?.get("email")as? String?:""
                    val user = User(email = userEmail)
                    imageUrl?.let {
                        val photo = Photo(imageUrl = it, creationTimeMs = creationTimeMs, user= user)
                        photos.add(photo)
                    }
                }
                _photos.value = photos
            }
    }

    /*
    init {
        val firebaseStorage = FirebaseFirestore.getInstance()
        //upload to cloud storage
        var photosRef = firebaseStorage
            .collection("photos")
            .limit(30)
            .orderBy("creation_time_ms", Query.Direction.DESCENDING)
        photosRef.addSnapshotListener{snapshot, e ->
            if (e !=null || snapshot==null){
                Log.e(TAG, e.toString())
                return@addSnapshotListener
            }
            val photosList = snapshot.toObjects<Photo>()
            _photos.value = photosList as MutableList<Photo>
            for (photo in photosList)
                Log.i(TAG, "Photo ${photo}")
        }

    }

     */
    fun signOut(){
        FirebaseAuth.getInstance().signOut()
        signedInUser = null
    }
    //error
    private val _errorHappened = MutableLiveData<String?>()
    val errorHappened: LiveData<String?>
        get() = _errorHappened

    //navigation for list
    private val _navigateToList = MutableLiveData<Boolean>(false)
    val navigateToList: LiveData<Boolean>
        get() = _navigateToList

    //navigation for sign up
    private val _navigateToSignUp = MutableLiveData<Boolean>(false)
    val navigateToSignUp: LiveData<Boolean>
        get() = _navigateToSignUp

    private val _navigateToPhotos = MutableLiveData<Boolean>(false)
    val navigateToPhotos:LiveData<Boolean>
        get() = _navigateToPhotos
    fun navigateToPhotos(){
        _navigateToPhotos.value = true
    }
    fun onNavigatedToPhotos(){
        _navigateToPhotos.value = false
    }

    //navigation for sign in
    private val _navigateToSignIn = MutableLiveData<Boolean>(false)
    val navigateToSignIn: LiveData<Boolean>
        get() = _navigateToSignIn
    //list navigate
    fun onNavigatedToList() {
        _navigateToList.value = false
    }

    //signup navigate
    fun navigateToSignUp() {
        _navigateToSignUp.value = true
    }
    fun onNavigatedToSignUp() {
        _navigateToSignUp.value = false
    }
    //signin navigate
    fun navigateToSignIn() {
        _navigateToSignIn.value = true
    }
    fun onNavigatedToSignIn() {
        _navigateToSignIn.value = false
    }



    var time = ""
    private var auth: FirebaseAuth
    init {
        auth = Firebase.auth
        if (time.trim() == "")
            photo.value = Photo()
        //_photos.value = mutableListOf<String>()
    }
    private val _navToPhoto = MutableLiveData<String?>()
    val navToPhoto:LiveData<String?>
        get() = _navToPhoto
    var imageUrl = ""

    fun onPhotoClick(selectedPhoto:Photo){
        _navToPhoto.value = selectedPhoto.imageUrl
        imageUrl = selectedPhoto.imageUrl
        photo.value = selectedPhoto
    }
    fun onPhotoNav(){
        _navToPhoto.value = null
    }


    var user:User = User()
    var verifyPassword = ""
    fun signIn() {
        if (user.email.isEmpty() || user.password.isEmpty()) {
            _errorHappened.value = "Email and password cannot be empty."
            return
        }
        auth.signInWithEmailAndPassword(user.email, user.password).addOnCompleteListener {
            if (it.isSuccessful) {
                //initializeTheDatabaseReference()
                _navigateToList.value = true
            } else {
                _errorHappened.value = it.exception?.message
            }
        }
    }
    fun signUp() {
        if (user.email.isEmpty() || user.password.isEmpty()) {
            _errorHappened.value = "Email and password cannot be empty."
            return
        }
        if (user.password != verifyPassword) {
            _errorHappened.value = "Password and verify do not match."
            return
        }
        auth.createUserWithEmailAndPassword(user.email, user.password).addOnCompleteListener {
            if (it.isSuccessful) {
                _navigateToSignIn.value = true
            } else {
                _errorHappened.value = it.exception?.message
            }
        }
    }
}
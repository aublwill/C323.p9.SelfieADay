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
    //shared view model
    val TAG = "PhotosViewModel"
    var signedInUser:User? = null

    //photos variables
    var photo = MutableLiveData<Photo>()
    private val _photos:MutableLiveData<MutableList<Photo>> = MutableLiveData()
    val photos:LiveData<List<Photo>>
        get() = _photos as LiveData<List<Photo>>

    //sensor varibales
    private lateinit var accSensor: MeasurableSensor
    private var accData = floatArrayOf(
        SensorManager.GRAVITY_EARTH, SensorManager.GRAVITY_EARTH, 0.0F
    )

    //shake/accelerometer variables
    private var _shake:MutableLiveData<Boolean> = MutableLiveData(false)
    val shake:LiveData<Boolean>
        get() = _shake
    fun onShaked() {
        _shake.value = false
    }

    /*
    @param sAcc:MeasurableSensor
    initializes sensor
    detects if device was shaken
     */
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
    /*
    @no params
    creates photo based on current values(time, user, url)
    adds photo to database
     */
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
    @no params
    signs current user out of app
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

    //navigation for photos
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

    //initialize authentication
    var time = ""
    private var auth: FirebaseAuth
    init {
        auth = Firebase.auth
        if (time.trim() == "")
            photo.value = Photo()
    }

    //selected photo navigation
    private val _navToPhoto = MutableLiveData<String?>()
    val navToPhoto:LiveData<String?>
        get() = _navToPhoto
    var imageUrl = ""

    /*
    @param selectedPhoto:Photo
    makes selected image full screen
     */
    fun onPhotoClick(selectedPhoto:Photo){
        _navToPhoto.value = selectedPhoto.imageUrl
        imageUrl = selectedPhoto.imageUrl
        photo.value = selectedPhoto
    }
    fun onPhotoNav(){
        _navToPhoto.value = null
    }


    //user variables
    var user:User = User()
    var verifyPassword = ""
    /*
    @no param
    signs user into the app
     */
    fun signIn() {
        if (user.email.isEmpty() || user.password.isEmpty()) {
            _errorHappened.value = "Email and password cannot be empty."
            return
        }
        auth.signInWithEmailAndPassword(user.email, user.password).addOnCompleteListener {
            if (it.isSuccessful) {
                _navigateToList.value = true
            } else {
                _errorHappened.value = it.exception?.message
            }
        }
    }
    /*
    @no params
    signs new user up to the app
     */
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
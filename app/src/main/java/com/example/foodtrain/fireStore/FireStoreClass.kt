package com.example.foodtrain.fireStore

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import com.example.foodtrain.Constants
import com.example.foodtrain.RegisterActivity
import com.example.foodtrain.LoginActivity
import com.example.foodtrain.UserProfile
import com.example.foodtrain.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class FireStoreClass {

    private val foodTrainFireStore = FirebaseFirestore.getInstance()

    fun registerUser( activity : RegisterActivity , userInfo : User){

        foodTrainFireStore.collection(Constants.USERS)
            .document(userInfo.userId)
            .set(userInfo, SetOptions.merge())
            .addOnCompleteListener {
                activity.userRegistrationSuccess()
            }
            .addOnFailureListener { e->
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while registering the user",
                    e
                )
            }
    }

    private fun getCurrentUserId() : String {
        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserId = ""
        if(currentUser != null){
            currentUserId = currentUser.uid
        }
        return currentUserId
    }
    fun getUserDetails(activity :Activity){

        foodTrainFireStore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .get()
            .addOnSuccessListener {document ->

                Log.i(activity.javaClass.simpleName,document.toString())

                val user = document.toObject(User::class.java)!!

                val sharedPreferences = activity.getSharedPreferences(
                    Constants.FOODTRAIN_PREFERENCES,
                    Context.MODE_PRIVATE
                )

                val editor :SharedPreferences.Editor = sharedPreferences.edit()
                // Key: logged_in_username

                editor.putString(
                    Constants.LOGGED_IN_USERNAME,
                    "${user.fname} ${user.lname}"
                )
                editor.apply()

                when(activity){
                    is LoginActivity ->{
                        activity.userLoggedInSuccess(user)
                    }
                }
            }
    }

    fun updateUserActivity(activity: Activity , userHashMap: HashMap<String,Any>){
        foodTrainFireStore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .update(userHashMap)
            .addOnSuccessListener {

                when (activity) {
                    is UserProfile -> {
                        activity.onUserProfileUpdateSuccess()

                    }
                }
            }
            .addOnFailureListener {e->
                when (activity) {
                    is UserProfile -> {
                        activity.closingProgressBar()
                    }
                }

                Log.e(
                    activity.javaClass.simpleName,
                    "Error occurred while updating the user details. ",
                    e
                )
            }

    }

    fun uploadImageToCloudStorage(activity: Activity,imageFileURI : Uri?){
        val sRef : StorageReference = FirebaseStorage.getInstance().reference.child(
            Constants.USER_PROFILE_IMAGE + System.currentTimeMillis() + "."
            + Constants.getFileExtension(
                activity,
                imageFileURI
            )
        )
        sRef.putFile(imageFileURI!!).addOnSuccessListener { taskSnapShot ->
            Log.e(
                "Firebase Image URL",
                taskSnapShot.metadata!!.reference!!.downloadUrl.toString()
            )

            taskSnapShot.metadata!!.reference!!.downloadUrl
                .addOnSuccessListener { uri ->
                    Log.e("Downloadable Image URL",uri.toString())
                    when(activity){
                        is UserProfile -> {
                            activity.imageUploadSuccess(uri.toString())
                        }
                    }
                }
        }
            .addOnFailureListener{exception ->

                when(activity){
                    is UserProfile ->{
                        activity.closingProgressBar()
                    }
                }

                Log.e(
                    activity.javaClass.simpleName,
                    exception.message,
                    exception
                )
            }
    }

}
package com.example.foodtrain.fireStore

import android.app.Activity
import android.util.Log
import com.example.foodtrain.Constants
import com.example.foodtrain.RegisterActivity
import com.example.foodtrain.SecondPage
import com.example.foodtrain.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

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

                when(activity){
                    is SecondPage ->{
                        activity.userLoggedInSuccess(user)
                    }
                }
            }
    }
}
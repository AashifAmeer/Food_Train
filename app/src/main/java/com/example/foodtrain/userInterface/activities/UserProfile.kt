package com.example.foodtrain.userInterface.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.foodtrain.Constants
import com.example.foodtrain.GlideLoader
import com.example.foodtrain.R
import com.example.foodtrain.fireStore.FireStoreClass
import com.example.foodtrain.models.User
import com.google.android.material.textfield.TextInputEditText
import java.io.IOException

class UserProfile : BaseActivity(), View.OnClickListener {

    private lateinit var userDetails : User
    private var userImageUri: Uri? = null
    private var userImageURL: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

         userDetails = User()
        var heading : String = "COMPLETE PROFILE"

        if(intent.hasExtra(Constants.EXTRA_USER_DETAILS) ){
             // Getting extra user details from parcelableExtra.
            userDetails = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!
        }
        if(intent.hasExtra(Constants.HEADING)){
            heading = intent.getStringExtra(Constants.HEADING)!!
        }
        val profileHeading = findViewById<TextView>(R.id.profileHeading)
        val fName = findViewById<TextInputEditText>(R.id.f_name_input)
        val lName = findViewById<TextInputEditText>(R.id.l_name_input)
        val email = findViewById<TextInputEditText>(R.id.email_input)
        val userImage = findViewById<ImageView>(R.id.userImage)
        val mobile = findViewById<TextInputEditText>(R.id.mobile)
        val genderMale = findViewById<RadioButton>(R.id.radioButton1)
        val genderFemale = findViewById<RadioButton>(R.id.radioButton2)
        val saveButton = findViewById<Button>(R.id.saveButton)

//        userImage.setImageURI(userDetails.image.toUri())
        profileHeading.text = heading

        Glide.with(this)
            .load(userDetails.image)
            .into(userImage)

        fName.isEnabled = false
        fName.setText(userDetails.fname)

        lName.isEnabled = false
        lName.setText(userDetails.lname)

        email.isEnabled = false
        email.setText(userDetails.email)

        mobile.setText((userDetails.mobile).toString())

        if(userDetails.gender == Constants.MALE){
            genderMale.isChecked = true
        }
        else{
            genderFemale.isChecked = true
        }

        userImage.setOnClickListener(this@UserProfile)
        saveButton.setOnClickListener (this@UserProfile)

    }

    override fun onClick(view: View?) {
        if(view != null){

            when(view.id){
                R.id.userImage -> {
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                           // showErrorSnackBar("You already have the permission",false)
                        Constants.openImagePicker(this@UserProfile)
                    }
                    else{
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            Constants.READ_STORAGE_PERMISSION_CODE
                        )
                    }
                }
                R.id.saveButton ->{
                    if (userProfileValidation()){
                        startProgressBar()

                        if(userImageUri != null){
                            FireStoreClass().uploadImageToCloudStorage(this, userImageUri )
                        }
                        else{
                            updateUserProfileDetails()
                        }
                    }
                }
            }
        }
    }

    private fun updateUserProfileDetails(){
        val userHashMap = HashMap<String,Any>()
        val mobile = findViewById<TextInputEditText>(R.id.mobile).text.toString().trim{it <= ' '}

        val gender = if (findViewById<RadioButton>(R.id.radioButton1).isChecked){
            Constants.MALE
        }else{
            Constants.FEMALE
        }

        if(mobile.isNotEmpty()){
            userHashMap[Constants.MOBILE] = mobile.toLong()
        }
        if(userImageURL.isNotEmpty()){
            userHashMap[Constants.USER_IMAGE_URL] = userImageURL
        }
        userHashMap[Constants.GENDER] = gender

        userHashMap[Constants.USER_PROFILE_COMPLETED] = 1
        FireStoreClass().updateUserActivity(this@UserProfile,userHashMap)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.READ_STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //showErrorSnackBar("Permission granted",false)
                Constants.openImagePicker(this@UserProfile)
            } else {
                // Permission denied
                // Handle permission denial here
                Toast.makeText(this,"Permission denied",Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val userImage = findViewById<ImageView>(R.id.userImage)
        if ( resultCode == Activity.RESULT_OK ) {

            if (requestCode == Constants.IMAGE_REQUEST_CODE) {
                if (data != null) {
                    try {
                         userImageUri = data.data!!
                        //userImage.setImageURI(selectedImageFileUri)
                        GlideLoader(this).loadUserPicture(userImageUri!!,userImage)
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(
                            this@UserProfile,
                            "Image selection failed !",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun userProfileValidation() : Boolean{
        return when{
            TextUtils.isEmpty(findViewById<TextInputEditText>(R.id.mobile).text.toString().trim { it <= ' ' } )->{
                showErrorSnackBar("Please provide your mobile",true)
                return false
            }
            !findViewById<RadioButton>(R.id.radioButton1).isChecked && !findViewById<RadioButton>(R.id.radioButton2).isChecked ->{
                showErrorSnackBar("Gender is required",true)
                return false
            }
            else ->{
                return true
            }
        }
    }
    fun onUserProfileUpdateSuccess(){
        closingProgressBar()
        Toast.makeText(this,"Successfully updated user profile",Toast.LENGTH_SHORT).show()

        startActivity(Intent(this@UserProfile, BottomNavBarActivity::class.java))
        finish()
    }

    fun imageUploadSuccess(imageURL :String){
        //closingProgressBar()
        userImageURL = imageURL
        updateUserProfileDetails()
    }
}
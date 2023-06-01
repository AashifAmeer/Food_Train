package com.example.foodtrain

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.foodtrain.models.User
import com.google.android.material.textfield.TextInputEditText
import java.io.IOException

class UserProfile : BaseActivity(), View.OnClickListener {

    private var userImage : ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        var userDetails :User = User()

        if(intent.hasExtra(Constants.EXTRA_USER_DETAILS)){
             // Getting extra user details from parcelableExtra.
            userDetails = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!
        }

        val fName = findViewById<TextInputEditText>(R.id.f_name_input)
        val lName = findViewById<TextInputEditText>(R.id.l_name_input)
        val email = findViewById<TextInputEditText>(R.id.email_input)

         userImage = findViewById(R.id.userImage)

        fName.isEnabled = false
        fName.setText(userDetails.fname)

        lName.isEnabled = false
        lName.setText(userDetails.lname)

        email.isEnabled = false
        email.setText(userDetails.email)

        userImage?.setOnClickListener(this@UserProfile)
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
            }
        }
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
        if ( resultCode == Activity.RESULT_OK ) {

            if (requestCode == Constants.IMAGE_REQUEST_CODE) {
                if (data != null) {
                    try {
                        val selectedImageFileUri = data.data!!
                        userImage?.setImageURI(selectedImageFileUri)
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
}
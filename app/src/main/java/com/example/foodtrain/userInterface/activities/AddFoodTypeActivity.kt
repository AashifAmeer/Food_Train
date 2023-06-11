package com.example.foodtrain.userInterface.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.foodtrain.Constants
import com.example.foodtrain.GlideLoader
import com.example.foodtrain.R
import com.example.foodtrain.fireStore.FireStoreClass
import com.example.foodtrain.models.FoodType
import com.google.android.material.textfield.TextInputEditText
import java.io.IOException
import java.util.*

class AddFoodTypeActivity : BaseActivity(),View.OnClickListener {

    private var foodTypeImageUri: Uri? = null
    private var foodTypeImageURL: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_food_type)

        // When clicking food type image
        val foodTypeImage = findViewById<ImageView>(R.id.foodTypeImage)
        foodTypeImage.setOnClickListener(this)

        //When clicking food
        val addFoodTypeButton = findViewById<Button>(R.id.addFoodTypeButton)
        addFoodTypeButton.setOnClickListener(this)
    }

    private fun addNewFoodItem(){

        val foodTypeId = generateNewID()
        val foodTypeName = findViewById<TextInputEditText>(R.id.food_type_name_input).text.toString().trim { it <= ' ' }

        val foodType : FoodType = if(foodTypeImageURL.isEmpty()){
            FoodType(foodTypeId,foodTypeName,"", emptyList())
        } else{
            FoodType(foodTypeId,foodTypeName,foodTypeImageURL, emptyList())
        }
        FireStoreClass().addNewFoodTypeToFireStore(this,foodType)
    }

    fun foodTypeAddedSuccess(){
        closingProgressBar()
        Toast.makeText(
            this,
            "Food Type added successfully",
            Toast.LENGTH_SHORT
        ).show()

        startActivity(Intent(this, BottomNavBarActivity::class.java))
        finish()
    }

    override fun onClick(view: View?) {
        if(view != null){

            when(view.id){
                R.id.foodTypeImage -> {
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                        // showErrorSnackBar("You already have the permission",false)
                        Constants.openImagePicker(this)
                    }
                    else{
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            Constants.READ_STORAGE_PERMISSION_CODE
                        )
                    }
                }
                R.id.addFoodTypeButton ->{
                    if (foodTypeValidation()){
                        startProgressBar()

                        if(foodTypeImageUri != null){
                            FireStoreClass().uploadImageToCloudStorage(this, foodTypeImageUri )
                        }
                        else{
                            addNewFoodItem()
                        }
                    }
                }

            }
        }
    }
    private fun foodTypeValidation() : Boolean{
        return when{
            TextUtils.isEmpty(findViewById<TextInputEditText>(R.id.food_type_name_input).text.toString().trim { it <= ' ' } )->{
                showErrorSnackBar("Please provide food type name",true)
                return false
            }
            else ->{
                return true
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
                Constants.openImagePicker(this)
            } else {
                // Permission denied
                // Handle permission denial here
                Toast.makeText(this,"Permission denied", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        val foodTypeImage = findViewById<ImageView>(R.id.foodTypeImage)
        if ( resultCode == Activity.RESULT_OK ) {

            if (requestCode == Constants.IMAGE_REQUEST_CODE) {
                if (data != null) {
                    try {
                        foodTypeImageUri = data.data!!
                        //userImage.setImageURI(selectedImageFileUri)
                        GlideLoader(this).loadUserPicture(foodTypeImageUri!!,foodTypeImage)
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(
                            this,
                            "Image selection failed !",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
    fun imageUploadSuccess(imageURL :String){
        //closingProgressBar()
        foodTypeImageURL = imageURL
        addNewFoodItem()
    }

    private fun generateNewID() : String{
        return UUID.randomUUID().toString()
    }
}
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
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import com.example.foodtrain.Constants
import com.example.foodtrain.GlideLoader
import com.example.foodtrain.R
import com.example.foodtrain.fireStore.FireStoreClass
import com.example.foodtrain.models.FoodItem
import com.example.foodtrain.models.FoodType
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.*

class AddFoodItemsActivity : BaseActivity(), View.OnClickListener {

    private val foodTypeNames = mutableListOf<String>()
    private var foodImageUri: Uri? = null
    private var foodImageURL: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_food_items)

        foodTypeNames.clear()
        foodTypeNames.add(0, "-- Select Food Type --")

        GlobalScope.launch (Dispatchers.Main) {
            val foodTypes = FireStoreClass().loadFoodTypesNames()

            foodTypeNames.addAll(foodTypes)
        }

        val foodTypeSpinner = findViewById<Spinner>(R.id.foodTypeSpinner)
        val adapter = ArrayAdapter(this,android.R.layout.simple_spinner_item,foodTypeNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        foodTypeSpinner.adapter = adapter

        val foodImage = findViewById<ImageView>(R.id.foodImage)
        foodImage.setOnClickListener(this)

        //When clicking food
        val addFoodItemButton = findViewById<Button>(R.id.addButton)
        addFoodItemButton.setOnClickListener(this)
    }

    private fun addNewFood(){

        val foodId = generateNewID()
        val foodName = findViewById<TextInputEditText>(R.id.food_name_input).text.toString().trim { it <= ' ' }
        val foodPrice = findViewById<TextInputEditText>(R.id.price_input).text.toString().trim { it <= ' ' }
        val foodQuantity = findViewById<TextInputEditText>(R.id.quantity_input).text.toString().trim { it <= ' ' }
        val foodDescription = findViewById<TextInputEditText>(R.id.product_description).text.toString().trim { it <= ' ' }
        val foodTypeSpinner = findViewById<Spinner>(R.id.foodTypeSpinner)
        val selectedFoodType = foodTypeSpinner.selectedItem.toString()

        GlobalScope.launch {
            val foodTypeId = FireStoreClass().getCurrentFoodTypeIdSelectedBySpinner(selectedFoodType)


            val foodItem : FoodItem = if(foodImageURL.isEmpty()){
                FoodItem(foodId,foodName,"",foodPrice.toDouble(),foodQuantity.toInt(),foodDescription,selectedFoodType,foodTypeId.toString())
            } else{
                FoodItem(foodId,foodName,foodImageURL,foodPrice.toDouble(),foodQuantity.toInt(),foodDescription,selectedFoodType,foodTypeId.toString() )
            }
            FireStoreClass().addNewFoodItemToFireStore(this@AddFoodItemsActivity,foodItem)
        }
    }

    override fun onClick(view: View?) {
        if(view != null){

            when(view.id){
                R.id.foodImage -> {
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
                R.id.addButton ->{
                    if (foodItemValidation()){
                        startProgressBar()

                        if(foodImageUri != null){
                            FireStoreClass().uploadImageToCloudStorage(this, foodImageUri )
                        }
                        else{
                            addNewFood()
                        }
                    }
                }

            }
        }
    }

    private fun foodItemValidation() : Boolean{
        return when{

            TextUtils.isEmpty(findViewById<TextInputEditText>(R.id.food_name_input).text.toString().trim { it <= ' ' } )->{
                showErrorSnackBar("Please provide food item name",true)
                return false
            }
            TextUtils.isEmpty(findViewById<TextInputEditText>(R.id.price_input).text.toString().trim { it <= ' ' } )->{
                showErrorSnackBar("Please provide price of food item",true)
                return false
            }
            TextUtils.isEmpty(findViewById<TextInputEditText>(R.id.quantity_input).text.toString().trim { it <= ' ' } )->{
                showErrorSnackBar("Please provide food item name",true)
                return false
            }
            TextUtils.isEmpty(findViewById<TextInputEditText>(R.id.product_description).text.toString().trim { it <= ' ' } )->{
                showErrorSnackBar("Please provide food item name",true)
                return false
            }
            !(isSpinnerSelectionValid(findViewById(R.id.foodTypeSpinner))) ->{
                showErrorSnackBar("Please select food type",true)
                return false
            }
            else ->{
                return true
            }
        }
    }
    private fun isSpinnerSelectionValid(spinner : Spinner) : Boolean{
        val selectedItemPosition = spinner.selectedItemPosition
        return selectedItemPosition != 0 // first is the hint
    }

    fun foodItemAddedSuccess(){
        closingProgressBar()
        Toast.makeText(
            this,
            "Food Item added successfully",
            Toast.LENGTH_SHORT
        ).show()

        startActivity(Intent(this, BottomNavBarActivity::class.java))
        finish()
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
        val foodImage = findViewById<ImageView>(R.id.foodImage)
        if ( resultCode == Activity.RESULT_OK ) {

            if (requestCode == Constants.IMAGE_REQUEST_CODE) {
                if (data != null) {
                    try {
                        foodImageUri = data.data!!
                        //userImage.setImageURI(selectedImageFileUri)
                        GlideLoader(this).loadUserPicture(foodImageUri!!,foodImage)
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
        foodImageURL = imageURL
        addNewFood()
    }

    private fun generateNewID() : String{
        return UUID.randomUUID().toString()
    }
}
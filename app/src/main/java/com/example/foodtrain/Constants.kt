package com.example.foodtrain

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import androidx.core.app.ActivityCompat.startActivityForResult

object Constants {
    const val READ_STORAGE_PERMISSION_CODE: Int = 2
    const val USERS : String = "users"
    const val FOODTRAIN_PREFERENCES : String = "FoodTrainPrefs"
    const val LOGGED_IN_USERNAME : String = "logged_in_username"
    const val EXTRA_USER_DETAILS : String = "extra_user_details"

    const val IMAGE_REQUEST_CODE :Int = 1

    const val MALE :String = "male"
    const val FEMALE : String = "female"
    const val MOBILE : String = "mobile"
    const val GENDER : String = "gender"
    const val USER_PROFILE_IMAGE : String = "user_profile_image"
    const val USER_IMAGE_URL : String = "image"

    const val HEADING : String = "profileHeading"
    const val USER_PROFILE_COMPLETED :String = "profileCompleted"

    const val FOOD_TYPE : String = "food_types"
    const val FOOD_TYPE_NAME : String = "foodTypeName"
    const val FOOD_Items : String = "foodItems"

    const val ADD_TO_CART : String = "add_to_cart"
    const val FOOD_QUANTITY_ORDER : String = "food_quantity_order"
    const val FOOD_TOTAL_PRICE : String = "food_total_price"

     fun openImagePicker(activity : Activity) {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activity.startActivityForResult(intent, IMAGE_REQUEST_CODE)
    }

    fun getFileExtension(activity:Activity,uri : Uri?) : String?{

        return MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }

}
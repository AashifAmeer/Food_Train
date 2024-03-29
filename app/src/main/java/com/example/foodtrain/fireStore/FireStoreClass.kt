package com.example.foodtrain.fireStore

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintSet
import com.example.foodtrain.Constants
import com.example.foodtrain.models.AddToCart
import com.example.foodtrain.models.FoodItem
import com.example.foodtrain.models.FoodType
import com.example.foodtrain.models.User
import com.example.foodtrain.userInterface.activities.*
import com.example.foodtrain.userInterface.fragments.HomeFragment
import com.example.foodtrain.userInterface.fragments.OrdersFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlin.collections.HashMap

class FireStoreClass {


    private val foodTrainFireStore = FirebaseFirestore.getInstance()

    fun registerUser(activity : RegisterActivity, userInfo : User){

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
                    is BottomNavBarActivity ->{
                        activity.userDetailSuccess(user)
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
                        is AddFoodTypeActivity -> {
                            activity.imageUploadSuccess(uri.toString())
                        }
                        is AddFoodItemsActivity -> {
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
                    is AddFoodTypeActivity ->{
                        activity.closingProgressBar()
                    }
                    is AddFoodItemsActivity -> {
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
    fun addNewFoodTypeToFireStore(activity: AddFoodTypeActivity, foodType: FoodType){

        foodTrainFireStore.collection(Constants.FOOD_TYPE)
            .document(foodType.foodTypeId)
            .set(foodType, SetOptions.merge())
            .addOnCompleteListener {

                GlobalScope.launch(Dispatchers.Main){
                    val foodType = loadFoodTypes()
                    HomeFragment().getFoodTypes(foodType)
                }
                activity.foodTypeAddedSuccess()

            }
            .addOnFailureListener { e->
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while adding the food type",
                    e
                )
            }
    }

    fun addNewFoodItemToFireStore(activity: AddFoodItemsActivity, foodItems: FoodItem){

        foodTrainFireStore.collection(Constants.FOOD_TYPE)
            .document(foodItems.food_type_id)
            .update(Constants.FOOD_Items,FieldValue.arrayUnion(foodItems))
            .addOnSuccessListener {
                GlobalScope.launch(Dispatchers.Main){
                    val foodType = loadFoodTypes()
                    HomeFragment().getFoodTypes(foodType)
                }
                activity.foodItemAddedSuccess()
            }
            .addOnFailureListener {e->
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while adding food item",
                    e
                )
            }
    }

     suspend fun loadFoodTypes() : ArrayList<FoodType>{

        val foodTypes = ArrayList<FoodType>()
        try {
            val querySnapshot = foodTrainFireStore.collection(Constants.FOOD_TYPE).get().await()

            for(document in querySnapshot) {
                val foodType = document.toObject(FoodType::class.java)
                foodTypes.add(foodType)
            }

        }catch (e : Exception){

        }
        return foodTypes
    }

    suspend fun getCurrentFoodTypeIdSelectedBySpinner(foodTypeName : String) : String{

        var currentFoodTypeId  = ""
        try {
            val querySnapshot = foodTrainFireStore.collection(Constants.FOOD_TYPE)
                .whereEqualTo(Constants.FOOD_TYPE_NAME,foodTypeName)
                .get()
                .await()


            if (!querySnapshot.isEmpty) {
                val document = querySnapshot.documents[0]
                val foodTypeId = document.id
                // Use the foodTypeId as needed
                currentFoodTypeId = foodTypeId

            } else {
                Log.d("Firestore", "No matching document found")
            }
        }
        catch (e: Exception){
            e.printStackTrace()
        }

        return currentFoodTypeId
    }

     suspend fun loadFoodTypesNames() : List<String> {
        val foodTypeNames = mutableListOf<String>()

        try {

            val querySnapshot = foodTrainFireStore.collection(Constants.FOOD_TYPE).get().await()

                for(document in querySnapshot){
                    val foodTypeName = document.getString(Constants.FOOD_TYPE_NAME)
                    foodTypeName?.let{
                        foodTypeNames.add(foodTypeName)
                    }
                }

        }catch (e: Exception){

        }

        return foodTypeNames
    }

    // add to cart order to firebase

    fun addToCartOrdersToFireStore( context: Context, addToCart : AddToCart){

        addToCart.user_id = getCurrentUserId()

        foodTrainFireStore.collection(Constants.ADD_TO_CART)
            .document(addToCart.order_id)
            .set(addToCart,SetOptions.merge())
            .addOnCompleteListener {

                GlobalScope.launch(Dispatchers.Main){

                }
                BottomNavBarActivity().addFoodItemToCartSuccess(addToCart.food_name,context)
                //activity.addFoodItemToCartSuccess()

            }
            .addOnFailureListener { e->
                Log.e(
                    "Error",
                    "Error while adding the food to cart",
                    e
                )
            }
    }


    suspend fun loadOrderDetails() : ArrayList<AddToCart> {
        val orderList = ArrayList<AddToCart>()

        try {
            val querySnapshot = foodTrainFireStore.collection(Constants.ADD_TO_CART).get().await()

            for(document in querySnapshot) {
                val orderDetails = document.toObject(AddToCart::class.java)
                orderList.add(orderDetails)
            }

        }catch (e : Exception){

        }

        return orderList
    }

    fun updateOrderDetails(orderId : String,addToCart : HashMap<String,Any>){

        foodTrainFireStore.collection(Constants.ADD_TO_CART)
            .document(orderId)
            .update(addToCart)
            .addOnSuccessListener {

                GlobalScope.launch(Dispatchers.Main){
                    val orders = loadOrderDetails()
                    OrdersFragment().getOrderUpdates(orders)
                }

            }
            .addOnFailureListener {e->
                Log.e(
                    "Error",
                    "Error occurred while updating the user details. ",
                    e
                )
            }

    }
}


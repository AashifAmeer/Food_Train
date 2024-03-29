package com.example.foodtrain.userInterface.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.widget.Toolbar
import androidx.core.net.toUri
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.foodtrain.Constants
import com.example.foodtrain.GlideLoader
import com.example.foodtrain.R
import com.example.foodtrain.databinding.ActivityBottomNavBarBinding
import com.example.foodtrain.fireStore.FireStoreClass
import com.example.foodtrain.models.User
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BottomNavBarActivity : BaseActivity() {

    private lateinit var binding: ActivityBottomNavBarBinding
    private lateinit var userDetails : User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBottomNavBarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_bottom_nav_bar)

        navView.setupWithNavController(navController)

        val welcomeText = findViewById<TextView>(R.id.userName)

        val sharedPreferences = getSharedPreferences(Constants.FOODTRAIN_PREFERENCES, Context.MODE_PRIVATE)
        val username = sharedPreferences.getString(Constants.LOGGED_IN_USERNAME,"A").toString()

        welcomeText.text = "Hello, $username"

        sideNavigationBar()
        getUserDetails()

        val currentUser = findViewById<ImageView>(R.id.currentUser)
        currentUser.setOnClickListener{
            val intent = Intent(this@BottomNavBarActivity, FoodAddSelectionActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onBackPressed() {
        doubleBackToExit()
    }
    private fun sideNavigationBar(){
        val drawerLayout = findViewById<DrawerLayout>(R.id.container)
        val navigationView = findViewById<NavigationView>(R.id.sideNavigationView)
        val toolbar = findViewById<Toolbar>(R.id.main_toolbar)

        val actionBarToggle =  ActionBarDrawerToggle(
            this@BottomNavBarActivity,
            drawerLayout,
            toolbar,
            R.string.navbar_open,
            R.string.navbar_close
        )
        drawerLayout.addDrawerListener(actionBarToggle)
        actionBarToggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navigationView.setNavigationItemSelectedListener{ MenuItem->
            when(MenuItem.itemId) {
                R.id.edit_user ->{

                    val intent = Intent(this@BottomNavBarActivity, UserProfile::class.java)
                    intent.putExtra(Constants.EXTRA_USER_DETAILS,userDetails)
                    intent.putExtra(Constants.HEADING,"EDIT PROFILE")
                    startActivity(intent)
                    drawerLayout.closeDrawers()

                    true
                }
                R.id.logout ->{
                    GlobalScope.launch(Dispatchers.Main) {
                        signOutWithDelay()
                    }
                    true
                }

                else-> false
            }
        }

    }

    private suspend fun signOutWithDelay() {
        startProgressBar()

        delay(2000)

        FirebaseAuth.getInstance().signOut()

        closingProgressBar()

        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun getUserDetails(){
        FireStoreClass().getUserDetails(this@BottomNavBarActivity)
    }
    fun userDetailSuccess(user : User){

        userDetails = user

        val userImage = findViewById<ImageView>(R.id.header_user_image)
        val uname = findViewById<TextView>(R.id.Uname)
        val email = findViewById<TextView>(R.id.userEmail)
        val mobile = findViewById<TextView>(R.id.userMobile)
        val gender = findViewById<TextView>(R.id.userGender)

        if(user != null){
            GlideLoader(this).loadUserPicture(user.image.toUri(),userImage)
            uname.text = "${user.fname} ${user.lname}"
            email.text = "Email   : ${user.email}"
            mobile.text = "Mobile : ${user.mobile}"
            gender.text = "Gender : ${user.gender.toUpperCase()}"
        }
        userImage.setOnClickListener {
            val intent = Intent(this@BottomNavBarActivity, UserProfile::class.java)
            intent.putExtra(Constants.EXTRA_USER_DETAILS,userDetails)
            intent.putExtra(Constants.HEADING,"EDIT PROFILE")
            startActivity(intent)
        }
    }
    override fun onResume() {
        super.onResume()
        val navigationView = findViewById<NavigationView>(R.id.sideNavigationView)
        navigationView.setCheckedItem(0) // Use 0 as the argument to clear the checked state
    }
    fun addFoodItemToCartSuccess(foodName : String,context: Context){
        Toast.makeText(context,"$foodName is added to cart successfully !",Toast.LENGTH_LONG).show()
    }
}
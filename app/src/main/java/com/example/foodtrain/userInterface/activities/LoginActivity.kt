package com.example.foodtrain.userInterface.activities

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.TextView
import com.example.foodtrain.Constants
import com.example.foodtrain.R
import com.example.foodtrain.fireStore.FireStoreClass
import com.example.foodtrain.models.User
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)

        val forgotPassword = findViewById<TextView>(R.id.forgotPassword)
        val loginBtn = findViewById<Button>(R.id.button)
        val registerBtnClick = findViewById<TextView>(R.id.registerBtn)

        forgotPassword.setOnClickListener {
            val intent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        loginBtn.setOnClickListener {
            loginRegisteredUsers()
        }

        registerBtnClick.paintFlags = registerBtnClick.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG

        registerBtnClick.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }
    }
    private fun validateUserLogin(): Boolean {
        val email  = findViewById<TextInputEditText>(R.id.emailID).text.toString().trim { it <= ' ' }
        val password  = findViewById<TextInputEditText>(R.id.password).text.toString().trim { it <= ' ' }

        return when{
            TextUtils.isEmpty(email) ->{
                showErrorSnackBar("Email Id is required",true)
                false
            }
            TextUtils.isEmpty(password) ->{
                showErrorSnackBar("Password is required",true)
                false
            }
            else ->{
                true
            }
        }
    }
    private fun loginRegisteredUsers(){
        if(validateUserLogin()){
            val email  = findViewById<TextInputEditText>(R.id.emailID).text.toString().trim { it <= ' ' }
            val password  = findViewById<TextInputEditText>(R.id.password).text.toString().trim { it <= ' ' }

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        startProgressBar()
                        FireStoreClass().getUserDetails(this@LoginActivity)
                    }
                    else{
                        showErrorSnackBar("Error",true)
                    }
                }
        }
    }
    fun userLoggedInSuccess(user : User){

       closingProgressBar()
        if(user.profileCompleted == 0){
            val intent = Intent(this@LoginActivity, UserProfile::class.java)
            intent.putExtra(Constants.EXTRA_USER_DETAILS,user)
            startActivity(intent)
        }
        else{
//            val intent = Intent(this@LoginActivity, BottomNavBarActivity::class.java)
//            intent.putExtra(Constants.EXTRA_USER_DETAILS,user)
//            intent.putExtra(Constants.HEADING,"EDIT PROFILE")
//            startActivity(intent)
                startActivity(Intent(this@LoginActivity,BottomNavBarActivity::class.java))
        }

        finish()
    }
}
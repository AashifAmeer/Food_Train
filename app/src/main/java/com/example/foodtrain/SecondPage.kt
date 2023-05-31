package com.example.foodtrain

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.example.foodtrain.fireStore.FireStoreClass
import com.example.foodtrain.models.User
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class SecondPage : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second_page)

        val forgotPassword = findViewById<TextView>(R.id.forgotPassword)
        val loginBtn = findViewById<Button>(R.id.button)
        val registerBtnClick = findViewById<TextView>(R.id.registerBtn)

        forgotPassword.setOnClickListener {
            val intent = Intent(this@SecondPage, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        loginBtn.setOnClickListener {
            loginRegisteredUsers()
        }

        registerBtnClick.paintFlags = registerBtnClick.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG

        registerBtnClick.setOnClickListener {
            startActivity(Intent(this,RegisterActivity::class.java))
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
                        FireStoreClass().getUserDetails(this@SecondPage)
                    }
                    else{
                        showErrorSnackBar("Error",true)
                    }
                }
        }
    }
    fun userLoggedInSuccess(user : User){

        Log.i("First Name : ",user.fname)
        Log.i("Last Name  : ",user.lname)
        Log.i("Email Id   : ",user.email)

       closingProgressBar()

        startActivity(Intent(this@SecondPage,UserProfile::class.java))
        finish()
    }
}
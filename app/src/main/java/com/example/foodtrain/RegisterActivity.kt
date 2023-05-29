package com.example.foodtrain

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowManager
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import com.example.foodtrain.fireStore.FireStoreClass
import com.example.foodtrain.models.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class RegisterActivity : BaseActivity() {

    private var fName : TextInputEditText? = null
    private var lName : TextInputEditText? = null
    private var password : TextInputEditText? = null
    private var confirmPassword : TextInputEditText? = null
    private var email : TextInputEditText? = null
    private var checkBox : CheckBox? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        val registerBtnClick = findViewById<Button>(R.id.create_btn)
        val loginBtnClick = findViewById<TextView>(R.id.loginBtn)
        loginBtnClick.paintFlags = loginBtnClick.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG

        registerBtnClick.setOnClickListener {
            registerNewUser()
        }

        loginBtnClick.setOnClickListener {
            onBackPressed()
        }
    }

    private fun validateRegisterDetails() : Boolean {

         fName = findViewById(R.id.f_name)
         lName = findViewById(R.id.l_name)
         email = findViewById(R.id.email)
         password = findViewById(R.id.password)
         confirmPassword = findViewById(R.id.confirm_pswrd)
         checkBox = findViewById(R.id.check_agreement)

        return when{
            TextUtils.isEmpty(fName?.text.toString().trim{it <= ' '}) ->{
                showErrorSnackBar("First name is required",true)
                false
            }

            TextUtils.isEmpty(lName?.text.toString().trim { it <= ' ' }) ->{
                showErrorSnackBar("Last name is required",true)
                false
            }

            TextUtils.isEmpty(email?.text.toString().trim { it <= ' ' }) ->{
                showErrorSnackBar("Email ID is required",true)
                false
            }

            TextUtils.isEmpty(password?.text.toString().trim { it <= ' ' }) ->{
                showErrorSnackBar("Password is required",true)
                false
            }

            TextUtils.isEmpty(confirmPassword?.text.toString().trim { it <= ' ' }) ->{
                showErrorSnackBar("Confirm password is required",true)
                false
            }

            password?.text.toString().trim{it <= ' '} != confirmPassword?.text.toString() ->{
                showErrorSnackBar("Password & Confirm password mismatched",true)
                false
            }

             !checkBox?.isChecked!! ->{
                showErrorSnackBar("Please agree to the terms and conditions",true)
                false
            }
            else ->{
                //showErrorSnackBar("Your details are valid",false)
                true
            }
        }

    }
    private fun registerNewUser(){
        if(validateRegisterDetails()){

            startProgressBar()

            fName = findViewById(R.id.f_name)
            lName = findViewById(R.id.l_name)
            var email :String = findViewById<TextInputEditText>(R.id.email).text.toString().trim { it <= ' ' }
            var password :String = findViewById<TextInputEditText>(R.id.password).text.toString().trim { it <= ' ' }

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener  { task ->
                    closingProgressBar()
                        if (task.isSuccessful) {
                            val  firebaseUser : FirebaseUser = task.result?.user!!
                            val user = User(
                                firebaseUser.uid,
                                fName?.text.toString().trim{it <= ' '},
                                lName?.text.toString().trim{it <= ' '},
                                email
                            )
                            FireStoreClass().registerUser(this@RegisterActivity,user)

                            Toast.makeText(this@RegisterActivity,"Account created successfully",
                                Toast.LENGTH_LONG).show()
//                            FirebaseAuth.getInstance().signOut()
//                            finish()
                        } else {
                            showErrorSnackBar("Error!", true)
                        }
                }
        }

    }

    fun userRegistrationSuccess(){
        Toast.makeText(
            this@RegisterActivity,
            "User registered successfully",
            Toast.LENGTH_SHORT
        ).show()

    }

}
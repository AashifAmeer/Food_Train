package com.example.foodtrain.userInterface.activities

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.foodtrain.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        val forgotPasswordSubmit = findViewById<Button>(R.id.forgot_password_btn)

        forgotPasswordSubmit.setOnClickListener {
            forgotPasswordCheck()
        }
    }
    private fun forgotPasswordCheck(){

        val email = findViewById<TextInputEditText>(R.id.email_forgot).text.toString().trim { it <= ' ' }
        if (email.isEmpty()){
            showErrorSnackBar("Please enter email ID",true)
        }
        else{
            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener {
                    task ->
                    if (task.isSuccessful){
                        Toast.makeText(this@ForgotPasswordActivity,"Email sent successfully",Toast.LENGTH_LONG).show()
                        finish()
                    }
                    else{
                        showErrorSnackBar("Error !",true)
                    }
                }
        }
    }
}
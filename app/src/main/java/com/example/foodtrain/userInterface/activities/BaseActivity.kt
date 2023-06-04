package com.example.foodtrain.userInterface.activities

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.foodtrain.R
import com.google.android.material.snackbar.Snackbar

open class BaseActivity : AppCompatActivity() {

    private var backButtonPressedOnce = false
    private lateinit var progressBarDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

    }
    fun showErrorSnackBar(message : String , errorMessage:Boolean){

        val snackBar = Snackbar.make(findViewById(android.R.id.content),message,Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view

        if(errorMessage){
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(this@BaseActivity, R.color.red)
            )
        }
        else{
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(this@BaseActivity, R.color.green)
            )
        }
        snackBar.show()
    }

    fun startProgressBar(){
        progressBarDialog = Dialog(this@BaseActivity)

        progressBarDialog.setContentView(R.layout.activity_progress_bar)

        progressBarDialog.setCancelable(false)
        progressBarDialog.setCanceledOnTouchOutside(false)

        progressBarDialog.show()
    }
    fun closingProgressBar(){
        progressBarDialog.dismiss()
    }

    fun doubleBackToExit(){

        if(backButtonPressedOnce){
            super.onBackPressed()
            return
        }
        this.backButtonPressedOnce = true

        Toast.makeText(this,"Please click back button again to exit !",Toast.LENGTH_SHORT).show()

        @Suppress("DEPRECATION")
        Handler().postDelayed({backButtonPressedOnce = false},2000)
    }

}
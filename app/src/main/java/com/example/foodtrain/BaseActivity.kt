package com.example.foodtrain

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar

open class BaseActivity : AppCompatActivity() {

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
                ContextCompat.getColor(this@BaseActivity,R.color.red)
            )
        }
        else{
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(this@BaseActivity,R.color.green)
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

}
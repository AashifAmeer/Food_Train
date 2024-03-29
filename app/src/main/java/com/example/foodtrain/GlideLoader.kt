package com.example.foodtrain

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import java.io.IOException

class GlideLoader (val context :Context){

    fun loadUserPicture(imageURI :Uri,imageView : ImageView){

        try{

            Glide
                .with(context)
                .load(imageURI)
                .fitCenter()
                .placeholder(R.drawable.ic_launcher_background)
                .into(imageView)
        }catch (e :IOException){
            e.printStackTrace()
        }
    }
}
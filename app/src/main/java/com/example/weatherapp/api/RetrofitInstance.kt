package com.example.weatherapp.api

import com.example.weatherapp.utils.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    //configures and returnes an instance of retrofit
    fun getInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL) //sets base url for the instance
            .addConverterFactory(GsonConverterFactory.create())//specifies json parsing library, here we use gson
            .build()
    }


}
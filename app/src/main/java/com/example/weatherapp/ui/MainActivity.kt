package com.example.weatherapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.weatherapp.R
import com.example.weatherapp.api.RetrofitInstance
import com.example.weatherapp.api.WeatherAPI
import com.example.weatherapp.api.models.ApiResponse
import com.example.weatherapp.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    lateinit var tv: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tv = findViewById(R.id.textView)
        callAPI()
    }

    private fun callAPI() {
        //gets instance of Retrofit
        val retrofitInstance = RetrofitInstance.getInstance()
        //create instance of weatherAPI interface using retrofit
        val weatherAPI = retrofitInstance.create(WeatherAPI::class.java)
        //enqueues call of weather info
        weatherAPI.getWeatherInfo(Constants.API_KEY, "Trivandrum", "yes")
            .enqueue(object : Callback<ApiResponse> {
                //gets response
                override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                    if (response.isSuccessful) {//checks if response is successfull
                        val responseBody = response.body()//gets required data from response
                        tv.text = responseBody?.current?.temp_c?.toString()
                    } else {
                        tv.text = response.message()
                    }
                }

                //failure occurred, probably network failure due to no network.
                override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                    tv.text = t.message
                }

            })
    }
}
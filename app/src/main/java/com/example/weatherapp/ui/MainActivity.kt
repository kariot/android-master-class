package com.example.weatherapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.weatherapp.R
import com.example.weatherapp.api.RetrofitInstance
import com.example.weatherapp.api.WeatherAPI
import com.example.weatherapp.api.models.ApiResponse
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        callAPI()
    }

    private fun callAPI() {
        binding.layLoading.visibility = View.VISIBLE
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
                        binding.layLoading.visibility = View.GONE
                        val responseBody = response.body()//gets required data from response
                        updateUI(responseBody) //updates UI
                    }
                }

                //failure occurred, probably network failure due to no network.
                override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                }

            })
    }

    private fun updateUI(responseBody: ApiResponse?) {
        responseBody?.let { response -> // null check
            binding.apply {
                //sets ui with received data
                txtTemp.text = "${response.current.temp_c}°C"
                txtLocation.text = response.location.name
                txtUpdatedTime.text = response.current.last_updated
                txtCondition.text = response.current.condition.text
                val imageUrl = response.current.condition.icon
                Glide.with(this@MainActivity).load(imageUrl.replaceFirst("//", "")).into(binding.imgWeather)
            }

        }
    }
}
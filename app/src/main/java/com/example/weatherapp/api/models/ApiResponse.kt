package com.example.weatherapp.api.models

data class ApiResponse(
    val current: Current,
    val location: Location
)
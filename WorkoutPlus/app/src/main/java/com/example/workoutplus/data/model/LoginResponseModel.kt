package com.example.workoutplus.data.model

import com.google.gson.annotations.SerializedName

data class LoginResponseModel (
    val user: UserModel,
    val accessToken: String,
    val expirationTime: Int
)
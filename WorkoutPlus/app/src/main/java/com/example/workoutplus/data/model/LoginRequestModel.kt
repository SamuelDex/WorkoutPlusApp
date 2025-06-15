package com.example.workoutplus.data.model

import com.google.gson.annotations.SerializedName

data class LoginRequestModel (
    var email: String?,
    var userName: String?,
    var password: String?
)
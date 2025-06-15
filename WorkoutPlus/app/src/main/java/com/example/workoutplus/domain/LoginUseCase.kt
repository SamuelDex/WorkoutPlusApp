package com.example.workoutplus.domain

import com.example.workoutplus.data.WorkoutPlusRepository
import com.example.workoutplus.data.model.LoginRequestModel
import com.example.workoutplus.data.model.LoginResponseModel

class LoginUseCase() {
    private val repository = WorkoutPlusRepository()

    suspend operator fun invoke(login: LoginRequestModel): Result<LoginResponseModel> = repository.login(login)
}

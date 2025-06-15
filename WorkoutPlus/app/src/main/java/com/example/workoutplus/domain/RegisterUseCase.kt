package com.example.workoutplus.domain

import com.example.workoutplus.data.WorkoutPlusRepository
import com.example.workoutplus.data.model.LoginRequestModel
import com.example.workoutplus.data.model.LoginResponseModel
import com.example.workoutplus.data.model.UserModel

class RegisterUseCase {
    private val repository = WorkoutPlusRepository()

    suspend operator fun invoke(user: UserModel): Result<UserModel> = repository.postUser(user)
}
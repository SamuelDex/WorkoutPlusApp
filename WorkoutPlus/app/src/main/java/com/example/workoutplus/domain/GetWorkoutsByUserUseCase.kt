package com.example.workoutplus.domain

import com.example.workoutplus.data.WorkoutPlusRepository
import com.example.workoutplus.data.model.UserModel
import com.example.workoutplus.data.model.WorkoutModel

class GetWorkoutsByUserUseCase {
    private val repository = WorkoutPlusRepository()

    suspend operator fun invoke(userId: Int): Result<List<WorkoutModel>> = repository.getWorkoutsByUser(userId)
}
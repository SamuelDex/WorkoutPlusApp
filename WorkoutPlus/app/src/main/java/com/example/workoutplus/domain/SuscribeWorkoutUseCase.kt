package com.example.workoutplus.domain

import com.example.workoutplus.data.WorkoutPlusRepository
import com.example.workoutplus.data.model.MessageResponseModel

class SuscribeWorkoutUseCase {
    private val repository = WorkoutPlusRepository()

    suspend operator fun invoke(userId: Int, workoutId: Int): Result<MessageResponseModel> = repository.suscribeWorkout(userId, workoutId)
}
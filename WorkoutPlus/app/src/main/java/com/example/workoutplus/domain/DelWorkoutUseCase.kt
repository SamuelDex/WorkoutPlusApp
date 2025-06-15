package com.example.workoutplus.domain

import com.example.workoutplus.data.WorkoutPlusRepository
import com.example.workoutplus.data.model.MessageResponseModel

class DelWorkoutUseCase {
    private val repository = WorkoutPlusRepository()

    suspend operator fun invoke(id: Int): Result<MessageResponseModel> = repository.delWorkout(id)
}
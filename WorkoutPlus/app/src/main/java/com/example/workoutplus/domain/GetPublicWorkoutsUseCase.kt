package com.example.workoutplus.domain

import com.example.workoutplus.data.WorkoutPlusRepository
import com.example.workoutplus.data.model.WorkoutModel

class GetPublicWorkoutsUseCase {
    private val repository = WorkoutPlusRepository()

    suspend operator fun invoke(): Result<List<WorkoutModel>> = repository.getWorkouts()
}
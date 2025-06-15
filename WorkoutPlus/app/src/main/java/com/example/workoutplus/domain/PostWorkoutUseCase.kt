package com.example.workoutplus.domain

import com.example.workoutplus.data.WorkoutPlusRepository
import com.example.workoutplus.data.model.WorkoutModel

class PostWorkoutUseCase {
    private val repository = WorkoutPlusRepository()

    suspend operator fun invoke(workout: WorkoutModel): Result<WorkoutModel> = repository.postWorkout(workout)
}
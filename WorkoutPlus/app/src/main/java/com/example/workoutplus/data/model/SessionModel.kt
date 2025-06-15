package com.example.workoutplus.data.model

data class SessionModel (
    var id: Int?,
    var name: String?,
    var workoutId: Int?,
    var exercises: MutableList<ExerciseModel>?
)
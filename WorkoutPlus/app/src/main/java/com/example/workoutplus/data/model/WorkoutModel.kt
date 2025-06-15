package com.example.workoutplus.data.model

data class WorkoutModel (
    var id: Int?,
    var name: String,
    var description: String?,
    var frequency: Int?,
    var intensity: Int?,
    var userId: Int?,
    var sessions: MutableList<SessionModel>?
)
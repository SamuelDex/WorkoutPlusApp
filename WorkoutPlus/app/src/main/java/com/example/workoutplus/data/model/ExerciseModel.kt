package com.example.workoutplus.data.model

data class ExerciseModel (
    var id: Int?,
    var sets: Int?,
    var reps: Int?,
    var weight: Int?,
    var sessionId: Int?,
    var exerciseInfoId: Int?,
    var exerciseInfo: ExerciseInfoModel?
)
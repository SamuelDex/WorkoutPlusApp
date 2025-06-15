package com.example.workoutplus.data.model

import java.util.Date

data class DiaryEntryModel (
    val id: Int?,
    val date: Date?,
    val exercise: String?,
    val sets: Int?,
    val reps: Int?,
    val weight: Int?,
    val userId: Int?
)
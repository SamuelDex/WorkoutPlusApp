package com.example.workoutplus.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.workoutplus.data.model.ExerciseModel

class ExerciseFragmentAdapter(
    fa: FragmentActivity,
    private val exercises: List<ExerciseModel>,
    private val userId: Int
) : FragmentStateAdapter(fa) {
    override fun getItemCount() = exercises.size
    override fun createFragment(position: Int): Fragment =
        ExerciseFragment.newInstance(exercises[position], userId)
}
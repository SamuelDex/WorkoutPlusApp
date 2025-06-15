package com.example.workoutplus.ui.adapter.exercise

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutplus.R
import com.example.workoutplus.data.model.ExerciseModel

class ExerciseAdapter (private val onClickListener: (ExerciseModel) -> Unit): RecyclerView.Adapter<ExerciseViewHolder>() {

    private var exercises: List<ExerciseModel>

    init {
        exercises = listOf<ExerciseModel>()
    }

    fun setExercises(exercises: List<ExerciseModel>){
        this.exercises = exercises
        notifyDataSetChanged()
    }

    fun getExercises(): List<ExerciseModel>{
        return exercises
    }

    fun getId(position: Int): Int? {
        return exercises[position].id
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val layoutInfrater = LayoutInflater.from(parent.context)
        return ExerciseViewHolder(layoutInfrater.inflate(R.layout.viewholder_exercise, parent, false))
    }

    override fun getItemCount(): Int = exercises.size

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        holder.render(exercises[position], onClickListener)
    }

}
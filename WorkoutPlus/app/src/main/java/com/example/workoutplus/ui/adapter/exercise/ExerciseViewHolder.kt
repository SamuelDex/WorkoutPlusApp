package com.example.workoutplus.ui.adapter.exercise

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutplus.data.model.ExerciseModel
import com.example.workoutplus.databinding.ViewholderExerciseBinding

class ExerciseViewHolder(view: View): RecyclerView.ViewHolder(view) {

    val binding = ViewholderExerciseBinding.bind(view)


    fun render(exercise: ExerciseModel, onClickListener: (ExerciseModel) -> Unit){
        binding.name.text = exercise.exerciseInfo!!.name
        binding.sets.text = "Series: " + (exercise.sets?.let { it } ?: run { "Sin definir" })
        binding.reps.text = "Repeticiones : " + (exercise.reps?.let { it } ?: run { "Sin definir" })
        binding.weight.text = "Peso : " + (exercise.weight?.let { it } ?: run { "Sin definir" })

        itemView.setOnClickListener{ onClickListener(exercise) }
    }
}
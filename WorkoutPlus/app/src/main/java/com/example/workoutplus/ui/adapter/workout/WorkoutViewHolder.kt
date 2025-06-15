package com.example.workoutplus.ui.adapter.workout

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutplus.R
import com.example.workoutplus.data.model.WorkoutModel
import com.example.workoutplus.databinding.ViewholderWorkoutBinding

class WorkoutViewHolder(view: View): RecyclerView.ViewHolder(view) {

    val binding = ViewholderWorkoutBinding.bind(view)


    fun render(workout: WorkoutModel, onClickListener: (WorkoutModel) -> Unit){
        binding.name.text = workout.name
        binding.frecuency.text = "Frecuencia: " + (workout.frequency?.let { it } ?: run { "Sin definir" })
        binding.intensity.text = "Intensidad: " + (workout.intensity?.let { it.toString() + "%" } ?: run { "Sin definir" })

        itemView.setOnClickListener{ onClickListener(workout) }
    }
}
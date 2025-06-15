package com.example.workoutplus.ui.adapter.diary

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutplus.data.model.DiaryEntryModel
import com.example.workoutplus.databinding.ViewholderExerciseBinding

class DiaryEntryViewHolder (view: View): RecyclerView.ViewHolder(view) {

    val binding = ViewholderExerciseBinding.bind(view)


    fun render(entry: DiaryEntryModel){
        binding.name.text = entry.exercise
        binding.sets.text = "Series: " + (entry.sets?.let { it } ?: run { "Sin definir" })
        binding.reps.text = "Repeticiones : " + (entry.reps?.let { it } ?: run { "Sin definir" })
        binding.weight.text = "Peso : " + (entry.weight?.let { it } ?: run { "Sin definir" })

    }
}
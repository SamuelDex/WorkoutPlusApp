package com.example.workoutplus.ui.adapter.diary

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutplus.R
import com.example.workoutplus.data.model.DiaryEntryModel
import com.example.workoutplus.data.model.ExerciseModel
import com.example.workoutplus.ui.adapter.exercise.ExerciseViewHolder

class DiaryEntryAdapter(): RecyclerView.Adapter<DiaryEntryViewHolder>() {

    private var entries: List<DiaryEntryModel>

    init {
        entries = listOf<DiaryEntryModel>()
    }

    fun setEtries(entries: List<DiaryEntryModel>){
        this.entries = entries
        notifyDataSetChanged()
    }

    fun getEtries(): List<DiaryEntryModel>{
        return entries
    }

    fun getId(position: Int): Int? {
        return entries[position].id
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryEntryViewHolder {
        val layoutInfrater = LayoutInflater.from(parent.context)
        return DiaryEntryViewHolder(layoutInfrater.inflate(R.layout.viewholder_exercise, parent, false))
    }

    override fun getItemCount(): Int = entries.size

    override fun onBindViewHolder(holder: DiaryEntryViewHolder, position: Int) {
        holder.render(entries[position])
    }

}
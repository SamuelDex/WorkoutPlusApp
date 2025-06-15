package com.example.workoutplus.ui.adapter.workout

import android.content.DialogInterface.OnClickListener
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutplus.R
import com.example.workoutplus.data.model.WorkoutModel

class WorkoutAdapter(private val onClickListener: (WorkoutModel) -> Unit): RecyclerView.Adapter<WorkoutViewHolder>() {

    private var workoutList: List<WorkoutModel>

    init {
        workoutList = listOf<WorkoutModel>()
    }

    fun setWorkoutList(workoutList: List<WorkoutModel>){
        this.workoutList = workoutList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        val layoutInfrater = LayoutInflater.from(parent.context)
        return WorkoutViewHolder(layoutInfrater.inflate(R.layout.viewholder_workout, parent, false))
    }

    override fun getItemCount(): Int = workoutList.size

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        holder.render(workoutList[position], onClickListener)
    }

    fun getId(position: Int): Int {
        return workoutList[position].id!!
    }
}
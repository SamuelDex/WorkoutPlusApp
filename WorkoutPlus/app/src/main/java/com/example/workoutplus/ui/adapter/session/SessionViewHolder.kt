package com.example.workoutplus.ui.adapter.session

import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutplus.data.model.SessionModel
import com.example.workoutplus.data.model.WorkoutModel
import com.example.workoutplus.databinding.ViewholderSessionBinding

class SessionViewHolder(view: View): RecyclerView.ViewHolder(view) {

    val binding = ViewholderSessionBinding.bind(view)


    fun render(session: SessionModel, onItemSelectedListener: (SessionModel) -> Unit, onPlayClickListener: (SessionModel) -> Unit){
        binding.name.text = session.name

        itemView.setOnClickListener{ onItemSelectedListener(session) }

        session.exercises?.let {
            session.id?.let {
                binding.play.setOnClickListener{ onPlayClickListener(session) }
            }?: kotlin.run {
                binding.play.isVisible = false
            }
        }


    }
}
package com.example.workoutplus.ui.adapter.session

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutplus.R
import com.example.workoutplus.data.model.SessionModel

class SessionAdapter (
    private val onItemSelectedListener: (SessionModel) -> Unit,
    private val onPlayClickListener: (SessionModel) -> Unit
): RecyclerView.Adapter<SessionViewHolder>() {

    private var sessions: List<SessionModel>

    init {
        sessions = listOf<SessionModel>()
    }

    fun setSessions(sessionList: List<SessionModel>){
        this.sessions = sessionList
        notifyDataSetChanged()
    }

    fun getSessions(): List<SessionModel>{
        return sessions
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessionViewHolder {
        val layoutInfrater = LayoutInflater.from(parent.context)
        return SessionViewHolder(layoutInfrater.inflate(R.layout.viewholder_session, parent, false))
    }

    override fun getItemCount(): Int = sessions.size

    override fun onBindViewHolder(holder: SessionViewHolder, position: Int) {
        holder.render(sessions[position], onItemSelectedListener, onPlayClickListener)
    }

    fun getId(position: Int): Int? {
        return sessions[position].id
    }
}
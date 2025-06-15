package com.example.workoutplus.ui.adapter.exercisedata

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutplus.R
import com.example.workoutplus.data.model.ExerciseInfoModel
import com.example.workoutplus.databinding.ViewholderExerciseInfoBinding

class ExerciseInfoAdapter(private val onItemSelected: (ExerciseInfoModel) -> Unit): RecyclerView.Adapter<ExerciseInfoAdapter.ExerciseDetailViewHolder>() {

    private var exercises: List<ExerciseInfoModel>
    private var selectedPosition = -1

    inner class ExerciseDetailViewHolder (view: View): RecyclerView.ViewHolder(view) {

        val binding = ViewholderExerciseInfoBinding.bind(view)


        fun bind(exercise: ExerciseInfoModel){
            binding.name.text = exercise.name
            binding.category.text = exercise.category

            binding.checkBox.isChecked = (selectedPosition == adapterPosition)

            itemView.setOnClickListener {
                val previousPosition = selectedPosition
                selectedPosition = layoutPosition

                notifyItemChanged(previousPosition)
                notifyItemChanged(selectedPosition)

                onItemSelected(exercise)
            }
        }
    }

    init {
        exercises = listOf<ExerciseInfoModel>()
    }

    fun setExercises(exercises: List<ExerciseInfoModel>, selectedExercise: ExerciseInfoModel? = null) {
        this.exercises = exercises
        selectedPosition = selectedExercise?.let {
            exercises.indexOfFirst { it.id == selectedExercise.id }
        } ?: -1

        notifyDataSetChanged()
    }


    fun getId(position: Int): Int? {
        return exercises[position].id
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseDetailViewHolder {
        val layoutInfrater = LayoutInflater.from(parent.context)
        return ExerciseDetailViewHolder(layoutInfrater.inflate(R.layout.viewholder_exercise_info, parent, false))
    }

    override fun getItemCount(): Int = exercises.size

    override fun onBindViewHolder(holder: ExerciseDetailViewHolder, position: Int) {
        holder.bind(exercises[position])
    }

}
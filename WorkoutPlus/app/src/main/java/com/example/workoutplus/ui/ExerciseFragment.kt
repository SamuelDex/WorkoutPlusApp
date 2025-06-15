package com.example.workoutplus.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.example.workoutplus.data.model.DiaryEntryModel
import com.example.workoutplus.data.model.ExerciseModel
import com.example.workoutplus.databinding.FragmentExerciseBinding
import java.util.Date

class ExerciseFragment : Fragment() {

    private lateinit var binding: FragmentExerciseBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExerciseBinding.inflate(inflater, container, false)

        binding.exercise.setText(arguments?.getString(ARG_EXERCISE) ?: "")
        binding.sets.setText(arguments?.getInt(ARG_SETS).toString() ?: "")
        binding.reps.setText(arguments?.getInt(ARG_REPS).toString() ?: "")
        binding.weight.setText(arguments?.getInt(ARG_WEIGHT).toString() ?: "")

        binding.button.setOnClickListener {
            val entry = DiaryEntryModel(
                null,
                Date(),
                arguments?.getString(ARG_EXERCISE) ?: "",
                binding.sets.text.toString().toIntOrNull(),
                binding.reps.text.toString().toIntOrNull(),
                binding.weight.text.toString().toIntOrNull(),
                arguments?.getInt(ARG_USER_ID)
            )
            (activity as? PlaySessionActivity)?.postEntry(entry)
        }

        return binding.root
    }

    companion object {
        private const val ARG_EXERCISE = "exercise"
        private const val ARG_SETS = "sets"
        private const val ARG_REPS = "reps"
        private const val ARG_WEIGHT = "weight"
        private const val ARG_USER_ID = "userId"

        @JvmStatic
        fun newInstance(exercise: ExerciseModel, userId: Int): ExerciseFragment {
            val fragment = ExerciseFragment()
            fragment.arguments = bundleOf(
                ARG_EXERCISE to exercise.exerciseInfo!!.name,
                ARG_SETS to exercise.sets,
                ARG_REPS to exercise.reps,
                ARG_WEIGHT to exercise.weight,
                ARG_USER_ID to userId
            )
            return fragment
        }
    }
}
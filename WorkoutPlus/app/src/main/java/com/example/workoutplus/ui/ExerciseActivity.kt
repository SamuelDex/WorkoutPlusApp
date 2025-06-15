package com.example.workoutplus.ui

import android.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.workoutplus.data.model.ExerciseInfoModel
import com.example.workoutplus.data.model.ExerciseModel
import com.example.workoutplus.databinding.ActivityExerciseBinding
import com.example.workoutplus.ui.adapter.exercisedata.ExerciseInfoAdapter
import com.google.gson.Gson

class ExerciseActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityExerciseBinding
    private val workoutViewModel: WorkoutViewModel by viewModels()
    private val UPDATE_CODE = 200
    private val CREATE_CODE = 201
    private var selectedPosition = 0
    private lateinit var adapter: ExerciseInfoAdapter
    private var exercise = ExerciseModel(null, null, null, null, null, null, null)
    private var exerciseInfo = ExerciseInfoModel(null, null, null, null, null)
    private var isUpdate = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.save.setOnClickListener(this)
        binding.searchButton.setOnClickListener(this)

        workoutViewModel.responseMessage.observe(this, Observer {
            showMessage(it.message)
        })

        workoutViewModel.errorMessage.observe(this, Observer {
            showMessage(it)
        })

        workoutViewModel.exerciseInfoList.observe(this, Observer {
            adapter.setExercises(it, exerciseInfo)
        })

        initSpinner()
        initRecyclerView()
        checkIsUpdate()
        workoutViewModel.getExerciseInfo()

    }

    private fun initSpinner() {
        val categories = listOf("Categoria", "Pecho", "Bíceps", "Tríceps", "Piernas", "Espalda", "Hombros", "Core")

        val adapter = ArrayAdapter(this, R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.category.adapter = adapter

        binding.category.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                if(position != 0){
                    binding.searchInput.setText("")
                    val selectedItem = categories[position]
                    workoutViewModel.getExerciseInfoByCategory(selectedItem)
                }else{
                    workoutViewModel.getExerciseInfo()
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                workoutViewModel.getExerciseInfo()
            }
        }
    }

    private fun checkIsUpdate() {
        isUpdate = intent.getBooleanExtra("isUpdate", false)
        Log.e("isUpdate", isUpdate.toString())
        if (isUpdate){
            val result = intent.getStringExtra("exercise")
            val gson = Gson()
            exercise = gson.fromJson(result, ExerciseModel::class.java)
            binding.sets.setText(exercise.sets.toString())
            binding.reps.setText(exercise.reps.toString())
            binding.weight.setText(exercise.weight.toString())
            exerciseInfo = exercise.exerciseInfo!!
        }
    }

    private fun initRecyclerView() {
        val recyclerView = binding.recyclerExerciseInfo
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ExerciseInfoAdapter() { onItemSelected(it) }
        recyclerView.adapter = adapter

    }

    private fun onItemSelected(it: ExerciseInfoModel) {
        exerciseInfo = it
        Log.e("Exercicio seleccionado: ", it.toString())
    }

    private fun showMessage(s: String?) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }

    override fun onClick(v: View?) {
        when(v){
            binding.save -> {
                exercise.sets = binding.sets.text.toString().toIntOrNull()
                exercise.reps = binding.reps.text.toString().toIntOrNull()
                exercise.weight = binding.weight.text.toString().toIntOrNull()
                exercise.exerciseInfo = exerciseInfo
                val gson = Gson()
                val exerciseJson = gson.toJson(exercise)
                intent.putExtra("exercise", exerciseJson)
                if(isUpdate){
                    setResult(UPDATE_CODE, intent)
                }else {
                    setResult(CREATE_CODE, intent)
                }
                finish()
            }
            binding.searchButton -> {
                val searchTerm = binding.searchInput.text.toString()
                if(!searchTerm.isNullOrEmpty()){
                    workoutViewModel.getExerciseInfoByName(searchTerm)
                }
            }
        }
    }
}
package com.example.workoutplus.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutplus.data.model.ExerciseModel
import com.example.workoutplus.data.model.SessionModel
import com.example.workoutplus.databinding.ActivitySessionBinding
import com.example.workoutplus.ui.adapter.exercise.ExerciseAdapter
import com.google.gson.Gson

class SessionActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivitySessionBinding
    private val workoutViewModel: WorkoutViewModel by viewModels()
    private val UPDATE_CODE = 200
    private val CREATE_CODE = 201
    private lateinit var adapter: ExerciseAdapter
    private lateinit var exerciseToUpdate: ExerciseModel
    private var session = SessionModel(null, null, null, null)
    private var isUpdate = false
    private val exerciseLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        when(it.resultCode){
            CREATE_CODE -> {
                val result = it.data?.getStringExtra("exercise")
                result?.let {
                    val gson = Gson()
                    val newExercise = gson.fromJson(result, ExerciseModel::class.java)
                    newExercise.exerciseInfoId = newExercise.exerciseInfo?.id
                    val exercises = adapter.getExercises().toMutableList()
                    exercises.add(newExercise)
                    adapter.setExercises(exercises)
                }?: kotlin.run {
                    showMessage("No se ha podido recuperar la sesión creada")
                }
            }
            UPDATE_CODE -> {
                val result = it.data?.getStringExtra("exercise")
                Log.e("Exercise despues de la actualizacion: ", result!!)
                result?.let {
                    val gson = Gson()
                    val updatedExercise = gson.fromJson(result, ExerciseModel::class.java)
                    updatedExercise.exerciseInfoId = updatedExercise.exerciseInfo?.id
                    updatedExercise.id?.let {
                        workoutViewModel.updateExercise(updatedExercise)
                    }?: kotlin.run {
                        val sessions = adapter.getExercises().toMutableList()
                        sessions.replaceAll { if (it == exerciseToUpdate) updatedExercise else it }
                        adapter.setExercises(sessions)
                    }

                }?: kotlin.run {
                    showMessage("No se ha podido recuperar la sesion")
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySessionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addExercise.setOnClickListener(this)
        binding.save.setOnClickListener(this)

        workoutViewModel.responseMessage.observe(this, Observer {
            showMessage(it.message)
        })

        workoutViewModel.errorMessage.observe(this, Observer {
            showMessage(it)
        })

        workoutViewModel.updatedExercise.observe(this, Observer {
            val exercises = adapter.getExercises().toMutableList()
            val updatedIndex = exercises.indexOf(exerciseToUpdate)
            exercises[updatedIndex] = it
            adapter.setExercises(exercises)
        })

        initRecyclerView()

        checkIsUpdate()
    }

    private fun initRecyclerView() {
        val recyclerView = binding.recyclerExercise
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ExerciseAdapter() { onItemSelected(it) }
        recyclerView.adapter = adapter

        val itemSwipe = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if (binding.recyclerExercise.isEnabled) {
                    val position = viewHolder.adapterPosition
                    val id = adapter.getId(position)
                    confirmDialog(id, position)
                } else {
                    binding.recyclerExercise.adapter?.notifyItemChanged(viewHolder.position)
                }
            }
        }
        val swap = ItemTouchHelper(itemSwipe)
        swap.attachToRecyclerView(binding.recyclerExercise)
    }


    private fun confirmDialog(id: Int?, position: Int) {
        val builder = android.app.AlertDialog.Builder(this)
        builder.setMessage("¿Desea eliminar este ejercicio?")
            .setTitle("Eliminar")
            .setPositiveButton("Confirmar") { dialog, which ->
                session.exercises!!.removeIf { it.id == id }
                adapter.setExercises(session.exercises!!)
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") {  dialog, which ->
                binding.recyclerExercise.adapter?.notifyItemChanged(position)
            }
        builder.show()
    }

    private fun onItemSelected(exercise: ExerciseModel) {
        exerciseToUpdate = exercise
        val gson = Gson()
        val exerciseJson = gson.toJson(exercise)
        Log.e("Exercise antes de la actualizacion: ", exerciseJson)
        val intent = Intent(this, ExerciseActivity::class.java)
        intent.putExtra("isUpdate", true)
        intent.putExtra("exercise", exerciseJson)
        exerciseLauncher.launch(intent)
    }

    private fun checkIsUpdate() {
        isUpdate = intent.getBooleanExtra("isUpdate", false)
        if (isUpdate){
            val result = intent.getStringExtra("session")
            val gson = Gson()
            session = gson.fromJson(result, SessionModel::class.java)
            binding.name.setText(session.name)
            session.exercises?.let { adapter.setExercises(it) }
        }
    }

    private fun showMessage(s: String?) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }

    override fun onClick(v: View?) {
        when(v){
            binding.addExercise -> {
                val intent = Intent(this, ExerciseActivity::class.java)
                intent.putExtra("isUpdate", false)
                exerciseLauncher.launch(intent)
            }
            binding.save -> {
                session.name = binding.name.text.toString()
                session.exercises = adapter.getExercises().toMutableList()
                val gson = Gson()
                val sessionJson = gson.toJson(session)
                intent.putExtra("session", sessionJson)
                if(isUpdate){
                    setResult(UPDATE_CODE, intent)
                }else {
                    setResult(CREATE_CODE, intent)
                }
                finish()
            }
        }
    }
}
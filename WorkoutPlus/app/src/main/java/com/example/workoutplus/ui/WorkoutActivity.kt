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
import com.example.workoutplus.data.model.SessionModel
import com.example.workoutplus.data.model.WorkoutModel
import com.example.workoutplus.databinding.ActivityWorkoutBinding
import com.example.workoutplus.ui.adapter.session.SessionAdapter
import com.google.gson.Gson

class WorkoutActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityWorkoutBinding
    private val workoutViewModel: WorkoutViewModel by viewModels()
    private var workout = WorkoutModel(null, "", null, null, null, null, mutableListOf<SessionModel>() )
    private var isUpdate = false
    private val UPDATE_CODE = 200
    private val CREATE_CODE = 201
    private lateinit var adapter: SessionAdapter
    private lateinit var sessionToUpdate: SessionModel
    private val sessionLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        when(it.resultCode){
            CREATE_CODE -> {
                val result = it.data?.getStringExtra("session")
                result?.let {
                    val gson = Gson()
                    val newSession = gson.fromJson(result, SessionModel::class.java)
                    val sessions = adapter.getSessions().toMutableList()
                    sessions.add(newSession)
                    adapter.setSessions(sessions)
                }?: kotlin.run {
                    showMessage("No se ha podido recuperar la sesión creada")
                }
            }
            UPDATE_CODE -> {
                val result = it.data?.getStringExtra("session")
                result?.let {
                    val gson = Gson()
                    val updatedSession = gson.fromJson(result, SessionModel::class.java)
                    updatedSession.id?.let {
                        workoutViewModel.updateSession(updatedSession)
                    }?: kotlin.run {
                        val sessions = adapter.getSessions().toMutableList()
                        sessions.replaceAll { if (it == sessionToUpdate) updatedSession else it }
                        adapter.setSessions(sessions)
                    }
                }?: kotlin.run {
                    showMessage("No se ha podido recuperar la rutina")
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.save.setOnClickListener(this)
        binding.newSession.setOnClickListener(this)

        workoutViewModel.responseMessage.observe(this, Observer {
            showMessage(it.message)
        })

        workoutViewModel.errorMessage.observe(this, Observer {
            showMessage(it)
        })

        workoutViewModel.updatedSession.observe(this, Observer { updatedSession ->
            val sessions = adapter.getSessions().toMutableList()

            val deletedExercises = sessionToUpdate.exercises!!.toSet() - updatedSession.exercises!!.toSet()

            for (exercise in deletedExercises){
                exercise.id?.let { workoutViewModel.delExercise(it) }
            }

            val updatedIndex = sessions.indexOf(sessionToUpdate)
            sessions[updatedIndex] = updatedSession
            adapter.setSessions(sessions)
        })

        workoutViewModel.workout.observe(this, Observer {
            workout = it
        })

        initRecyclerView()

        checkIsUpdate()

    }

    private fun initRecyclerView() {
        val recyclerView = binding.recyclerSession
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = SessionAdapter({ onItemSelected(it) }, { onPlayClick(it) })
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
                if (binding.recyclerSession.isEnabled) {
                    val position = viewHolder.adapterPosition
                    val id = adapter.getId(position)
                    confirmDialog(id, position)
                } else {
                    binding.recyclerSession.adapter?.notifyItemChanged(viewHolder.position)
                }
            }
        }
        val swap = ItemTouchHelper(itemSwipe)
        swap.attachToRecyclerView(binding.recyclerSession)
    }

    private fun confirmDialog(id: Int?, position: Int) {
        val builder = android.app.AlertDialog.Builder(this)
        builder.setMessage("¿Desea eliminar esta sesión?")
            .setTitle("Eliminar")
            .setPositiveButton("Confirmar") { dialog, which ->
                id?.let { workoutViewModel.delSession(it) }
                workout.sessions!!.removeIf { it.id == id }
                adapter.setSessions(workout.sessions!!)
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") {  dialog, which ->
                binding.recyclerSession.adapter?.notifyItemChanged(position)
            }
        builder.show()
    }

    private fun onPlayClick(session: SessionModel){
        sessionToUpdate = session
        val gson = Gson()
        val sessionJson = gson.toJson(session)
        val intent = Intent(this, PlaySessionActivity::class.java)
        intent.putExtra("session", sessionJson)
        startActivity(intent)
    }

    private fun onItemSelected(session: SessionModel) {
        sessionToUpdate = session
        val gson = Gson()
        val sessionJson = gson.toJson(session)
        val intent = Intent(this, SessionActivity::class.java)
        intent.putExtra("isUpdate", true)
        intent.putExtra("session", sessionJson)
        sessionLauncher.launch(intent)
    }

    private fun checkIsUpdate() {
        isUpdate = intent.getBooleanExtra("isUpdate", false)
        Log.e("isUpdate", isUpdate.toString())
        if (isUpdate){
            val result = intent.getStringExtra("workout")
            Log.e("Recibido en elintent", result!!)
            val gson = Gson()
            workout = gson.fromJson(result, WorkoutModel::class.java)
            binding.name.setText(workout.name)
            workout.frequency?.let { binding.frecuency.setText(it.toString()) }?: kotlin.run { binding.frecuency.setHint("Frecuencia") }
            workout.intensity?.let { binding.intensity.setText(it.toString()) }?: kotlin.run { binding.intensity.setHint("Intensidad") }
            workout.sessions?.let { adapter.setSessions(it) }
        }
    }

    override fun onClick(v: View?) {
        when(v){
            binding.save -> {
                workout.name = binding.name.text.toString()
                workout.frequency = binding.frecuency.text.toString().toIntOrNull()
                workout.intensity = binding.intensity.text.toString().toIntOrNull()
                workout.sessions = adapter.getSessions().toMutableList()
                val gson = Gson()
                val workoutJson = gson.toJson(workout)
                intent.putExtra("workout", workoutJson)
                Log.e("workout desde el intent", workoutJson)
                if(isUpdate){
                    setResult(UPDATE_CODE, intent)
                }else {
                    setResult(CREATE_CODE, intent)
                }
                finish()
            }
            binding.newSession -> {
                val intent = Intent(this, SessionActivity::class.java)
                intent.putExtra("isUpdate", false)
                sessionLauncher.launch(intent)
            }
        }
    }

    private fun showMessage(s: String?) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        workout.id?.let { workoutViewModel.getWorkoutById(it) }
    }
}
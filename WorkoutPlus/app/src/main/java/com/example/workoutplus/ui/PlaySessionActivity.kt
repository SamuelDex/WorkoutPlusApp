package com.example.workoutplus.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.workoutplus.data.model.DiaryEntryModel
import com.example.workoutplus.data.model.LoginResponseModel
import com.example.workoutplus.data.model.SessionModel
import com.example.workoutplus.databinding.ActivityPlaySessionBinding
import com.google.gson.Gson

class PlaySessionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlaySessionBinding
    private val workoutViewModel: WorkoutViewModel by viewModels()
    private lateinit var adapter: ExerciseFragmentAdapter
    private lateinit var session: SessionModel
    private lateinit var loginResponseModel: LoginResponseModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlaySessionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        workoutViewModel.errorMessage.observe(this, Observer {
            showMessage(it)
        })

        workoutViewModel.responseMessage.observe(this, Observer {
            if (binding.viewPager.currentItem < adapter.itemCount - 1) {
                binding.viewPager.currentItem++
            } else {
                showMessage("¡Fin de la rutina!")
                finish()
            }
        })

        getSessionFromIntent()
        getUserFromPreferences()

        adapter = ExerciseFragmentAdapter(this, session.exercises!!, loginResponseModel.user.id!!)
        binding.viewPager.adapter = adapter
        binding.viewPager.isUserInputEnabled = false
    }

    private fun getSessionFromIntent() {
        val result = intent.getStringExtra("session")
        val gson = Gson()
        session = gson.fromJson(result, SessionModel::class.java)
        binding.session.setText(session.name)
    }

    private fun getUserFromPreferences() {
        val prefs = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = prefs.getString("usuario", null)

        if (json != null) {
            loginResponseModel = gson.fromJson(json, LoginResponseModel::class.java)
            workoutViewModel.getWorkoutsByUser(loginResponseModel.user.id!!)
        }else
            showMessage("No se ha podido recuperar el usuario")
    }

    fun postEntry(entry: DiaryEntryModel) {
        workoutViewModel.postDiaryEntry(entry)
    }

    private fun showMessage(s: String?) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }
}
package com.example.workoutplus.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutplus.R
import com.example.workoutplus.data.model.LoginResponseModel
import com.example.workoutplus.data.model.WorkoutModel
import com.example.workoutplus.databinding.FragmentWorkoutPannelBinding
import com.example.workoutplus.ui.adapter.workout.WorkoutAdapter
import com.google.gson.Gson

class WorkoutPannelFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentWorkoutPannelBinding
    private final val UPDATE_CODE = 200
    private final val CREATE_CODE = 201
    private lateinit var workoutList: List<WorkoutModel>
    private lateinit var publicWorkoutList: List<WorkoutModel>
    private lateinit var loginResponseModel: LoginResponseModel
    private lateinit var adapter: WorkoutAdapter
    private val workoutViewModel: WorkoutViewModel by viewModels()
    private val workoutLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        when(it.resultCode){
            CREATE_CODE -> {
                val result = it.data?.getStringExtra("workout")
                Log.e("create", result!!)
                result?.let {
                    val gson = Gson()
                    val newWorkout = gson.fromJson(result, WorkoutModel::class.java)
                    newWorkout.userId = loginResponseModel.user.id
                    Log.e("new workout", newWorkout.toString())
                    workoutViewModel.postWorkout(newWorkout)
                }?: kotlin.run {
                    showMessage("No se ha podido recuperar la rutina")
                }
            }
            UPDATE_CODE -> {
                val result = it.data?.getStringExtra("workout")
                result?.let {
                    val gson = Gson()
                    val updatedWorkout = gson.fromJson(result, WorkoutModel::class.java)
                    workoutViewModel.updateWorkout(updatedWorkout)
                    Log.e("2222w", updatedWorkout.toString())


                }?: kotlin.run {
                    showMessage("No se ha podido recuperar la rutina")
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWorkoutPannelBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()

        workoutViewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            showMessage(it)
        })

        workoutViewModel.workoutList.observe(viewLifecycleOwner, Observer {
            workoutList = it
            adapter.setWorkoutList(workoutList)
            Log.e("1234", "getList")
        })

        workoutViewModel.publicWorkoutList.observe(viewLifecycleOwner, Observer {
            publicWorkoutList = it
            showPublicWorkoutDialog()
        })

        workoutViewModel.responseMessage.observe(viewLifecycleOwner, Observer {
            showMessage(it.message)
        })

        getUserFromPreferences()

        binding.publicWorkoutLayout.setOnClickListener(this)
        binding.postWorkout.setOnClickListener(this)
    }

    private fun showPublicWorkoutDialog() {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_public_workout, null)

        val recyclerView = dialogView.findViewById<RecyclerView>(R.id.recyclerViewDialog)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val publicWorkoutAdapter = WorkoutAdapter() { onWorkoutSelected(it) }
        publicWorkoutAdapter.setWorkoutList(publicWorkoutList)
        recyclerView.adapter = publicWorkoutAdapter

        builder.setView(dialogView)
            .setTitle("Lista de rutinas publicas")
            .setNegativeButton("Cerrar") { dialog, _ -> dialog.dismiss() }
            .show()

    }

    private fun getUserFromPreferences() {
        val prefs = requireContext().getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = prefs.getString("usuario", null)

        if (json != null) {
            loginResponseModel = gson.fromJson(json, LoginResponseModel::class.java)
            workoutViewModel.getWorkoutsByUser(loginResponseModel.user.id!!)
        }else
            showMessage("No se ha podido recuperar el usuario")
    }

    private fun initRecyclerView(){
        val recyclerView = binding.recyclerWorkout
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = WorkoutAdapter() { onWorkoutSelected(it) }
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
                if (binding.recyclerWorkout.isEnabled) {
                    val position = viewHolder.adapterPosition
                    val id = adapter.getId(position)
                    confirmDialog(id, position)
                } else {
                    binding.recyclerWorkout.adapter?.notifyItemChanged(viewHolder.position)
                }
            }
        }
        val swap = ItemTouchHelper(itemSwipe)
        swap.attachToRecyclerView(binding.recyclerWorkout)
    }

    private fun confirmDialog(id: Int, position: Int) {
        val builder = android.app.AlertDialog.Builder(requireContext())
        builder.setMessage("¿Desea eliminar esta rutina?")
            .setTitle("Eliminar")
            .setPositiveButton("Confirmar") { dialog, which ->
                delete(id)
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") {  dialog, which ->
                binding.recyclerWorkout.adapter?.notifyItemChanged(position)
            }
        builder.show()
    }

    private fun delete(id: Int) {
        workoutViewModel.delWorkouts(loginResponseModel.user.id!!, id)
    }


    private fun onWorkoutSelected(workout: WorkoutModel){
        Log.e("1111w", workout.toString())

        if (workout.userId == null){
            workoutViewModel.suscribeWorkouts(loginResponseModel.user.id!!, workout.id!!)
        }else {
            val gson = Gson()
            val workoutJson = gson.toJson(workout)
            val intent = Intent(requireContext(), WorkoutActivity::class.java)
            intent.putExtra("isUpdate", true)
            intent.putExtra("workout", workoutJson)
            workoutLauncher.launch(intent)
        }
    }

    private fun showMessage(s: String?) {
        Toast.makeText(requireContext(), s, Toast.LENGTH_SHORT).show()
    }

    override fun onClick(v: View?) {
        when(v){
            binding.publicWorkoutLayout -> workoutViewModel.getPublicWorkouts()
            binding.postWorkout -> {
                val intent = Intent(requireContext(), WorkoutActivity::class.java)
                intent.putExtra("isUpdate", false)
                workoutLauncher.launch(intent)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        workoutViewModel.getWorkoutsByUser(loginResponseModel.user.id!!)
    }
}
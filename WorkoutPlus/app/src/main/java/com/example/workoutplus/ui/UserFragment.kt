package com.example.workoutplus.ui

import android.content.Context
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.workoutplus.R
import com.example.workoutplus.data.model.LoginResponseModel
import com.example.workoutplus.data.model.UserModel
import com.example.workoutplus.databinding.FragmentUserBinding
import com.example.workoutplus.databinding.FragmentWorkoutPannelBinding
import com.google.gson.Gson

class UserFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentUserBinding
    private val workoutViewModel: WorkoutViewModel by viewModels()
    private lateinit var loginResponseModel: LoginResponseModel
    private lateinit var user: UserModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prefs = requireContext().getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = prefs.getString("usuario", null)

        if (json != null) {
            loginResponseModel = gson.fromJson(json, LoginResponseModel::class.java)
            user = loginResponseModel.user
        }else
            showMessage("No se ha podido recuperar el usuario")

        binding.user.setText(user.userName)
        binding.email.setText(user.email)

        binding.save.setOnClickListener(this)

        workoutViewModel.user.observe(viewLifecycleOwner, Observer {
            user = it
            showMessage("Información del usuario actualizada")
        })

        workoutViewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            showMessage(it)
        })
    }

    private fun showMessage(s: String?) {
        Toast.makeText(requireContext(), s, Toast.LENGTH_SHORT).show()
    }

    override fun onClick(v: View?) {
        var hasChanged = false

        if(binding.user.text.toString().isNotEmpty() && !user.userName.equals(binding.user.text.toString())) {
            user.userName = binding.user.text.toString()
            hasChanged = true
        }

        if(binding.email.text.toString().isNotEmpty() && !user.email.equals(binding.email.text.toString())) {
            if (Patterns.EMAIL_ADDRESS.matcher(binding.email.text.toString()).matches()){
                user.email = binding.email.text.toString()
                hasChanged = true
            }else
                showMessage("Email invalido")
        }

        if(binding.password.text.toString().isNotEmpty() && binding.confirmPassword.text.toString().isNotEmpty()) {
            if (binding.password.text.toString().equals(binding.confirmPassword.text.toString())){
                if (!user.password.equals(binding.password.text.toString())){
                    user.password = binding.password.text.toString()
                    hasChanged = true
                }else{
                    showMessage("La nueva contraseña no puede ser la misma que la antigua")
                }
            }else{
                showMessage("La contraseña no coincide con la confirmacion")
            }
        }

        if (hasChanged) {
            val accesToken = "Bearer ${loginResponseModel.accessToken}"
            workoutViewModel.updateUser(
                accesToken,
                user
            )
        }
    }
}
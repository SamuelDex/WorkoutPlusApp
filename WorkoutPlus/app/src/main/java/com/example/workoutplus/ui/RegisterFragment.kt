package com.example.workoutplus.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.workoutplus.R
import com.example.workoutplus.data.model.LoginRequestModel
import com.example.workoutplus.data.model.UserModel
import com.example.workoutplus.databinding.FragmentLoginBinding
import com.example.workoutplus.databinding.FragmentRegisterBinding
import com.google.gson.Gson

class RegisterFragment : Fragment(), View.OnClickListener {

    private val workoutViewModel: WorkoutViewModel by viewModels()
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        workoutViewModel.user.observe(viewLifecycleOwner, Observer {
            val login = LoginRequestModel(it.email, null, it.password)
            workoutViewModel.login(login)

        })

        workoutViewModel.loginResponseModel.observe(viewLifecycleOwner, Observer {
            val prefs = requireContext().getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
            val editor = prefs.edit()
            val gson = Gson()
            val json = gson.toJson(it)
            editor.putString("user", json)
            editor.apply()

            startActivity(Intent(requireContext(), NavigationActivity::class.java))
            requireActivity().finish()
        })

        workoutViewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            showMessage(it.toString())
        })

        binding.button.setOnClickListener(this)
    }

    private fun showMessage(s: String?) {
        Toast.makeText(requireContext(), s, Toast.LENGTH_SHORT).show()
    }

    fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
        return emailRegex.matches(email)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View?) {
        var user = UserModel(null, null, null, null, null, null)

        if (!binding.name.text.isEmpty())
            user.name = binding.user.text.toString()
        else
            showMessage("Nombre invalido")

        if (!binding.surname.text.isEmpty())
            user.surname = binding.user.text.toString()
        else
            showMessage("Apellido invalido")

        if (Patterns.EMAIL_ADDRESS.matcher(binding.email.text.toString()).matches())
            user.email = binding.email.text.toString()
        else
            showMessage("Correo invalido")

        if (!binding.user.text.isEmpty())
            user.userName = binding.user.text.toString()
        else
            showMessage("Usuario invalido")

        if (!binding.password.text.isEmpty())
            user.password = binding.password.text.toString()
        else
            showMessage("Contraseña invalida")

        if (!user.email.isNullOrEmpty() && !user.userName.isNullOrEmpty() && !user.password.isNullOrEmpty() && !user.name.isNullOrEmpty() && !user.surname.isNullOrEmpty())
            workoutViewModel.register(user)
    }

}
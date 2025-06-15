package com.example.workoutplus.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.example.workoutplus.databinding.ActivityMainBinding
import com.example.workoutplus.databinding.FragmentLoginBinding
import com.google.gson.Gson

class LoginFragment : Fragment(), View.OnClickListener {

    private val workoutViewModel: WorkoutViewModel by viewModels()
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        workoutViewModel.loginResponseModel.observe(viewLifecycleOwner, Observer {
            val prefs = requireContext().getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
            val editor = prefs.edit()
            val gson = Gson()
            val json = gson.toJson(it)
            editor.putString("usuario", json)
            editor.apply()
            Log.e("guardado: ", json)

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
        var login = LoginRequestModel(null, null, null)
        if (Patterns.EMAIL_ADDRESS.matcher(binding.login.text.toString()).matches())
            login.email = binding.login.text.toString()
        else if (!binding.login.text.isEmpty())
            login.userName = binding.login.text.toString()
        else
            showMessage("Usuario o correo invalido")

        if (!binding.password.text.isEmpty())
            login.password = binding.password.text.toString()
        else
            showMessage("Contraseña invalida")

        if (!login.email.isNullOrEmpty() || !login.userName.isNullOrEmpty() && !login.password.isNullOrEmpty())
            workoutViewModel.login(login)
    }
}
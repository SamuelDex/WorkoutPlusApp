package com.example.workoutplus.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.workoutplus.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val workoutViewModel: WorkoutViewModel by viewModels()

    val fragments = listOf<Fragment>(
        LoginFragment(),
        RegisterFragment()
    )

    val adapter = LoginFragmentAdapter(
        fragments,
        supportFragmentManager,
        lifecycle
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        workoutViewModel.isLoading.observe(this, Observer {
            binding.progressCircular.isVisible = it
        })

        binding.viewPager.adapter = adapter

        binding.tabLayout.tabGravity = TabLayout.GRAVITY_FILL
        TabLayoutMediator(binding.tabLayout, binding.viewPager){tab, position ->
            if (position == 0)
                tab.text = "Iniciar Sesión"
            else
                tab.text = "Registrar"
        }.attach()
    }
}
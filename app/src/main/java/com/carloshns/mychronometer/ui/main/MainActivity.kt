package com.carloshns.mychronometer.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.carloshns.mychronometer.R
import com.carloshns.mychronometer.data.main.MainViewModel
import com.carloshns.mychronometer.databinding.ActivityMainBinding
import com.carloshns.mychronometer.ui.chronometer.ChronometerActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.buttonPressed.observe(this, Observer {
            val horas = viewModel.horas.value
            val minutos = viewModel.minutos.value
            val segundos = viewModel.segundos.value

            val intent = Intent(this, ChronometerActivity::class.java)
            intent.putExtra("horas", horas)
            intent.putExtra("minutos", minutos)
            intent.putExtra("segundos", segundos)

            resetNumbersPickersData()
            viewModel.resetData()
            startActivity(intent)
        })
    }
    fun resetNumbersPickersData(){
        binding.horasNumberPicker.number = 0
        binding.minutosNumberPicker.number = 0
        binding.segundosNumberPicker.number = 0
    }
}
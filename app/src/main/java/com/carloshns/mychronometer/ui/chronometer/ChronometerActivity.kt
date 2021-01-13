package com.carloshns.mychronometer.ui.chronometer

import android.content.Intent
import android.os.*
import android.util.Log
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.carloshns.mychronometer.R
import com.carloshns.mychronometer.data.chronometer.ChronometerViewModel
import com.carloshns.mychronometer.databinding.ActivityChronometerBinding


class ChronometerActivity : AppCompatActivity() {

    private val viewModel: ChronometerViewModel by viewModels()
    lateinit var binding: ActivityChronometerBinding
    lateinit var myIntent: Intent

    lateinit var clock: CountDownTimer

    override fun onStop() {
        super.onStop()
        if(this::clock.isInitialized){
            pararRelogio()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_chronometer)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setUpListeners()

        myIntent = intent
        iniciarDados()

        viewModel.setupMili()
        viewModel.recebeuHorariosDoMain.postValue(true)
    }

    fun iniciarDados(){

        val horas = myIntent.getIntExtra("horas", 0)
        val minutos = myIntent.getIntExtra("minutos", 0)
        val segundos = myIntent.getIntExtra("segundos", 0)

        viewModel.horas.postValue(horas)
        viewModel.minutos.postValue(minutos)
        viewModel.segundos.postValue(segundos)

        viewModel.horaAtual.postValue(0)
        viewModel.minutoAtual.postValue(0)
        viewModel.segundoAtual.postValue(0)
    }

    fun setUpListeners(){
        viewModel.recebeuHorariosDoMain.observe(this, Observer {
            converterPeriodosDeTempoEAtualizarHorario()
        })

        viewModel.relogioAtivado.observe(this, Observer {
            if(it){
                var tempoTotalEmMili = viewModel.calcularTempoEmMili()
                instanciarRelogio(tempoTotalEmMili)
                inicarRelogio()

                binding.iniciarButton.visibility = INVISIBLE
                binding.pausarButton.visibility = VISIBLE
            } else {
                pararRelogio()
                binding.iniciarButton.visibility = VISIBLE
                binding.pausarButton.visibility = INVISIBLE
            }
        })

        viewModel.reiniciarRelogio.observe(this, Observer {
            pararRelogio()
            viewModel.pausarRelogio()
            iniciarDados()
            viewModel.relogioAtivado.postValue(true)
        })

        viewModel.tempoFinalizado.observe(this, Observer {
            binding.iniciarButton.visibility = INVISIBLE
            binding.pausarButton.visibility = INVISIBLE
        })

        viewModel.voltarParaSelecao.observe(this, Observer {
            finish()
        })

    }

    fun converterPeriodosDeTempoEAtualizarHorario(){
        viewModel.converterPeriodosDeTempoParaMilisegundosEAtualizar()
        viewModel.criarEAtualizarHorario()
    }

    fun instanciarRelogio(total: Long) {
        this.clock = object : CountDownTimer(total + 2000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                viewModel.atualizarRelogio()
                viewModel.criarEAtualizarHorario()
            }

            override fun onFinish() {
                viewModel.tempoFinalizado()
            }
        }
    }

    fun inicarRelogio(){
        this.clock.start()
    }

    fun pararRelogio(){
        this.clock.cancel()
    }

}
package com.carloshns.mychronometer.data.chronometer

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ChronometerViewModel: ViewModel() {

    var horas = MutableLiveData<Int>()
    var minutos = MutableLiveData<Int>()
    var segundos = MutableLiveData<Int>()

    var horaAtual = MutableLiveData<Int>()
    var minutoAtual = MutableLiveData<Int>()
    var segundoAtual = MutableLiveData<Int>()

    var horasEmMili = MutableLiveData<Long>()
    var minutosEmMili = MutableLiveData<Long>()
    var segundosEmMili = MutableLiveData<Long>()

    var stringHorario = MutableLiveData<String>()

    var recebeuHorariosDoMain = MutableLiveData<Boolean>()
    var relogioAtivado =  MutableLiveData<Boolean>()
    var tempoFinalizado = MutableLiveData<Boolean>()
    var voltarParaSelecao = MutableLiveData<Boolean>()
    var reiniciarRelogio = MutableLiveData<Boolean>()

    fun setupMili(){
        horasEmMili.postValue(0)
        minutosEmMili.postValue(0)
        segundosEmMili.postValue(0)
    }

    fun converterPeriodosDeTempoParaMilisegundosEAtualizar(){
        converterHorasEAtualizar(horas.value ?: 0)
        converterMinutosEAtualizar(minutos.value ?: 0)
        converterSegundosEAtualizar(segundos.value ?: 0)
    }

    private fun converterHorasEAtualizar(horas: Int){
        val horasConvertidas = (3600000 * horas).toLong()
        horasEmMili.postValue(horasConvertidas)
    }

    private fun converterMinutosEAtualizar(minutos: Int){
        val horasConvertidas = (60000 * minutos).toLong()
        minutosEmMili.postValue(horasConvertidas)
    }

    private fun converterSegundosEAtualizar(segundos: Int){
        val horasConvertidas = (1000 * segundos).toLong()
        segundosEmMili.postValue(horasConvertidas)
    }

    fun criarEAtualizarHorario(){
        stringHorario.postValue(criarStringHorario())
    }

    fun criarStringHorario(): String {
        val horas = horaAtual.value ?: 0
        val minutos = minutoAtual.value ?: 0
        val segundos = segundoAtual.value ?: 0

        var horario = ""
        horario += "${if(horas >= 10) horas.toString() else "0${horas}"}:"
        horario += "${if(minutos >= 10) minutos.toString() else "0${minutos}"}:"
        horario += "${if(segundos >= 10) segundos.toString() else "0${segundos}"}"
        Log.d("xxx", "criarStringHorario: ${horario}")
        return horario
    }

    fun iniciarRelogio(){
        relogioAtivado.postValue(true)
    }

    fun pausarRelogio(){
        relogioAtivado.postValue(false)
    }

    fun calcularTempoEmMili(): Long {
        var total: Long = 0
        total += horasEmMili.value ?: 0
        total += minutosEmMili.value ?: 0
        total += segundosEmMili.value ?: 0
        return total
    }

    fun atualizarRelogio(){

        var horas: Int = horaAtual.value ?: 0
        var minutos: Int = minutoAtual.value ?: 0
        var segundos: Int = segundoAtual.value ?: 0

        if(segundos < 59){
            if(horas == this.horas.value &&
                    minutos == this.minutos.value &&
                    segundos == this.segundos.value) {
                        tempoFinalizado.postValue(true)
                        tempoFinalizado.postValue(false)
                return
            }
            segundos++
        } else {
            if(minutos < 59){
                minutos++
                segundos = 0
            } else {
                if(horas < 23){
                    horas++
                    minutos = 0
                }
            }
        }
        horaAtual.postValue(horas)
        minutoAtual.postValue(minutos)
        segundoAtual.postValue(segundos)
    }

    fun tempoFinalizado(){
        tempoFinalizado.postValue(true)
    }

    fun voltarParaPaginaDeSelecao(){
        voltarParaSelecao.postValue(true)
    }

    fun reiniciarRelogio(){
        reiniciarRelogio.postValue(true)
    }
}
package com.carloshns.mychronometer.data.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class MainViewModel: ViewModel() {

    var horas = MutableLiveData<Int>().apply {
        value = 0
    }
    var minutos = MutableLiveData<Int>().apply {
        value = 0
    }
    var segundos = MutableLiveData<Int>().apply {
        value = 0
    }
    var buttonPressed = MutableLiveData<Boolean>()

    fun onClickButton(){
        buttonPressed.postValue(true)
        buttonPressed.postValue(false)
    }

    fun resetData(){
        horas.postValue(0)
        minutos.postValue(0)
        segundos.postValue(0)
    }

}
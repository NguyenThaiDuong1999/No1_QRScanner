package com.scanqr.qrscanner.qrgenerator.feature.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModelNo1 : ViewModel() {
    private var string = ""
    var liveString = MutableLiveData<String>()
    fun changeText() {
        string = "Hello my base"
        liveString.value = string
    }

    fun editText() {
        string = "Edit my base"
        liveString.value = string
    }
}
package com.dve.androidservicetest.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class ServiceCommunicator {
    sealed class Events{
        data object Empty:Events()
        class Count(val num :Int):Events()
        data object StopCount:Events()
        class StartCount(val num :Int):Events()
        class ServiceStatus(val running:Boolean):Events()
    }

    var running = false
        set(value) {
            field = value
            CoroutineScope(Dispatchers.IO).launch {
                _events.emit(Events.ServiceStatus(value))
            }
        }
    private val _events = MutableSharedFlow<Events>()
    val events = _events.asSharedFlow()

    fun emitCount(num: Int){
        CoroutineScope(Dispatchers.IO).launch {
            _events.emit(Events.Count(num))
        }
    }

    fun startCount(num: Int){
        CoroutineScope(Dispatchers.IO).launch {
            _events.emit(Events.StartCount(num))
        }
    }

    fun stopCount(){
        CoroutineScope(Dispatchers.IO).launch {
            _events.emit(Events.StopCount)
        }
    }

    fun checkServiceStatus(){
        CoroutineScope(Dispatchers.IO).launch {
            _events.emit(Events.ServiceStatus(running))
        }
    }

}
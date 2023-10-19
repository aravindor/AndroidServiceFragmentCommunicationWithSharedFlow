package com.dve.androidservicetest

import android.Manifest
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.dve.androidservicetest.AppConstants.TAG
import com.dve.androidservicetest.utils.ServiceCommunicator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MyService : Service() {

    private var counting: Job? = null
    private  val notificationBuilder = NotificationCompat.Builder(this,"counting_notification_chanel")
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle("Counting service is active")


    @Inject
    lateinit var serviceCommunicator: ServiceCommunicator

    inner class ServiceBinder:Binder(){
        fun getService():MyService = this@MyService
    }

    override fun onCreate() {
        super.onCreate()
        setServiceCommandListener()
        showNotification()
        serviceCommunicator.running = true
    }

    private val binder = ServiceBinder()

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand: ")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: Service")
        counting?.cancel()
        serviceCommunicator.running = false
    }

    private fun setServiceCommandListener(){
        Log.d(TAG, "setServiceCommandListener: Started")
        CoroutineScope(Dispatchers.IO).launch {
            serviceCommunicator.events.collect{ events->
                when (events){
                    is ServiceCommunicator.Events.Count -> Unit
                    ServiceCommunicator.Events.Empty -> Unit
                    ServiceCommunicator.Events.StopCount -> {
                        Log.d(TAG, "setServiceCommandListener: Stop")
                        counting?.cancel()

                    }

                    is ServiceCommunicator.Events.StartCount -> {
                        Log.d(TAG, "setServiceCommandListener: Start")
                        counting?.cancel()
                        startCount(events.num)
                    }

                    else -> Unit
                }
            }
        }
    }

    private fun startCount(num : Int){
        counting =CoroutineScope(Dispatchers.IO).launch {
            var currentNum = num
            while (true){
                serviceCommunicator.emitCount(currentNum)
                Log.d(TAG, "Count: $currentNum")
                notificationBuilder.setContentText("Count :  $currentNum")
                NotificationManagerCompat.from(this@MyService).apply {
                    if (ActivityCompat.checkSelfPermission(
                            this@MyService,
                            Manifest.permission.POST_NOTIFICATIONS
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        notify(1,notificationBuilder.build())
                    }
                }
                delay(1000)
                currentNum++
            }
        }
    }

    private fun showNotification() {
        val notification = notificationBuilder.build()
        startForeground(1,notification)
    }
}
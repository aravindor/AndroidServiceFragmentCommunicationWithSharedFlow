package com.dve.androidservicetest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val intent  = Intent(this,MyService::class.java)
        startService(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        val intent  = Intent(this,MyService::class.java)
        stopService(intent)
    }
}
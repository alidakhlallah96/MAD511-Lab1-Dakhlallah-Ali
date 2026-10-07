package com.example.mad511_lab1_dakhlallah_ali

import android.app.Application
import com.example.mad511_lab1_dakhlallah_ali.data.AppContainer
import com.example.mad511_lab1_dakhlallah_ali.data.DefaultAppContainer

// application class to initialize app container
class SetListApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}
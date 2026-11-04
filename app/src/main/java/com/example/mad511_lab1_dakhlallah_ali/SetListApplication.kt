package com.example.mad511_lab1_dakhlallah_ali

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// @HiltAndroidApp generates the main Hilt dependency graph for the app
@HiltAndroidApp
class SetListApplication : Application()
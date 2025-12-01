package com.project.raman.shr

import android.app.Application
import com.google.firebase.FirebaseApp
import com.project.raman.shr.utils.PrefManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp: Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        PrefManager.init(this)
    }
}
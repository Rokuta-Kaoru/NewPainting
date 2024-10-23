package com.example.helloworld

import android.app.Application
import com.google.firebase.FirebaseApp

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Firebaseの初期化
        FirebaseApp.initializeApp(this)
    }
}

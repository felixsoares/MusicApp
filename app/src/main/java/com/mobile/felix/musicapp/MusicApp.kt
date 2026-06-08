package com.mobile.felix.musicapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MusicApp : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}
package com.example.musicplayer

import android.app.Application
import timber.log.Timber

class MusicApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
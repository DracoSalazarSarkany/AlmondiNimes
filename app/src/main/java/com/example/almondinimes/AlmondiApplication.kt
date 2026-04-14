package com.example.almondinimes

import android.app.Application
import android.content.Context

class AlmondiApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        private var instance: AlmondiApplication? = null

        fun getContext(): Context {
            return instance!!.applicationContext
        }
    }
}

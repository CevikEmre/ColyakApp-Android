package com.example.colyak.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle

class ColyakApp : Application() {
    var currentActivity: Activity? = null
        private set

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                currentActivity = activity
            }

            override fun onActivityStarted(activity: Activity) {}
            override fun onActivityResumed(activity: Activity) {
                currentActivity = activity
            }

            override fun onActivityPaused(activity: Activity) {}
            override fun onActivityStopped(activity: Activity) {}
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
            override fun onActivityDestroyed(activity: Activity) {
                if (currentActivity == activity) {
                    currentActivity = null
                }
            }
        })
    }

    @SuppressLint("StaticFieldLeak")
    companion object {
        fun applicationContext(): Context = instance.applicationContext
        lateinit var instance: ColyakApp
            private set
    }

    init {
        instance = this
    }
}
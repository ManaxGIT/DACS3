package com.example.video_explorer

import android.app.Application
import android.util.Log
import com.example.video_explorer.data.AppContainer
import com.example.video_explorer.data.DefaultAppContainer

class YoutubeVideoApplication: Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        try {
            super.onCreate()
            container = DefaultAppContainer()
        } catch(e: Exception) {
            Log.i("ex_mess", e.toString())
        }
    }
}
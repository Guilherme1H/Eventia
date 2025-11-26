package com.example.eventia

import android.app.Application
import androidx.emoji2.text.EmojiCompat

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        EmojiCompat.init(this)
    }
}
package com.example.tvserver

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val textView = TextView(this).apply {
            text = "TV Remote Server Running on Port 8888\n\nPlease enable Accessibility Service in Settings if not enabled."
            textSize = 20f
            setPadding(40, 40, 40, 40)
        }
        setContentView(textView)

        startService(Intent(this, SocketServerService::class.java))

        if (TVRemoteAccessibilityService.instance == null) {
            startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
        }
    }
}

package com.example.tvserver

import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.IBinder
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.ServerSocket
import kotlin.concurrent.thread

class SocketServerService : Service() {

    private var serverSocket: ServerSocket? = null
    private var isRunning = false

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (!isRunning) {
            isRunning = true
            startServer()
        }
        return START_STICKY
    }

    private fun startServer() {
        thread {
            try {
                serverSocket = ServerSocket(8888)
                while (isRunning) {
                    val client = serverSocket?.accept() ?: break
                    thread {
                        val reader = BufferedReader(InputStreamReader(client.getInputStream()))
                        val command = reader.readLine()
                        handleCommand(command)
                        client.close()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun handleCommand(command: String?) {
        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        when (command) {
            "CMD_BACK" -> TVRemoteAccessibilityService.instance?.sendBackButton()
            "CMD_HOME" -> TVRemoteAccessibilityService.instance?.sendHomeButton()
            "CMD_VOL_UP" -> audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI)
            "CMD_VOL_DOWN" -> audioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
        serverSocket?.close()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}

package com.example.tvserver;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.IBinder;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServerService extends Service {

    private ServerSocket serverSocket;
    private boolean isRunning = false;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!isRunning) {
            isRunning = true;
            startServer();
        }
        return START_STICKY;
    }

    private void startServer() {
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(8888);
                while (isRunning) {
                    Socket client = serverSocket.accept();
                    new Thread(() -> {
                        try {
                            BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                            String command = reader.readLine();
                            handleCommand(command);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                client.close();
                            } catch (Exception ignored) {}
                        }
                    }).start();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void handleCommand(String command) {
        if (command == null) return;
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        switch (command) {
            case "CMD_BACK":
                if (TVRemoteAccessibilityService.instance != null) {
                    TVRemoteAccessibilityService.instance.sendBackButton();
                }
                break;
            case "CMD_HOME":
                if (TVRemoteAccessibilityService.instance != null) {
                    TVRemoteAccessibilityService.instance.sendHomeButton();
                }
                break;
            case "CMD_VOL_UP":
                if (audioManager != null) {
                    audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
                }
                break;
            case "CMD_VOL_DOWN":
                if (audioManager != null) {
                    audioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
                }
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy() ;
        isRunning = false;
        try {
            if (serverSocket != null) serverSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

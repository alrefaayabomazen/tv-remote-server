package com.example.tvserver;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView textView = new TextView(this);
        textView.setText("TV Remote Server Running on Port 8888\n\nPlease enable Accessibility Service in Settings.");
        textView.setTextSize(20);
        textView.setPadding(40, 40, 40, 40);
        setContentView(textView);

        Intent serviceIntent = new Intent(this, SocketServerService.class);
        startService(serviceIntent);

        if (TVRemoteAccessibilityService.instance == null) {
            Intent settingsIntent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivity(settingsIntent);
        }
    }
}

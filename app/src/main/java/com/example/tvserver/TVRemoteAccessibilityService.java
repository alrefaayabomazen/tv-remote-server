package com.example.tvserver;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;

public class TVRemoteAccessibilityService extends AccessibilityService {

    public static TVRemoteAccessibilityService instance = null;

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        instance = this;
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {}

    @Override
    public void onInterrupt() {}

    @Override
    public void onDestroy() {
        super.onDestroy();
        instance = null;
    }

    public void sendBackButton() {
        performGlobalAction(GLOBAL_ACTION_BACK);
    }

    public void sendHomeButton() {
        performGlobalAction(GLOBAL_ACTION_HOME);
    }
}

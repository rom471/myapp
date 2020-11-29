package com.rom471.services;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;

public class AccessibilityMonitorService extends AccessibilityService {
    private MyRecorder3 myRecorder3;
    public AccessibilityMonitorService() {
    }
    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        myRecorder3=new MyRecorder3(getApplication());
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        myRecorder3.record(event);

    }
    @Override
    public void onInterrupt() {
        myRecorder3.close();
    }
}
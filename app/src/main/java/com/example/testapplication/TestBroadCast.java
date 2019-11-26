package com.example.testapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class TestBroadCast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"receive base service alarm",Toast.LENGTH_SHORT).show();
        Log.d("AlarmBroadcastReceiver","receive base service alarm: " + System.currentTimeMillis());
    }
}

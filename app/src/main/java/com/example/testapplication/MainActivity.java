package com.example.testapplication;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int CAPTURE_PERMISSION_REQUEST_CODE = 1;
    public static Intent mediaProjectionPermissionResultData;
    Intent serviceIntent;
    TestApplication application;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application= (TestApplication) getApplication();
        requestCapture();
    }

    @TargetApi(21)
    private void requestCapture() {
        MediaProjectionManager mediaProjectionManager =
                (MediaProjectionManager) getApplication().getSystemService(
                        Context.MEDIA_PROJECTION_SERVICE);
        startActivityForResult(
                mediaProjectionManager.createScreenCaptureIntent(), CAPTURE_PERMISSION_REQUEST_CODE);
        Log.e(TAG, " startScreenCapture");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(TAG, " onActivityResult data: " + data + " resultCode :" + resultCode + " requestCode:" + requestCode);
        switch (requestCode) {
            case CAPTURE_PERMISSION_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    application.setMediaProjectionPermissionResultData(data);
                    startCaptureService();
                }
                break;
            default:
                break;
        }
    }

    private void startCaptureService() {
        serviceIntent = new Intent(this, TestService.class);
        startService(serviceIntent);
    }

    @Override
    protected void onDestroy() {
        if (serviceIntent != null) {
            stopService(serviceIntent);
        }
        super.onDestroy();
    }
}

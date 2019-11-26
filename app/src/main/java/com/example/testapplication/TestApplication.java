package com.example.testapplication;

import android.app.Application;
import android.content.Intent;

public class TestApplication extends Application {

    public Intent mediaProjectionPermissionResultData;

    public Intent getMediaProjectionPermissionResultData() {
        return mediaProjectionPermissionResultData;
    }

    public void setMediaProjectionPermissionResultData(Intent mediaProjectionPermissionResultData) {
        this.mediaProjectionPermissionResultData = mediaProjectionPermissionResultData;
    }
}

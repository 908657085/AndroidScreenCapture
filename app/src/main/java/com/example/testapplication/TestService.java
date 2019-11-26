package com.example.testapplication;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.projection.MediaProjection;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import static com.example.testapplication.ScreenUtils.getScreenHeight;
import static com.example.testapplication.ScreenUtils.getScreenWidth;

public class TestService extends Service {
    private static final String TAG = TestService.class.getSimpleName();
    ScreenCapturerAndroid screenCapturerAndroid;
    private TestApplication application;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = (TestApplication) getApplication();
        setForeground();
        startCapture();
        Log.d(TAG, "onCreate: ");
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        if(screenCapturerAndroid!=null){
            screenCapturerAndroid.stopCapture();
        }
        stopForeground(true);
        super.onDestroy();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void startCapture() {
        if(application.getMediaProjectionPermissionResultData()==null){
            Log.d(TAG,"mediaProjectionPermissionResultData is null,start capture error!");
            return;
        }
        SurfaceTextureHelper surfaceTextureHelper = SurfaceTextureHelper.create("webrtc_screen_capture", EglBase.create().getEglBaseContext());
        screenCapturerAndroid = new ScreenCapturerAndroid(application.getMediaProjectionPermissionResultData(), new MediaProjection.Callback() {
            @Override
            public void onStop() {
                Log.d(TAG, "mediaProjection onStop");
            }
        });
        screenCapturerAndroid.initialize(surfaceTextureHelper, getApplicationContext());
        screenCapturerAndroid.startCapture(getScreenWidth(getApplicationContext()), getScreenHeight(getApplicationContext()), -1);
    }


    private void setForeground() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                //修改安卓8.1以上系统报错
                String name = getResources().getString(R.string.app_name);
                NotificationChannel notificationChannel = new NotificationChannel(name, name, NotificationManager.IMPORTANCE_NONE);
                notificationChannel.enableLights(false);//如果使用中的设备支持通知灯，则说明此通知通道是否应显示灯
                notificationChannel.enableVibration(false);
                notificationChannel.setShowBadge(false);//是否显示角标
                notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_SECRET);
                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                manager.createNotificationChannel(notificationChannel);
                Notification notification = new Notification.Builder(this.getApplicationContext(), name)
                        .build();
                startForeground(1, notification);
            } else {
                startForeground(1, new Notification());
            }
        }
    }
}

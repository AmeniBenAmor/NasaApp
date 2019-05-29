package com.example.projectfinal;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class SaveImageOftheDay extends Service {
    public SaveImageOftheDay() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

    }
}

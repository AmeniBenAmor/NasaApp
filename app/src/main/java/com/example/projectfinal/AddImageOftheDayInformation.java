package com.example.projectfinal;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class AddImageOftheDayInformation extends Service {
    public AddImageOftheDayInformation() {
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
        super.onStart(intent, startId);
        String date = intent.getStringExtra("date");
        String title=intent.getStringExtra("title") ;
        String information= intent.getStringExtra("information");
        String path=intent.getStringExtra("path");
        Log.i("Service","date: "+date);
        Log.i("Service","title: "+title);
        Log.i("Service","information: "+information);
        Log.i("Service","path: "+path);
        ImageOfTheDayDataBaseHelper helper =new  ImageOfTheDayDataBaseHelper(getApplicationContext());
        Intent newIntent = new Intent(getApplicationContext(), ShowAPODSvedImages.class);

        newIntent.putExtra("status",helper.addData(date,title,information,path));
        try {
            Log.d("Hello","hi");
            newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(newIntent);

        }catch (Exception e){
            Log.e("heh",e.toString());
        }
    }
}

package com.example.projectfinal;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class AddWeatherImageInformation extends Service {
    public AddWeatherImageInformation() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        String date = intent.getStringExtra("date");
        String cloudScore=intent.getStringExtra("cloud_score") ;
        String latitude= intent.getStringExtra("latitude");
        String longitude= intent.getStringExtra("longitude");
        String path=intent.getStringExtra("path");
        Log.i("Service","date: "+date);
        Log.i("Service","cloud score: "+cloudScore);
        Log.i("Service","latitude: "+latitude);
        Log.i("Service","longitude: "+longitude);
        Log.i("Service","path: "+path);
        WeatherDataBaseHelper helper =new  WeatherDataBaseHelper(getApplicationContext());
        Intent newIntent = new Intent(getApplicationContext(), ShowWeatherImages.class);

        newIntent.putExtra("status",helper.addData(date,cloudScore,latitude,longitude,path));
        try {
            newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(newIntent);

        }catch (Exception e){
            Log.e("heh",e.toString());
        }


    }



    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}

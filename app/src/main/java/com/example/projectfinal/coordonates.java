package com.example.projectfinal;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class coordonates extends AppCompatActivity {

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {

                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    private String saveImage(Bitmap imageBitmap){
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/saved_images/WeatherImages");
        myDir.mkdirs();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String fileName = format.format(new Date());
        File file = new File(myDir, fileName+".jpg");
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            Toast.makeText(getApplicationContext(),"saved to: \n"+"saved_images/WeatherImages/"+fileName+".jpg",Toast.LENGTH_SHORT).show();
            Log.i("Files", "saved to: "+myDir.toString()+fileName+".jpg");
            return file.getAbsolutePath();

        } catch (Exception e) {
            Log.e("Files",e.toString());
            return null;
        }
    }
    public static final int MY_PERMISSIONS_REQUEST_WRITE = 99;

    public  boolean isStoragePermissionGranted() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle(R.string.text_write_ext_permission)
                        .setMessage(R.string.text_location_permission)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(coordonates.this,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE);
            }
            return false;
        } else {
            return true;


        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordonates);
        final ImageView image = findViewById(R.id.imV2);
        final TextView textView = findViewById(R.id.infoTextView);
        final Intent intentToAddweatherInformation = new Intent(getApplicationContext(),AddWeatherImageInformation.class);


        new Thread(new Runnable() {
            public void run() {


                double longitude = 10.1657900;
                double latitude = 36.8189700;

                try{
                    checkLocationPermission();
                    LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
                    Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    longitude = location.getLongitude();
                    latitude = location.getLatitude();

                }catch (Exception e){
                    System.out.println("Can't get location"+e.toString());
                }
                Date date = new Date();


                String urlFormat = String.format("https://api.nasa.gov/planetary/earth/imagery/?lon=%s&lat=%s&date=2016-%d-%d&cloud_score=false&api_key=",longitude,latitude,date.getMonth()+1,date.getDate());
                JSONObject json = RemoteFetch.getJSON(getApplicationContext(),urlFormat+"%s");

                if(json!=null)
                {
                    try{
                        String informations= "in this day in 2016\ncloud score: "+json.getInt("cloud_score")+
                                "\ndate: "+json.getString("date")+
                                "\nlatitude: "+latitude+"\nlongitude: "+longitude;
                        intentToAddweatherInformation.putExtra("date",json.getString("date"));
                        intentToAddweatherInformation.putExtra("cloud_score",Integer.toString(json.getInt("cloud_score")));
                        intentToAddweatherInformation.putExtra("latitude",Double.toString(latitude));
                        intentToAddweatherInformation.putExtra("longitude",Double.toString(longitude));

                        textView.setText(informations);
                        new DownloadImageTask(image).execute(json.getString("url"));
                    }
                    catch (Exception e){

                    }

                }

            }
        }).start();

        Button homeButton = findViewById(R.id.homeBtn);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ShowWeatherImages.class);
                startActivity(intent);
            }
        });
        final Button saveWeatherBtn = findViewById(R.id.saveWeatherBtn);
        saveWeatherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{

                    BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
                    Bitmap bitmap= drawable.getBitmap();
                    if(isStoragePermissionGranted()){
                        String path=saveImage(bitmap);
                        intentToAddweatherInformation.putExtra("path",path);
                        startService(intentToAddweatherInformation);
                        Intent intentToUserLog = new Intent("com.codinginflow.SAVE_USER_ACTIVITY");
                        intentToUserLog.putExtra("activity","saving Weather Image to: "+path);
                        sendBroadcast(intentToUserLog);
                    }else{
                        System.out.println("no permission");
                    }

                }catch(Exception e){
                    System.out.print("Error: "+e.toString()+"\n");
                }



            }
        });



    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle(R.string.title_location_permission)
                        .setMessage(R.string.text_location_permission)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(coordonates.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
                        //LocationManager.requestLocationUpdates(provider, 400, 1, this);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }
}

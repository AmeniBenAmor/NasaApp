package com.example.projectfinal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;



import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.Serializable;

public class MainActivity extends AppCompatActivity {

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.button);
        final ImageView image= findViewById(R.id.imageView);
        final TextView textArea = findViewById(R.id.textView);


        new Thread(new Runnable() {
            public void run() {

                JSONObject json = RemoteFetch.getJSON(getApplicationContext(),"https://api.nasa.gov/planetary/apod?api_key=%s");

                if(json!=null)
                {
                    try{
                        textArea.setText(json.getString("title"));
                        new DownloadImageTask(image).execute(json.getString("url"));
                    }
                    catch (Exception e){
                        textArea.setText("null");
                    }

                }

                else textArea.setText("null");
            }
        }).start();

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(),coordonates.class);
                startActivity(i);

            }
        });

        final Button saveWeatherBtn = findViewById(R.id.saveIMG);
        saveWeatherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                    BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();

                    Bitmap bitmap= drawable.getBitmap();
                    Intent intent = new Intent(getApplicationContext(),SaveImageOftheDay.class);
                    ByteArrayOutputStream bStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 50, bStream);
                    byte[] byteArray = bStream.toByteArray();
                    intent.putExtra("image", byteArray);
                    startService(intent);

                }catch(Exception e){
                    System.out.print("Error: "+e.toString()+"\n");
                }



            }
        });




    }


}

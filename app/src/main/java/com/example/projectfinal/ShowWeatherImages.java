package com.example.projectfinal;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ShowWeatherImages extends AppCompatActivity {
    private List<String> fileList= new ArrayList<String>();
    private int index = 0;
    private void ListDir(File f){
        File [] files = f.listFiles();
        fileList.clear();
        for (File file : files){
            fileList.add(file.getPath());
        }
    }

    public void setInformations(ImageView jpgView, TextView textView){
        File imageFile = new File(fileList.get(index));
        String resultToPrint="Date: "+fileList.get(index).split("\\/")[6].split("\\.")[0];
        WeatherDataBaseHelper helper = new WeatherDataBaseHelper(getApplicationContext());
        try{
            Cursor data = helper.getDataByPath(imageFile.getAbsolutePath());
            Log.i("Data:",data.toString());
            while(data.moveToNext()){
                resultToPrint+= "\n Capture Date: "+data.getString(0) ;
                resultToPrint+= "\n Cloud Score: "+data.getString(1);
                resultToPrint+= "\n latitude: "+data.getString(2);
                resultToPrint+= "\n longitude: "+data.getString(3);
            }
        }catch (Exception e){
            Log.e("Data",e.toString());
        }

        textView.setText(resultToPrint);

        BitmapDrawable d = new BitmapDrawable(getResources(), imageFile.getAbsolutePath());
        jpgView.setImageDrawable(d);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            Intent intent = getIntent();
            Toast.makeText(getApplicationContext(),intent.getStringExtra("status"),Toast.LENGTH_SHORT).show();

        }catch(Exception e){
            Log.e("ShowImageIntent",e.toString());
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_images);
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/saved_images/WeatherImages");
        ListDir(myDir);
        final TextView textView = findViewById(R.id.fileListTextView);
        final ImageView jpgView = findViewById(R.id.weatherSavedImageView);

        setInformations(jpgView, textView);








        Button nextButton = findViewById(R.id.nextWeatherSavedImages);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(index<(fileList.size()-1))index++;
                else index=0;
                setInformations(jpgView, textView);

            }
        });

        Button previousButton = findViewById(R.id.previousWeatherSavedImages);
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(index>0)index--;
                else index=fileList.size()-1;
                setInformations(jpgView, textView);

            }
        });


    }

}

package com.example.projectfinal;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ShowAPODSvedImages extends AppCompatActivity {

    private List<String> fileList= new ArrayList<String>();
    private int index = 0;
    private void ListDir(File f){
        File [] files = f.listFiles();
        fileList.clear();
        for (File file : files){
            fileList.add(file.getPath());
        }
    }

    public void setInformations(ImageView jpgView, TextView titleView, TextView textView){
        File imageFile = new File(fileList.get(index));
        String titleToPrint="Date: "+fileList.get(index).split("\\/")[6].split("\\.")[0];
        String informationTOPrint = "Information: ";
        ImageOfTheDayDataBaseHelper helper = new ImageOfTheDayDataBaseHelper(getApplicationContext());
        try{
            Cursor data = helper.getDataByPath(imageFile.getAbsolutePath());
            Log.i("Data:",data.toString());
            if(data.moveToNext()){
                titleToPrint+= "\nTitle: "+data.getString(1);
                informationTOPrint+= data.getString(2);
            }
        }catch (Exception e){
            Log.e("Data",e.toString());
        }

        textView.setText(informationTOPrint);
        titleView.setText(titleToPrint);
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
        setContentView(R.layout.activity_show_apodsved_images);
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/saved_images/imageOfTheDay");
        ListDir(myDir);
        final TextView textView = findViewById(R.id.savedAPODTextView);
        final TextView titleView = findViewById(R.id.savedAPODTitleView);
        final ImageView jpgView = findViewById(R.id.savedAPODImageView);

        setInformations(jpgView, titleView,textView);

        Button nextButton = findViewById(R.id.savedAPODnextBtn);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(index<(fileList.size()-1))index++;
                else index=0;
                setInformations(jpgView, titleView,textView);
            }
        });

        Button previousButton = findViewById(R.id.savedAPODPreBtn);
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(index>0)index--;
                else index=fileList.size()-1;
                setInformations(jpgView, titleView,textView);

            }
        });


    }
}

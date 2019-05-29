package com.example.projectfinal;

import android.content.Context;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class RemoteFetch {
    private static final String key = "6l7KcC0lcWUct1hmqop89LggHxXQtHihVNpjIvsS";
    //private static final String urlFormat="https://api.nasa.gov/planetary/apod?api_key=%s";

    public static JSONObject getJSON(Context context,String urlFormat){



        try{

            URL url = new URL (String.format(urlFormat,key));
            System.out.print("hello1 "+url+"\n");
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();
            Response response = client.newCall(request).execute();
            JSONObject data = new JSONObject(response.body().string());
            System.out.print("hello2 "+data.toString()+"\n");
            return data;
        }catch (Exception e){
            System.out.print("hello error\n");
            System.out.print(e.toString());
            return null;
        }
    }
}

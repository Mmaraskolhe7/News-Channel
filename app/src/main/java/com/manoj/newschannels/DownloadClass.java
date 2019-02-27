package com.manoj.newschannels;

import android.os.AsyncTask;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

//To download the JSON fro API using asynktsk
public class DownloadClass extends AsyncTask<String,Void,String> {

    @Override
    protected String doInBackground(String... urls) {
        URL url;
        String result ="";
        HttpURLConnection urlConnection =null;
        try {
            url =new URL(urls[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = urlConnection.getInputStream();

            InputStreamReader reader = new InputStreamReader(in);
            int data = reader.read();
            while(data!=-1){
                char current = (char) data;
                result += current;
                data= reader.read();
            }
            return result;
        }
        catch (Exception e){
            e.printStackTrace();
            return "Failed";
        }

    }
}


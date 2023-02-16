package com.example.findweatherapp;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class GetDataFromInternet extends AsyncTask<URL, Void, String> {

    public GetDataFromInternet(AsyncResponse delegate){
        this.delegate = delegate;
    }

    private static final String TAG = "GetDataFromInternet";
    private AsyncResponse delegate;

    public interface AsyncResponse{
        void processFinished(String output);
    }

    protected String getResponseFromFromHttpGetUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            String result;

            if (scanner.hasNext()){
                result = scanner.next();
                return result;
            }
            else {
                return null;
            }
        }
        finally {
            urlConnection.disconnect();
        }

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.d(TAG, "onPreExecute: called");

    }

    @Override
    protected String doInBackground(URL[] url) {
        Log.d(TAG, "doInBackground: called");

        String result = null;

        try {
            result = getResponseFromFromHttpGetUrl(url[0]);
        }
        catch (IOException e){
            e.printStackTrace();
        }

        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        delegate.processFinished(result);
    }
}

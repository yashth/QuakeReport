package com.example.quakereport;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


public class QueryUtils {
    /** Sample JSON response for a USGS query */
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();
    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }


    public static List<Quake> fetchEarthquakeDetail(String requestUrl){

        URL url = createUrl(requestUrl);
        Log.d(LOG_TAG, "fetchEarthquakeDetail requestUrl: "+requestUrl);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String jsonResponse = "";
        try{
            jsonResponse = makeHttpResponse(url);
        }catch (IOException e){
            Log.e(LOG_TAG, "Error closing input stream", e);

        }

        List<Quake> earthQuake = extractFeature(jsonResponse);

        return earthQuake;
    }

    private static List<Quake> extractFeature(String jsonResponse){
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }

        Log.d(LOG_TAG, "extractFeature jsonResponse: "+jsonResponse);

        List<Quake> earthquakes = new ArrayList<>();

        try{
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray jsonArray = jsonObject.getJSONArray("features");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject firstFeature = jsonArray.getJSONObject(0);
                JSONObject properties = firstFeature.getJSONObject("properties");

                double magnitude = properties.getDouble("mag");
                String location = properties.getString("place");
                long date = properties.getLong("time");

                String url = properties.getString("url");
                Quake quake = new Quake(magnitude, location, date, url);

                earthquakes.add(quake);
            }



        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
        }

        Log.d(LOG_TAG, "extractFeature earthquakes: "+earthquakes);

        return earthquakes;

    }


    private static URL createUrl(String stringUrl){
        URL url = null;
        try{
            url = new URL(stringUrl);
        } catch (MalformedURLException e){
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        Log.d(LOG_TAG, "createUrl url: "+url);
        return url;

    }

    private static String makeHttpResponse(URL url) throws IOException {
        String jsonResponse="";
        Log.d(LOG_TAG, "makeHttpResponse url: "+url);
        if(url==null){
            return null;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try{
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if(urlConnection.getResponseCode()==200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);

            }else{
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }

        } catch (IOException e){
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        }finally {
            if(urlConnection!=null){
                urlConnection.disconnect();
            }
            if(inputStream!=null){
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException{
        StringBuilder output = new StringBuilder();
        if(inputStream!=null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            Log.d(LOG_TAG,"line: "+line);
            while(line!=null){
                output.append(line);
                Log.d(LOG_TAG,"line: "+line);
                line = reader.readLine();
            }

        }
        return output.toString();
    }

}

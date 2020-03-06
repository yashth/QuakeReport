package com.example.quakereport;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.Nullable;
import android.content.AsyncTaskLoader;
import android.util.Log;

import java.util.List;

public class QuakeLoader extends AsyncTaskLoader<List<Quake>> {

    private static final String LOG_TAG = "QuakeLoader";

    private String mUrl;

    public QuakeLoader(Context context,String url){
        super(context);

        Log.d(LOG_TAG,"TEST: QuakeLoader called ");
        mUrl = url;
    }

    @Override
    protected void onStartLoading(){
        Log.d(LOG_TAG,"TEST: onStartLoading called ");
        forceLoad();
    }

    @Nullable
    @Override
    public List<Quake> loadInBackground() {
        Log.d(LOG_TAG,"TEST: loadInBackground called ");

        if(mUrl==null){
            return null;
        }

        List<Quake> earthquake = QueryUtils.fetchEarthquakeDetail(mUrl);

        return earthquake;


    }
}

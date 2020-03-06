package com.example.quakereport;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;

import android.app.usage.UsageEvents;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.loader.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.Loader;
import android.content.AsyncTaskLoader;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<Quake>> {

    public static final String LOG_TAG = "MainActivity";


    private static final int EARTHQUAKE_LOADER_ID = 1;
    private TextView mEmptyStateTextView;

    /** URL to query the USGS dataset for earthquake information */
    private static final String USGS_DATA = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";

    private QuakeAdapter mAdapter;;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Create a fake list of earthquake locations.

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = connMgr.getActiveNetworkInfo();

        if(netInfo!=null && netInfo.isConnected()){
            android.app.LoaderManager loaderManager = getLoaderManager();
            Log.d(LOG_TAG,"TEST: initLoader called");
            loaderManager.initLoader(EARTHQUAKE_LOADER_ID,null,this);
        } else{
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }

        Log.d(LOG_TAG,"TEST: Adapter called");

        mAdapter = new QuakeAdapter(this,new ArrayList<Quake>());

        ListView listView = (ListView) findViewById(R.id.list);

        listView.setEmptyView(mEmptyStateTextView);

        listView.setAdapter(mAdapter);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Quake currentQuake = mAdapter.getItem(position);

                Uri earthquakeUri = Uri.parse(currentQuake.getUrl());

                Intent websiteIntent = new Intent(Intent.ACTION_VIEW,earthquakeUri);
                startActivity(websiteIntent);
            }
        });



    }

    @Override
    public Loader<List<Quake>> onCreateLoader(int i, Bundle bundle) {
        // Create a new loader for the given URL

        Log.d(LOG_TAG,"TEST: onCreateLoader called i: "+i);

        return new QuakeLoader(this, USGS_DATA);
    }

    @Override
    public void onLoadFinished(Loader<List<Quake>> loader, List<Quake> earthquake){

        Log.d(LOG_TAG,"TEST: onLoadFinished called ");
        mEmptyStateTextView.setText(R.string.no_quake);

        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        mAdapter.clear();

        if(earthquake!=null || !earthquake.isEmpty()){
           mAdapter.addAll(earthquake);
        }


    }

    @Override
    public void onLoaderReset(Loader<List<Quake>> loader) {

        Log.d(LOG_TAG,"TEST: onLoaderReset called ");
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }





}

package com.example.android.newsappstage2;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    /**
     * Constant for id loader.
     * It could come to play if there will going to use multiple loaders
     */
    private static final int NEWSAPI_LOADER_ID = 1;
    /**
     * URL of Guardian API
     */
    //private static final String USGS_REQUEST_URL =
    //        "http://content.guardianapis.com/search?order-by=newest&show-fields=trailText,byline,thumbnail&page-size=10&api-key=test";

    private static final String USGS_REQUEST_URL =
            "http://content.guardianapis.com/search";
    /**
     * Adapter for the list of news
     */
    private NewsAdapter mAdapter;

    /**
     * TextView that is displayed when the list is empty
     */
    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        // Find a reference to the {@link ListView} in the layout
        ListView newsListView = findViewById(R.id.list);

        //find id of empty view
        mEmptyStateTextView = findViewById(R.id.empty_view);
        newsListView.setEmptyView(mEmptyStateTextView);

        // This create a new adapter that takes an empty list of news as input
        mAdapter = new NewsAdapter(this, new ArrayList<News>());

        // Set the adapter on the {@link ListView}so the list can be populated in the UI
        newsListView.setAdapter(mAdapter);


        // Set the item click to open url web page of news
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current news that was clicked on
                News currentNews = (News) adapterView.getItemAtPosition(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri newsUri = Uri.parse(currentNews.getUrl());

                // Create a new intent to view the news URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = null;

        if (connMgr != null) {
            // Get details on the currently active default data network
            networkInfo = connMgr.getActiveNetworkInfo();
        }


        // If there is a network connection, we can fetch data
        if (networkInfo != null && networkInfo.isConnected()) {

            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant of istance.
            loaderManager.initLoader(NEWSAPI_LOADER_ID, null, this);
        } else {
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            // Set empty state text to display "Sorry! There isn\'t news ad the moment."
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }

    }

    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        // getString retrieves a String value from the preferences.
        String searchTerm = sharedPrefs.getString(getString(R.string.settings_search_term_key),"DEFAULT");

        // parse breaks apart the URI string that's passed into its parameter
        Uri baseUri = Uri.parse(USGS_REQUEST_URL);

        // buildUpon prepares the baseUri that we just parsed so we can add query parameters to it
        Uri.Builder uriBuilder = baseUri.buildUpon();

        // Append query parameter and its value. For example, the `format=geojson`
        uriBuilder.appendQueryParameter("q", searchTerm);
        uriBuilder.appendQueryParameter("page-size", "10");
        uriBuilder.appendQueryParameter("show-fields", "trailText,byline,thumbnail");
        uriBuilder.appendQueryParameter("order-by", "newest");
        uriBuilder.appendQueryParameter("api-key", "test");


        // Create a new loader for the given URL
        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {

        // Hide loading indicator when the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display message when there isn't news
        mEmptyStateTextView.setText(R.string.no_news);
        // Clear the adapter of previous news data
        mAdapter.clear();

        // This will trigger the ListView to update.
        if (news != null && !news.isEmpty()) {
            mAdapter.addAll(news);

        }

    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {

        // Clear out our existing data.
        mAdapter.clear();

    }

    //Inner static AsyncTaskLoader Class. Useful in handling configuration changes (screen orientation)
    public static class NewsLoader extends AsyncTaskLoader<List<News>> {

        private String mUrl;

        private NewsLoader(Context context, String url) {
            super(context);
            mUrl = url;
        }

        @Override
        protected void onStartLoading() {

            forceLoad();
        }

        @Override
        public List<News> loadInBackground() {
            //return QueryUtils.extractFeatureFromJson(mUrl);
            return QueryUtils.fetchNewsData(mUrl);

        }
    }

    @Override
    // This method initialize the contents of the Activity's options menu.
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the Options Menu we specified in XML
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    //This method is where we can setup the specific action that occurs when any of the items in the Options Menu are selected.
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

package com.zekunwang.flickster;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.zekunwang.flickster.adapters.MovieArrayAdapter;
import com.zekunwang.flickster.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;    // async http library


public class MovieActivity extends ActionBarActivity {

    static ArrayList<Movie> movies;
    MovieArrayAdapter movieAdapter;
    ListView lvItems;
    private SwipeRefreshLayout swipeContainer;
    RadioButton rbVote;
    RadioButton rbDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        lvItems = (ListView) findViewById(R.id.lvMovies);
        movies = new ArrayList<>();
        movieAdapter = new MovieArrayAdapter(this, movies);
        lvItems.setAdapter(movieAdapter);

        refreshMovies(0);

        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshMovies(0);
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;
                if (movies.get(position).getVoteAverage() < MovieArrayAdapter.POPULAR_VOTE) {
                    intent = new Intent(MovieActivity.this, MovieDetailActivity.class);
                } else {
                    intent = new Intent(MovieActivity.this, YoutubeActivity.class);
                }
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });

    }

    private void refreshMovies(int page) {
        if (swipeContainer != null) {
            swipeContainer.setRefreshing(false);    // disable refreshing icon
        }
        String url = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray movieJsonResults = null;
                try {
                    movieJsonResults = response.getJSONArray("results");    // get raw data from response
                    movies.clear(); // clear old movie data
                    movies.addAll(Movie.fromJSONArray(movieJsonResults));     // convert JSON array to model class objects

                    if (rbVote.isChecked()) {
                        Movie.sortByVote(movies);
                    } else {
                        rbDate.setChecked(true);
                        Movie.sortByDate(movies);
                    }
                    movieAdapter.notifyDataSetChanged();    // trigger update of data
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem actionViewItem = menu.findItem(R.id.miActionSort);
        // Retrieve the action-view from menu
        View v = MenuItemCompat.getActionView(actionViewItem);
        // Find the button within action-view
        final RadioButton rbByDate = (RadioButton) v.findViewById(R.id.rbByDate);
        final RadioButton rbByVote = (RadioButton) v.findViewById(R.id.rbByVote);
        rbDate = rbByDate;
        rbVote = rbByVote;
        rbByDate.setChecked(true);
        rbByDate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    rbByVote.setChecked(false);
                    Movie.sortByDate(movies);
                    movieAdapter.notifyDataSetChanged();
                }
            }
        });
        rbByVote.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    rbByDate.setChecked(false);
                    Movie.sortByVote(movies);
                    movieAdapter.notifyDataSetChanged();
                }
            }
        });
        // Handle button click here
        return super.onPrepareOptionsMenu(menu);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movie, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }
}

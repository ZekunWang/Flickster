package com.zekunwang.flickster;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zekunwang.flickster.models.Movie;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;


public class MovieDetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        ImageView ivImage = (ImageView) findViewById(R.id.ivMovieImage);
        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        TextView tvReleaseDate = (TextView) findViewById(R.id.tvReleaseDate);
        RatingBar rbVote = (RatingBar) findViewById(R.id.rbVote);
        TextView tvOverview = (TextView) findViewById(R.id.tvOverview);

        int position = getIntent().getIntExtra("position", 0);
        Movie movie = MovieActivity.movies.get(position);

        tvTitle.setText(movie.getOriginalTitle());
        tvReleaseDate.setText("Release Date: " + movie.getReleaseDate());
        rbVote.setRating((float) movie.getVoteAverage());
        tvOverview.setText(movie.getOverview());


        // use Picasso to fetch image from url and put into image view
        String urlImage = null;
        int placeholderImage = R.drawable.movie_placeholder_landscape;
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            urlImage = movie.getBackdropPath();
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            urlImage = movie.getPosterPath();
            placeholderImage = R.drawable.movie_placeholder_portrait;
        }
        //viewHolder.ivImage.setImageResource(placeholderImage);
        Picasso.with(this).load(urlImage)
                .placeholder(placeholderImage)
                .into(ivImage);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movie_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

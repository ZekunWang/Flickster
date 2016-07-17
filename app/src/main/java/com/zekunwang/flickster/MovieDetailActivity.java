package com.zekunwang.flickster;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zekunwang.flickster.models.Movie;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;


public class MovieDetailActivity extends Activity {

    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        ImageView ivButtonImage = (ImageView) findViewById(R.id.ivButtonImage);
        ImageView ivImage = (ImageView) findViewById(R.id.ivMovieImage);
        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        TextView tvReleaseDate = (TextView) findViewById(R.id.tvReleaseDate);
        RatingBar rbVote = (RatingBar) findViewById(R.id.rbVote);
        TextView tvOverview = (TextView) findViewById(R.id.tvOverview);

        position = getIntent().getIntExtra("position", 0);
        Movie movie = MovieActivity.movies.get(position);

        tvTitle.setText(movie.getOriginalTitle());
        tvReleaseDate.setText("Release Date: " + movie.getReleaseDate());
        rbVote.setRating((float) movie.getVoteAverage());
        tvOverview.setText(movie.getOverview());


        // use Picasso to fetch image from url and put into image view
        String urlImage = null;
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = (int) (0.5625 * width);

        int placeholderImage = R.drawable.movie_placeholder_landscape;
        Drawable newDrawable = null;

        //viewHolder.ivImage.setImageResource(placeholderImage);
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            urlImage = movie.getBackdropPath();
            Drawable myDrawable = getResources().getDrawable(R.drawable.movie_placeholder_landscape);
            Bitmap b = ((BitmapDrawable) myDrawable).getBitmap();
            newDrawable = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(b, width, height, false));
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            urlImage = movie.getPosterPath();
            newDrawable = getResources().getDrawable(R.drawable.movie_placeholder_portrait);
            width = dpToPx(167);
        }
        //viewHolder.ivImage.setImageResource(placeholderImage);
        Picasso.with(this).load(urlImage).resize(width, 0)
                .placeholder(newDrawable)
                .into(ivImage);

        ivButtonImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MovieDetailActivity.this, YoutubeActivity.class);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });
    }

    // convert dp to px
    private int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
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

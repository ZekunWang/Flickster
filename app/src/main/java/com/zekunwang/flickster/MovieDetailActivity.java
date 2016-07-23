package com.zekunwang.flickster;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zekunwang.flickster.models.Movie;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;


public class MovieDetailActivity extends ActionBarActivity {

    int position;
    private final double RATIO = 1 / 4.0;

    @BindView(R.id.ivButtonImage) ImageView ivButtonImage;
    @BindView(R.id.ivMovieImage) ImageView ivImage;
    @BindView(R.id.tvTitle) TextView tvTitle;
    @BindView(R.id.tvReleaseDate) TextView tvReleaseDate;
    @BindView(R.id.rbVote) RatingBar rbVote;
    @BindView(R.id.tvOverview) TextView tvOverview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        // get width of current metric
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = (int) (0.5625 * width);
        // popular drawable
        Drawable drawableLandscape = getResources().getDrawable(R.drawable.movie_placeholder_landscape);
        Drawable drawablePortrait = getResources().getDrawable(R.drawable.movie_placeholder_portrait);
        Bitmap bitmapLandscape = ((BitmapDrawable) drawableLandscape).getBitmap();
        Bitmap bitmapPortrait = ((BitmapDrawable) drawablePortrait).getBitmap();
        Drawable drawableLandscapeRegular = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmapLandscape, width, height, false));
        Drawable drawablePortraitRegular = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmapPortrait, (int)(width * RATIO), (int)(width / 0.668 * RATIO), false));
        // use Picasso to fetch image from url and put into image view
        int orientation = getResources().getConfiguration().orientation;

        position = getIntent().getIntExtra("position", 0);
        Movie movie = MovieActivity.movies.get(position);

        tvTitle.setText(movie.getOriginalTitle());
        tvReleaseDate.setText("Release Date: " + movie.getReleaseDate());
        rbVote.setRating((float) movie.getVoteAverage());
        tvOverview.setText(movie.getOverview());

        String urlImage = movie.getBackdropPath();
        Drawable placeholderImage = drawableLandscapeRegular;
        int widthRegular = width;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            urlImage = movie.getPosterPath();
            placeholderImage = drawablePortraitRegular;
            widthRegular = (int)(width * RATIO);


            //viewHolder.ivImage.setImageResource(placeholderImage);
            Picasso.with(this).load(urlImage).resize(widthRegular, 0)
                    .placeholder(placeholderImage)
                    .transform(new RoundedCornersTransformation(10, 10))
                    .into(ivImage);
        } else {
            //viewHolder.ivImage.setImageResource(placeholderImage);
            Picasso.with(this).load(urlImage).resize(widthRegular, 0)
                    .placeholder(placeholderImage)
                    .into(ivImage);
        }
    }

    @OnClick(R.id.ivButtonImage)
    public void OnClickPopular(View v) {
        Intent intent = new Intent(MovieDetailActivity.this, YoutubeActivity.class);
        intent.putExtra("position", position);
        startActivity(intent);
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

        return super.onOptionsItemSelected(item);
    }
}

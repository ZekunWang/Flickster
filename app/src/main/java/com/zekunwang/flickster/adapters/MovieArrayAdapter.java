package com.zekunwang.flickster.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zekunwang.flickster.R;
import com.zekunwang.flickster.models.Movie;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by zwang_000 on 7/13/2016.
 */
public class MovieArrayAdapter extends ArrayAdapter<Movie> {

    private final int REGULAR = 0;
    private final int POPULAR = 1;
    public static final int POPULAR_VOTE = 5;
    private final int VIEW_TYPE_COUNT = 2;
    Movie movie;
    int type;
    int width;
    int height;
    int orientation;
    private final double RATIO_PORTRAIT = 3 / 7.0;
    private final double RATIO_LANDSCAPE = 4 / 7.0;
    Drawable drawableLandscapePopular;
    Drawable drawableLandscapeRegular;
    Drawable drawablePortraitRegular;


    public MovieArrayAdapter(Context context, List<Movie> movies) {
        super(context, 0, movies);

        // use Picasso to fetch image from url and put into image view
        orientation = getContext().getResources().getConfiguration().orientation;
        // get width of current metric
        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        width = metrics.widthPixels;
        height = (int) (0.5625 * width);
        // popular drawable
        Drawable drawableLandscape = getContext().getResources().getDrawable(R.drawable.movie_placeholder_landscape);
        Drawable drawablePortrait = getContext().getResources().getDrawable(R.drawable.movie_placeholder_portrait);
        Bitmap bitmapLandscape = ((BitmapDrawable) drawableLandscape).getBitmap();
        Bitmap bitmapPortrait = ((BitmapDrawable) drawablePortrait).getBitmap();
        drawableLandscapePopular = new BitmapDrawable(getContext().getResources(), Bitmap.createScaledBitmap(bitmapLandscape, width, height, false));
        drawableLandscapeRegular = new BitmapDrawable(getContext().getResources(), Bitmap.createScaledBitmap(bitmapLandscape, (int) (width * RATIO_LANDSCAPE), (int) (height * RATIO_LANDSCAPE), false));
        drawablePortraitRegular = new BitmapDrawable(getContext().getResources(), Bitmap.createScaledBitmap(bitmapPortrait,
                (int) (width * RATIO_PORTRAIT), (int) (width / 0.668 * RATIO_PORTRAIT), false));
    }
    
    @Override
    public int getItemViewType(int position) {
        // decide returned view type according to vote of the movie
        return getItem(position).getVoteAverage() < POPULAR_VOTE ? REGULAR : POPULAR;
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;

    }

    static class ViewHolderPopular {
        @BindView(R.id.tvTitle) TextView tvTitle;
        @BindView(R.id.ivMovieImage) ImageView ivImage;
        @BindView(R.id.ivButtonImage) ImageView ivButtonView;

        public ViewHolderPopular(View view) {
            ButterKnife.bind(this, view);
        }
    }
    static class ViewHolderRegular {
        @BindView(R.id.tvTitle) TextView tvTitle;
        @BindView(R.id.ivMovieImage) ImageView ivImage;
        @BindView(R.id.tvOverview) TextView tvOverview;

        public ViewHolderRegular(View view) {
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get data from the position
        movie = getItem(position);
        // get view type by position
        type = getItemViewType(position);

        // check the existing view being used
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());

            if (type == REGULAR) {  // save view for REGULAR
                convertView = inflater.inflate(R.layout.item_movie, parent, false);
                ViewHolderRegular viewHolderRegular = new ViewHolderRegular(convertView);

                showRegular(viewHolderRegular);
                convertView.setTag(viewHolderRegular); // cache viewHolder for this item view
            } else {  // save view for POPULAR
                convertView = inflater.inflate(R.layout.item_movie_popular, parent, false);
                ViewHolderPopular viewHolderPopular = new ViewHolderPopular(convertView);

                showPopular(viewHolderPopular);
                convertView.setTag(viewHolderPopular); // cache viewHolder for this item view
            }
        } else if (type == REGULAR) {
                ViewHolderRegular viewHolderRegular = (ViewHolderRegular) convertView.getTag();
                showRegular(viewHolderRegular);
        } else {
                ViewHolderPopular viewHolderPopular = (ViewHolderPopular) convertView.getTag();
                showPopular(viewHolderPopular);
        }
        return convertView;
    }

    private void showRegular(ViewHolderRegular viewHolderRegular) {
        viewHolderRegular.tvTitle.setText(movie.getOriginalTitle());
        viewHolderRegular.tvOverview.setText(movie.getOverview());

        String urlImage = movie.getPosterPath();
        Drawable placeholderImage = drawablePortraitRegular;
        int widthRegular = (int)(width * RATIO_PORTRAIT);

        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            urlImage = movie.getBackdropPath();
            placeholderImage = drawableLandscapeRegular;
            widthRegular = (int)(width * RATIO_LANDSCAPE);
        }

        //viewHolder.ivImage.setImageResource(placeholderImage);
        Picasso.with(getContext()).load(urlImage).resize(widthRegular, 0)
                .placeholder(placeholderImage)
                .transform(new RoundedCornersTransformation(10, 10))
                .into(viewHolderRegular.ivImage);
    }

    private void showPopular(ViewHolderPopular viewHolderPopular) {
        // use Picasso to fetch image from url and put into image view
        viewHolderPopular.tvTitle.setText(movie.getOriginalTitle());
        String urlImage = movie.getBackdropPath();

        Picasso.with(getContext()).load(urlImage)
                .resize(width,height)
                .placeholder(drawableLandscapePopular)
                .into(viewHolderPopular.ivImage);
    }
}

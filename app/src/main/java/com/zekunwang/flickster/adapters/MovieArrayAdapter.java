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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zekunwang.flickster.R;
import com.zekunwang.flickster.models.Movie;

import java.util.List;

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

    private static class ViewHolderPopular {
        ImageView ivButtonView;
        ImageView ivImage;
    }
    private static class ViewHolderRegular {
        TextView tvTitle;
        TextView tvOverview;
        ImageView ivImage;
    }

    public MovieArrayAdapter(Context context, List<Movie> movies) {
        super(context, 0, movies);
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
                ViewHolderRegular viewHolderRegular = new ViewHolderRegular();
                convertView = inflater.inflate(R.layout.item_movie, parent, false);
                // viewHolder cachees views of the item view
                viewHolderRegular.ivImage = (ImageView) convertView.findViewById(R.id.ivMovieImage);
                viewHolderRegular.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
                viewHolderRegular.tvOverview = (TextView) convertView.findViewById(R.id.tvOverview);
                convertView.setTag(viewHolderRegular); // cache viewHolder for this item view
            } else {  // save view for POPULAR
                ViewHolderPopular viewHolderPopular = new ViewHolderPopular();
                convertView = inflater.inflate(R.layout.item_movie_popular, parent, false);
                // viewHolder cachees views of the item view
                viewHolderPopular.ivImage = (ImageView) convertView.findViewById(R.id.ivMovieImage);
                viewHolderPopular.ivButtonView = (ImageView) convertView.findViewById(R.id.ivButtonImage);
                convertView.setTag(viewHolderPopular); // cache viewHolder for this item view
            }
        }

        if (type == REGULAR) {
                ViewHolderRegular viewHolderRegular = (ViewHolderRegular) convertView.getTag();
                showRegular(viewHolderRegular);
        } else {
                ViewHolderPopular viewHolderPopular = (ViewHolderPopular) convertView.getTag();
                showPopular(viewHolderPopular, convertView);
        }

        return convertView;
    }

    private void showRegular(ViewHolderRegular viewHolderRegular) {
        viewHolderRegular.tvTitle.setText(movie.getOriginalTitle());
        viewHolderRegular.tvOverview.setText(movie.getOverview());

        // use Picasso to fetch image from url and put into image view
        int orientation = getContext().getResources().getConfiguration().orientation;
        String urlImage = movie.getPosterPath();
        int placeholderImage = R.drawable.movie_placeholder_portrait;
        int width = 167;

        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            urlImage = movie.getBackdropPath();
            placeholderImage = R.drawable.movie_placeholder_landscape;
            width = 414;
        }
        loadImageView(urlImage, width, placeholderImage, viewHolderRegular.ivImage);
    }

    private void showPopular(ViewHolderPopular viewHolderPopular, View convertView) {
        // use Picasso to fetch image from url and put into image view
        String urlImage = movie.getBackdropPath();

        // get width of current metric
        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = (int)(0.5625 * width);

        Drawable myDrawable = getContext().getResources().getDrawable(R.drawable.movie_placeholder_landscape);
        Bitmap b = ((BitmapDrawable)myDrawable).getBitmap();
        Drawable newDrawable = new BitmapDrawable(getContext().getResources(), Bitmap.createScaledBitmap(b, width, height, true));

        RelativeLayout relativeLayout = (RelativeLayout)  convertView.findViewById(R.id.popularView);
        relativeLayout.setMinimumHeight(height);

        //viewHolder.ivImage.setImageResource(placeholderImage);
        Picasso.with(getContext()).load(urlImage)
                .fit()
                .placeholder(newDrawable)
                .into(viewHolderPopular.ivImage);
    }

    private void loadImageView(String urlImage, int width, int placeholderImage, ImageView ivImage) {
        //viewHolder.ivImage.setImageResource(placeholderImage);
        Picasso.with(getContext()).load(urlImage).resize(dpToPx(width), 0)
                .placeholder(placeholderImage)
                .transform(new RoundedCornersTransformation(10, 10))
                .into(ivImage);
    }

    // convert dp to px
    private int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }
}

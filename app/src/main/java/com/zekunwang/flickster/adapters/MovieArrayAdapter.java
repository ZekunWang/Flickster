package com.zekunwang.flickster.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
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

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by zwang_000 on 7/13/2016.
 */
public class MovieArrayAdapter extends ArrayAdapter<Movie> {

    // View lookup cache
    private static class ViewHolder {
        ImageView ivImage;
        TextView tvTitle;
        TextView tvOverview;
    }

    public MovieArrayAdapter(Context context, List<Movie> movies) {
        super(context, R.layout.item_movie, movies);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get data from the position
        Movie movie = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        // check the existing view being used
        if (convertView == null) {
            viewHolder = new ViewHolder();  // create new view holder
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_movie, parent, false);
            // viewHolder cachees views of the item view
            viewHolder.ivImage = (ImageView) convertView.findViewById(R.id.ivMovieImage);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            viewHolder.tvOverview = (TextView) convertView.findViewById(R.id.tvOverview);
            convertView.setTag(viewHolder); // cache viewHolder for this item view
        } else {
            viewHolder = (ViewHolder)convertView.getTag();  // get cached views to improve efficiency
        }

        viewHolder.tvTitle.setText(movie.getOriginalTitle());
        viewHolder.tvOverview.setText(movie.getOverview());

        // use Picasso to fetch image from url and put into image view
        String urlImage = null;
        int placeholderImage = R.drawable.movie_placeholder_portrait;
        int orientation = getContext().getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            urlImage = movie.getPosterPath();
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            urlImage = movie.getBackdropPath();
            placeholderImage = R.drawable.movie_placeholder_landscape;
        }
        //viewHolder.ivImage.setImageResource(placeholderImage);
        Picasso.with(getContext()).load(urlImage)
                .placeholder(placeholderImage)
                .transform(new RoundedCornersTransformation(10, 10))
                .into(viewHolder.ivImage);

        return convertView;
    }
}

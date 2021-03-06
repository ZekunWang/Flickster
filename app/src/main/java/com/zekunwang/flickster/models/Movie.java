package com.zekunwang.flickster.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by zwang_000 on 7/13/2016.
 */
public class Movie {

    String posterPath;
    String originalTitle;
    String releaseDate;
    String overview;
    String backdropPath;
    int voteCount;
    double voteAverage;
    int id;
    ArrayList<Video> videos;

    public Movie(JSONObject jsonObject) throws JSONException {
        this.posterPath = jsonObject.getString("poster_path");
        this.originalTitle = jsonObject.getString("original_title");
        this.releaseDate = jsonObject.getString("release_date");
        this.overview = jsonObject.getString("overview");
        this.backdropPath = jsonObject.getString("backdrop_path");
        this.voteCount = jsonObject.getInt("vote_count");
        this.voteAverage = jsonObject.getDouble("vote_average");
        this.id = jsonObject.getInt("id");
        this.videos = new ArrayList<Video>();
    }

    public static void sortByDate(List<Movie> movies) {
        Collections.sort(movies, new Comparator<Movie>() {
            @Override
            public int compare(Movie m1, Movie m2) {
                return m2.getReleaseDate().compareTo(m1.getReleaseDate());
            }
        });
    }

    public static void sortByVote(List<Movie> movies) {
        Collections.sort(movies, new Comparator<Movie>() {
            @Override
            public int compare(Movie m1, Movie m2) {
                double v1 = m1.getVoteAverage();
                double v2 = m2.getVoteAverage();
                if (v1 == v2) {
                    return 0;
                }
                return v1 > v2 ? -1 : 1;
            }
        });
    }

    public static ArrayList<Movie> fromJSONArray(JSONArray jsonArray) {
        ArrayList<Movie> results = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                results.add(new Movie(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return results;
    }

    public String getPosterPath() {
        return "https://image.tmdb.org/t/p/w342" + posterPath;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getOverview() {
        return overview;
    }

    public String getBackdropPath() {
        return "https://image.tmdb.org/t/p/w780" + backdropPath;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public int getId() {
        return id;
    }

    public ArrayList<Video> getVideos() {
        return videos;
    }

    public void setVideos(ArrayList<Video> v) {
        this.videos = v;
    }
}

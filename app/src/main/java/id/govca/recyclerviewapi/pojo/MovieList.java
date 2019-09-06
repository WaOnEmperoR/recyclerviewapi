package id.govca.recyclerviewapi.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MovieList {

    @SerializedName("results")
    private ArrayList<Movie> movieArrayList;

    public ArrayList<Movie> getMovieArrayList() {
        return movieArrayList;
    }

    public void setMovieArrayList(ArrayList<Movie> movieArrayList) {
        this.movieArrayList = movieArrayList;
    }
}

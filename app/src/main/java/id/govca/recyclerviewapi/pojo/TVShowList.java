package id.govca.recyclerviewapi.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TVShowList {

    @SerializedName("results")
    private ArrayList<TVShow> tvShowArrayList;

    public ArrayList<TVShow> getTvShowArrayList() {
        return tvShowArrayList;
    }

    public void setTvShowArrayList(ArrayList<TVShow> tvShowArrayList) {
        this.tvShowArrayList = tvShowArrayList;
    }
}

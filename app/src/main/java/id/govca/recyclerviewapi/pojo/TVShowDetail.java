package id.govca.recyclerviewapi.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TVShowDetail {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("genres")
    @Expose
    private int[] genres;
    @SerializedName("homepage")
    @Expose
    private String homepage;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("first_air_date")
    @Expose
    private String first_air_date;
    @SerializedName("vote_average")
    @Expose
    private double vote_average;
    @SerializedName("overview")
    @Expose
    private String overview;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int[] getGenres() {
        return genres;
    }

    public void setGenres(int[] genres) {
        this.genres = genres;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirst_air_date() {
        return first_air_date;
    }

    public void setFirst_air_date(String first_air_date) {
        this.first_air_date = first_air_date;
    }

    public double getVote_average() {
        return vote_average;
    }

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }
}

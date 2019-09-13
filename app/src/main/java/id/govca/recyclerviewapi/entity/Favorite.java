package id.govca.recyclerviewapi.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

import id.govca.recyclerviewapi.pojo.Genre;

@Entity(tableName = "favorite")
public class Favorite implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int favId;
    @ColumnInfo(name = "thingsId")
    private int thingsId;
    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "type")
    private int type;
    @ColumnInfo(name = "date_available")
    private String date_available;
    @ColumnInfo(name = "vote_average")
    private double vote_average;
    @ColumnInfo(name = "poster_path")
    private String poster_path;
    @ColumnInfo(name = "homepage")
    private String homepage;
    @ColumnInfo(name = "synopsis")
    private String synopsis;

    public int getFavId() {
        return favId;
    }

    public void setFavId(int favId) {
        this.favId = favId;
    }

    public int getThingsId() {
        return thingsId;
    }

    public void setThingsId(int thingsId) {
        this.thingsId = thingsId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDate_available() {
        return date_available;
    }

    public void setDate_available(String date_available) {
        this.date_available = date_available;
    }

    public double getVote_average() {
        return vote_average;
    }

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }
}

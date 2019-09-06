package id.govca.recyclerviewapi.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "first_air_date",
        "name"
})

public class TVShow implements Parcelable {
    @JsonProperty("name")
    private String name;
    @JsonProperty("popularity")
    private double popularity;
    @JsonProperty("vote_average")
    private double vote_average;
    @JsonProperty("overview")
    private String overview;
    @JsonProperty("first_air_date")
    private String first_air_date;
    @JsonProperty("poster_path")
    private String poster_path;
    @JsonProperty("genre_ids")
    private int[] genre_ids;

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("popularity")
    public double getPopularity() {
        return popularity;
    }

    @JsonProperty("popularity")
    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    @JsonProperty("vote_average")
    public double getVote_average() {
        return vote_average;
    }

    @JsonProperty("vote_average")
    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }

    @JsonProperty("overview")
    public String getOverview() {
        return overview;
    }

    @JsonProperty("overview")
    public void setOverview(String overview) {
        this.overview = overview;
    }

    @JsonProperty("first_air_date")
    public String getFirst_air_date() {
        return first_air_date;
    }

    @JsonProperty("first_air_date")
    public void setFirst_air_date(String first_air_date) {
        this.first_air_date = first_air_date;
    }

    @JsonProperty("poster_path")
    public String getPoster_path() {
        return poster_path;
    }

    @JsonProperty("poster_path")
    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    @JsonProperty("genre_ids")
    public int[] getGenre_ids() {
        return genre_ids;
    }

    @JsonProperty("genre_ids")
    public void setGenre_ids(int[] genre_ids) {
        this.genre_ids = genre_ids;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeDouble(this.popularity);
        dest.writeDouble(this.vote_average);
        dest.writeString(this.overview);
        dest.writeString(this.first_air_date);
        dest.writeString(this.poster_path);
        dest.writeIntArray(this.genre_ids);
    }

    public TVShow() {
    }

    protected TVShow(Parcel in) {
        this.name = in.readString();
        this.popularity = in.readDouble();
        this.vote_average = in.readDouble();
        this.overview = in.readString();
        this.first_air_date = in.readString();
        this.poster_path = in.readString();
        this.genre_ids = in.createIntArray();
    }

    public static final Parcelable.Creator<TVShow> CREATOR = new Parcelable.Creator<TVShow>() {
        @Override
        public TVShow createFromParcel(Parcel source) {
            return new TVShow(source);
        }

        @Override
        public TVShow[] newArray(int size) {
            return new TVShow[size];
        }
    };
}

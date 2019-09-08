package id.govca.recyclerviewapi.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TVShow implements Parcelable {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("popularity")
    @Expose
    private double popularity;
    @SerializedName("vote_average")
    @Expose
    private double vote_average;
    @SerializedName("overview")
    @Expose
    private String overview;
    @SerializedName("first_air_date")
    @Expose
    private String first_air_date;
    @SerializedName("poster_path")
    @Expose
    private String poster_path;
    @SerializedName("genre_ids")
    @Expose
    private int[] genre_ids;
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("name")
    public String getName() {
        return name;
    }

    @SerializedName("name")
    public void setName(String name) {
        this.name = name;
    }

    @SerializedName("popularity")
    public double getPopularity() {
        return popularity;
    }

    @SerializedName("popularity")
    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    @SerializedName("vote_average")
    public double getVote_average() {
        return vote_average;
    }

    @SerializedName("vote_average")
    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }

    @SerializedName("overview")
    public String getOverview() {
        return overview;
    }

    @SerializedName("overview")
    public void setOverview(String overview) {
        this.overview = overview;
    }

    @SerializedName("first_air_date")
    public String getFirst_air_date() {
        return first_air_date;
    }

    @SerializedName("first_air_date")
    public void setFirst_air_date(String first_air_date) {
        this.first_air_date = first_air_date;
    }

    @SerializedName("poster_path")
    public String getPoster_path() {
        return poster_path;
    }

    @SerializedName("poster_path")
    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    @SerializedName("genre_ids")
    public int[] getGenre_ids() {
        return genre_ids;
    }

    @SerializedName("genre_ids")
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
        dest.writeInt(this.id);
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
        this.id = in.readInt();
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

    @SerializedName("id")
    public int getId() {
        return id;
    }

    @SerializedName("id")
    public void setId(int id) {
        this.id = id;
    }
}

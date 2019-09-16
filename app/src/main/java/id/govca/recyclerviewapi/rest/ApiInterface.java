package id.govca.recyclerviewapi.rest;

import id.govca.recyclerviewapi.pojo.MovieDetail;
import id.govca.recyclerviewapi.pojo.MovieList;
import id.govca.recyclerviewapi.pojo.TVShowDetail;
import id.govca.recyclerviewapi.pojo.TVShowList;
import io.reactivex.Observable;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiInterface {

    @GET("discover/movie")
    Observable<MovieList> RxGetMovieList(@Query("api_key") String apiKey,
                                         @Query("language") String language);

    @GET("search/movie")
    Observable<MovieList> RxSearchMovies(@Query("query") String query,
                                         @Query("api_key") String apiKey,
                                         @Query("language") String language);

    @GET("discover/tv")
    Observable<TVShowList> RxGetTVShowList(@Query("api_key") String apiKey,
                                           @Query("language") String language);

    @GET("search/tv")
    Observable<TVShowList> RxSearchTVShows(@Query("query") String query,
                                         @Query("api_key") String apiKey,
                                         @Query("language") String language);

    @GET("movie/{Id}")
    Observable<MovieDetail> RxMovieDetails(@Path("Id") int id,
                                           @Query("api_key") String apiKey,
                                           @Query("language") String language);

    @GET("tv/{Id}")
    Observable<TVShowDetail> RxTVShowDetails(@Path("Id") int id,
                                             @Query("api_key") String apiKey,
                                             @Query("language") String language);


}

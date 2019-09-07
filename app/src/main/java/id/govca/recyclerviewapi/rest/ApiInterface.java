package id.govca.recyclerviewapi.rest;

import id.govca.recyclerviewapi.pojo.MovieDetail;
import id.govca.recyclerviewapi.pojo.MovieList;
import id.govca.recyclerviewapi.pojo.TVShowDetail;
import id.govca.recyclerviewapi.pojo.TVShowList;
import io.reactivex.Observable;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("movie/popular")
    Observable<MovieList> RxGetMovieList(@Query("api_key") String apiKey,
                                         @Query("language") String language,
                                         @Query("page") int page);

    @GET("tv/popular")
    Observable<TVShowList> RxGetTVShowList(@Query("api_key") String apiKey,
                                           @Query("language") String language,
                                           @Query("page") int page);

    @GET("movie/{Id}")
    Observable<MovieDetail> RxMovieDetails(@Path("Id") int id,
                                           @Query("api_key") String apiKey,
                                           @Query("language") String language);

    @GET("tv/{Id}")
    Observable<TVShowDetail> RxTVShowDetails(@Path("Id") int id,
                                             @Query("api_key") String apiKey,
                                             @Query("language") String language);
}

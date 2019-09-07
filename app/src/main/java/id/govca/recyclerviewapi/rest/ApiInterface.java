package id.govca.recyclerviewapi.rest;

import java.util.List;
import java.util.Map;

import id.govca.recyclerviewapi.pojo.Movie;
import id.govca.recyclerviewapi.pojo.MovieDetail;
import id.govca.recyclerviewapi.pojo.MovieList;
import id.govca.recyclerviewapi.pojo.TVShowDetail;
import id.govca.recyclerviewapi.pojo.TVShowList;
import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface ApiInterface {

    @GET("movie/popular?api_key=34200b5e63f3bd9a701e1705f7982e41&language=en-US&page=1")
    Observable<MovieList> RxGetMovieList();

    @GET("tv/popular?api_key=34200b5e63f3bd9a701e1705f7982e41&language=en-US&page=1")
    Observable<TVShowList> RxGetTVShowList();

    @GET("movie/{Id}")
    Observable<MovieDetail> RxMovieDetails(
            @Path("Id") int id,
            @Query("api_key") String apiKey,
            @Query("language") String language);

    @GET("/tv")
    Observable<TVShowDetail> RxTVShowDetails(@QueryMap Map<String, String> filters);
}

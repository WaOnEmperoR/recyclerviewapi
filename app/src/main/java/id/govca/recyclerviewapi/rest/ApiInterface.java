package id.govca.recyclerviewapi.rest;

import java.util.List;

import id.govca.recyclerviewapi.pojo.Movie;
import id.govca.recyclerviewapi.pojo.MovieList;
import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("movie/popular?api_key=34200b5e63f3bd9a701e1705f7982e41&language=en-US&page=1")
    Observable<MovieList> RxGetMovieList();
}

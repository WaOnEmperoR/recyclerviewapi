package id.govca.recyclerviewapi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Locale;
import java.util.StringJoiner;

import id.govca.recyclerviewapi.helper.Constants;
import id.govca.recyclerviewapi.pojo.Genre;
import id.govca.recyclerviewapi.pojo.MovieDetail;
import id.govca.recyclerviewapi.pojo.MovieList;
import id.govca.recyclerviewapi.rest.ApiClient;
import id.govca.recyclerviewapi.rest.ApiInterface;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class DetailActivity extends AppCompatActivity {

    private CompositeDisposable disposable = new CompositeDisposable();

    private final String TAG = this.getClass().getSimpleName();
    private View mProgressView;

    private int idThings;
    private String category;
    private TextView tv_name, tv_rating, tv_genres, tv_homepage, tv_year, tv_synopsis;
    private ImageView imgView_poster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Locale current = getResources().getConfiguration().locale;

        Bundle b = getIntent().getExtras();
        Log.d(TAG, "Movie ID : " + String.valueOf(b.getInt("Movie_ID")));
        Log.d(TAG, "Category : " + b.getString("Category"));

        idThings = b.getInt("Movie_ID");
        category = b.getString("Category");

        Log.d(TAG, current.getISO3Language());
        Log.d(TAG, current.getISO3Country());

        tv_name = findViewById(R.id.tv_movie_name);
        tv_rating = findViewById(R.id.tv_movie_rating_content);
        tv_genres = findViewById(R.id.tv_genres_content);
        tv_homepage = findViewById(R.id.tv_homepage_content);
        tv_year = findViewById(R.id.tv_movie_year_content);
        tv_synopsis = findViewById(R.id.tv_movie_synopsis_content);
        imgView_poster = findViewById(R.id.imgView_poster);

        ObserveMovieDetail();
    }

    private Observable<MovieDetail> getMovieDetailObs(){
        final ApiInterface mApiService = ApiClient.getClient().create(ApiInterface.class);

        return mApiService.RxMovieDetails(idThings, Constants.API_KEY, "en-US")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private void ObserveMovieDetail()
    {
        Observable<MovieDetail> movieDetailObservable = getMovieDetailObs();

        disposable.add(
                movieDetailObservable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<MovieDetail>() {
                    @Override
                    public void onNext(MovieDetail movieDetail) {
//                        Log.d(TAG, movieDetail.getHomepage());
//                        Log.d(TAG, movieDetail.getOriginal_title());
//                        Log.d(TAG, movieDetail.getOverview());

                        StringJoiner joiner = new StringJoiner(", ");
                        Genre[] genres = movieDetail.getGenres();
                        for (int i=0; i<genres.length; i++)
                        {
                            joiner.add(genres[i].getName());
                        }
                        Log.d(TAG, joiner.toString());

                        tv_name.setText(movieDetail.getOriginal_title());
                        tv_genres.setText(joiner.toString());
                        tv_homepage.setText(movieDetail.getHomepage());
                        tv_synopsis.setText(movieDetail.getOverview());
                        tv_year.setText(movieDetail.getRelease_date());
                        tv_rating.setText(String.valueOf(movieDetail.getVote_average()));

                        Glide
                                .with(getBaseContext())
                                .load(Constants.IMAGE_ROOT_LARGE + movieDetail.getPoster_path())
                                .into(imgView_poster);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete from RXJava");
                        this.dispose();
                    }
                })

        );
    }
}

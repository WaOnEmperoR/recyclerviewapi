package id.govca.recyclerviewapi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.util.Locale;
import java.util.StringJoiner;

import id.govca.recyclerviewapi.entity.Favorite;
import id.govca.recyclerviewapi.helper.Constants;
import id.govca.recyclerviewapi.pojo.Genre;
import id.govca.recyclerviewapi.pojo.MovieDetail;
import id.govca.recyclerviewapi.pojo.MovieList;
import id.govca.recyclerviewapi.pojo.TVShow;
import id.govca.recyclerviewapi.pojo.TVShowDetail;
import id.govca.recyclerviewapi.rest.ApiClient;
import id.govca.recyclerviewapi.rest.ApiInterface;
import id.govca.recyclerviewapi.viewmodel.MovieListViewModel;
import id.govca.recyclerviewapi.viewmodel.MovieViewModel;
import id.govca.recyclerviewapi.viewmodel.TvShowViewModel;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class DetailActivity extends AppCompatActivity {

    private CompositeDisposable disposable = new CompositeDisposable();

    private final String TAG = this.getClass().getSimpleName();
    private View mProgressView;
    private View mScrollView;

    private int idThings, category;
    private TextView tv_name, tv_rating, tv_genres, tv_homepage, tv_year, tv_synopsis;
    private ImageView imgView_poster;

    Button favoriteButton;

    private MovieViewModel movieViewModel;
    private TvShowViewModel tvShowViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Bundle b = getIntent().getExtras();
        idThings = b.getInt("Movie_ID");
        category = b.getInt("Category");

        mProgressView = findViewById(R.id.progressBarDetail);
        mScrollView = findViewById(R.id.scrollViewDetail);

        tv_name = findViewById(R.id.tv_movie_name);
        tv_rating = findViewById(R.id.tv_movie_rating_content);
        tv_genres = findViewById(R.id.tv_genres_content);
        tv_homepage = findViewById(R.id.tv_homepage_content);
        tv_year = findViewById(R.id.tv_movie_year_content);
        tv_synopsis = findViewById(R.id.tv_movie_synopsis_content);
        imgView_poster = findViewById(R.id.imgView_poster);
        favoriteButton = findViewById(R.id.btn_favorite);

        if (category == 0)
        {
            movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
            movieViewModel.getMovieDetail().observe(this, getMovieDetail);

            Locale current = getResources().getConfiguration().locale;

            String param_lang = current.getLanguage() + "-" + current.getCountry();
            if (param_lang.equals("in-ID"))
            {
                param_lang = "id-ID";
            }

            movieViewModel.setMovieDetail(idThings, param_lang);
        }
        else if (category == 1)
        {
            tvShowViewModel = ViewModelProviders.of(this).get(TvShowViewModel.class);
            tvShowViewModel.getTvShowDetail().observe(this, getTVShowDetail);

            Locale current = getResources().getConfiguration().locale;

            String param_lang = current.getLanguage() + "-" + current.getCountry();
            if (param_lang.equals("in-ID"))
            {
                param_lang = "id-ID";
            }

            tvShowViewModel.setTvShowDetail(idThings, param_lang);
        }
    }

    private Observer<MovieDetail> getMovieDetail = new Observer<MovieDetail>() {
        @Override
        public void onChanged(MovieDetail movieDetail) {
            if (movieDetail!=null){
                pseudoAdapterMovie(movieDetail);
                showLoading(false);
            }
        }
    };

    private Observer<TVShowDetail> getTVShowDetail = new Observer<TVShowDetail>() {
        @Override
        public void onChanged(TVShowDetail tvShowDetail) {
            if (tvShowDetail != null)
            {
                pseudoAdapterTVShow(tvShowDetail);
                showLoading(false);
            }
        }
    };

    private void pseudoAdapterMovie(final MovieDetail movieDetail){
        StringJoiner joiner = new StringJoiner(", ");
        Genre[] genres = movieDetail.getGenres();
        for (int i=0; i<genres.length; i++)
        {
            joiner.add(genres[i].getName());
        }

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

        CheckMovie checkMovie = new CheckMovie();
        checkMovie.execute(movieDetail.getId());

        //Movie is already set as favorite
        if (isMovieFavorite(movieDetail.getId()))
        {
            favoriteButton.setText(getResources().getString(R.string.title_unfavorite));
            favoriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    unsetFavoriteMovie(movieDetail);
                }
            });
        }
        // not yet a favorite
        else
        {
            favoriteButton.setText(getResources().getString(R.string.title_favorite));
            favoriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setFavoriteMovie(movieDetail);
                }
            });
        }

    }

    private void pseudoAdapterTVShow(final TVShowDetail tvShowDetail)
    {
        StringJoiner joiner = new StringJoiner(", ");
        Genre[] genres = tvShowDetail.getGenres();
        for (int i=0; i<genres.length; i++)
        {
            joiner.add(genres[i].getName());
        }

        tv_name.setText(tvShowDetail.getName());
        tv_genres.setText(joiner.toString());
        tv_homepage.setText(tvShowDetail.getHomepage());
        tv_synopsis.setText(tvShowDetail.getOverview());
        tv_year.setText(tvShowDetail.getFirst_air_date());
        tv_rating.setText(String.valueOf(tvShowDetail.getVote_average()));

        Glide
                .with(getBaseContext())
                .load(Constants.IMAGE_ROOT_LARGE + tvShowDetail.getPoster_path())
                .into(imgView_poster);

        CheckMovie cm = new CheckMovie();
        cm.execute(idThings);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_change_settings){
            Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(mIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void showLoading(Boolean state) {
        if (state) {
            mProgressView.setVisibility(View.VISIBLE);
            mScrollView.setVisibility(View.GONE);
        } else {
            mProgressView.setVisibility(View.GONE);
            mScrollView.setVisibility(View.VISIBLE);
        }
    }


    private class CheckMovie extends AsyncTask<Integer, Void, Boolean>{

        @Override
        protected Boolean doInBackground(Integer... integers) {
            int count = DatabaseClient.getInstance(getApplicationContext())
                    .getAppDatabase()
                    .getFavoriteDAO()
                    .checkFavoriteMovie(integers[0]);

            return count>0;
        }
    }

    private class CheckTVShow extends AsyncTask<Integer, Void, Boolean>{

        @Override
        protected Boolean doInBackground(Integer... integers) {
            int count = DatabaseClient.getInstance(getApplicationContext())
                    .getAppDatabase()
                    .getFavoriteDAO()
                    .checkFavoriteTVShow(integers[0]);

            return count>0;
        }

    }

    private void setFavoriteMovie(final MovieDetail movieDetail){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Favorite fav = new Favorite();

                fav.setThingsId(movieDetail.getId());
                fav.setDate_available(movieDetail.getRelease_date());
                fav.setHomepage(movieDetail.getHomepage());
                fav.setType(0);
                fav.setTitle(movieDetail.getOriginal_title());
                fav.setVote_average(movieDetail.getVote_average());
                fav.setPoster_path(movieDetail.getPoster_path());

                DatabaseClient.getInstance(getApplicationContext())
                        .getAppDatabase()
                        .getFavoriteDAO()
                        .insertFavorite(fav);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid)
            {
                super.onPostExecute(aVoid);
                favoriteButton.setText(getResources().getString(R.string.title_unfavorite));
                Log.d(TAG, "Set as Favorite Movie success");
            }
        }.execute();
    }

    private void unsetFavoriteMovie(final MovieDetail movieDetail)
    {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Favorite fav = new Favorite();

                fav.setThingsId(movieDetail.getId());
                fav.setDate_available(movieDetail.getRelease_date());
                fav.setHomepage(movieDetail.getHomepage());
                fav.setType(0);
                fav.setTitle(movieDetail.getOriginal_title());
                fav.setVote_average(movieDetail.getVote_average());
                fav.setPoster_path(movieDetail.getPoster_path());

                DatabaseClient.getInstance(getApplicationContext())
                        .getAppDatabase()
                        .getFavoriteDAO()
                        .deleteFavorite(fav);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid)
            {
                super.onPostExecute(aVoid);
                favoriteButton.setText(getResources().getString(R.string.title_favorite));
                Log.d(TAG, "Delete Favorite Movie success");
            }
        }.execute();
    }

    private boolean isTVShowFavorite(int idThings)
    {
        int count = DatabaseClient.getInstance(getApplicationContext())
                .getAppDatabase()
                .getFavoriteDAO()
                .checkFavoriteTVShow(idThings);

        return (count>0);
    }

    private void setFavoriteTVShow(final TVShowDetail tvShowDetail){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Favorite fav = new Favorite();

                fav.setThingsId(tvShowDetail.getId());
                fav.setDate_available(tvShowDetail.getFirst_air_date());
                fav.setHomepage(tvShowDetail.getHomepage());
                fav.setType(1);
                fav.setTitle(tvShowDetail.getName());
                fav.setVote_average(tvShowDetail.getVote_average());
                fav.setPoster_path(tvShowDetail.getPoster_path());

                DatabaseClient.getInstance(getApplicationContext())
                        .getAppDatabase()
                        .getFavoriteDAO()
                        .insertFavorite(fav);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid)
            {
                super.onPostExecute(aVoid);
                favoriteButton.setText(getResources().getString(R.string.title_unfavorite));
                Log.d(TAG, "Set TV Show as Favorite success");
            }
        }.execute();
    }

    private void unsetFavoriteTVShow(final TVShowDetail tvShowDetail)
    {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Favorite fav = new Favorite();

                fav.setThingsId(tvShowDetail.getId());
                fav.setDate_available(tvShowDetail.getFirst_air_date());
                fav.setHomepage(tvShowDetail.getHomepage());
                fav.setType(1);
                fav.setTitle(tvShowDetail.getName());
                fav.setVote_average(tvShowDetail.getVote_average());
                fav.setPoster_path(tvShowDetail.getPoster_path());

                DatabaseClient.getInstance(getApplicationContext())
                        .getAppDatabase()
                        .getFavoriteDAO()
                        .deleteFavorite(fav);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid)
            {
                super.onPostExecute(aVoid);
                favoriteButton.setText(getResources().getString(R.string.title_favorite));
                Log.d(TAG, "Delete Favorite TV Show success");
            }
        }.execute();
    }
}

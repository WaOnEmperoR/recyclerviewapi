package id.govca.recyclerviewapi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
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
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.util.Locale;
import java.util.StringJoiner;

import id.govca.recyclerviewapi.entity.Favorite;
import id.govca.recyclerviewapi.helper.Constants;
import id.govca.recyclerviewapi.pojo.Genre;
import id.govca.recyclerviewapi.pojo.MovieDetail;
import id.govca.recyclerviewapi.pojo.TVShowDetail;
import id.govca.recyclerviewapi.viewmodel.MovieViewModel;
import id.govca.recyclerviewapi.viewmodel.TvShowViewModel;

public class DetailActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();
    private View mProgressView;
    private View mScrollView;

    private int idThings, category;
    private TextView tv_name, tv_rating, tv_genres, tv_homepage, tv_year, tv_synopsis;
    private ImageView imgView_poster;

    private RatingBar ratingBar;

    Context context = GlobalApplication.getAppContext();

    Button favoriteButton;

    private MovieViewModel movieViewModel;
    private TvShowViewModel tvShowViewModel;

    private Favorite favorite = new Favorite();

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
//        tv_rating = findViewById(R.id.tv_movie_rating_content);
        ratingBar = findViewById(R.id.ratingBar);
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
        ratingBar.setRating((float) movieDetail.getVote_average()/2.0f);

        favorite.setDate_available(movieDetail.getRelease_date());
        favorite.setHomepage(movieDetail.getHomepage());
        favorite.setThingsId(movieDetail.getId());
        favorite.setType(0);
        favorite.setPoster_path(movieDetail.getPoster_path());
        favorite.setTitle(movieDetail.getOriginal_title());
        favorite.setVote_average(movieDetail.getVote_average());

        Glide
                .with(getBaseContext())
                .load(Constants.IMAGE_ROOT_LARGE + movieDetail.getPoster_path())
                .into(imgView_poster);

        CheckThings checkThings = new CheckThings();
        checkThings.execute();
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
        ratingBar.setRating((float) tvShowDetail.getVote_average()/2.0f);

        favorite.setTitle(tvShowDetail.getName());
        favorite.setPoster_path(tvShowDetail.getPoster_path());
        favorite.setVote_average(tvShowDetail.getVote_average());
        favorite.setType(1);
        favorite.setThingsId(tvShowDetail.getId());
        favorite.setHomepage(tvShowDetail.getHomepage());
        favorite.setDate_available(tvShowDetail.getFirst_air_date());

        Glide
                .with(getBaseContext())
                .load(Constants.IMAGE_ROOT_LARGE + tvShowDetail.getPoster_path())
                .into(imgView_poster);

        CheckThings checkThings = new CheckThings();
        checkThings.execute();
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

    private class CheckThings extends AsyncTask<Void, Void, Boolean>{
        @Override
        protected Boolean doInBackground(Void... voids) {
            int count = DatabaseClient.getInstance(getApplicationContext())
                    .getAppDatabase()
                    .getFavoriteDAO()
                    .checkFavorite(category, idThings);

            return count>0;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            if (aBoolean)
            {
                favoriteButton.setText(getResources().getString(R.string.title_unfavorite));
                favoriteButton.setBackgroundColor(getResources().getColor(R.color.colorUnFavorite));
                favoriteButton.setTextColor(getResources().getColor(R.color.colorWhite));
                favoriteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UnsetFavorite unsetFavorite = new UnsetFavorite();
                        unsetFavorite.execute();
                    }
                });
            }
            else
            {
                favoriteButton.setText(getResources().getString(R.string.title_favorite));
                favoriteButton.setBackgroundColor(getResources().getColor(R.color.colorFavorite));
                favoriteButton.setTextColor(getResources().getColor(R.color.colorGold));
                favoriteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SetFavorite setFavorite = new SetFavorite();
                        setFavorite.execute();
                    }
                });
            }
        }
    }

    private class SetFavorite extends AsyncTask<Void, Void, Boolean>{

        @Override
        protected Boolean doInBackground(Void... voids) {
            Long l = DatabaseClient.getInstance(getApplicationContext())
                    .getAppDatabase()
                    .getFavoriteDAO()
                    .insertFavorite(favorite);

            return (l!=null);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean)
            {
                favoriteButton.setText(getResources().getString(R.string.title_unfavorite));
                favoriteButton.setBackgroundColor(getResources().getColor(R.color.colorUnFavorite));
                favoriteButton.setTextColor(getResources().getColor(R.color.colorWhite));
                DynamicToast.makeSuccess(context, "Set as Favorite Success", 3).show();
                Log.d(TAG, "Set Favorite success");
            }
            else
            {
                Log.d(TAG, "Failed to set Favorite");
            }
        }
    }

    private class UnsetFavorite extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            DatabaseClient.getInstance(getApplicationContext())
                    .getAppDatabase()
                    .getFavoriteDAO()
                    .deleteFavorite(category, idThings);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            favoriteButton.setText(getResources().getString(R.string.title_favorite));
            favoriteButton.setBackgroundColor(getResources().getColor(R.color.colorFavorite));
            favoriteButton.setTextColor(getResources().getColor(R.color.colorGold));
            DynamicToast.makeSuccess(context, "Delete Favorite Success", 3).show();
            Log.d(TAG, "Delete Favorite Success");
        }
    }

}

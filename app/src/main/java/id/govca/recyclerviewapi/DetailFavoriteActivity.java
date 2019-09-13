package id.govca.recyclerviewapi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import id.govca.recyclerviewapi.entity.Favorite;
import id.govca.recyclerviewapi.helper.Constants;
import id.govca.recyclerviewapi.viewmodel.FavoriteItemViewModel;
import id.govca.recyclerviewapi.viewmodel.MovieViewModel;
import id.govca.recyclerviewapi.viewmodel.TvShowViewModel;
import io.reactivex.disposables.Disposable;

public class DetailFavoriteActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();
    private View mProgressView;
    private View mScrollView;

    private int idThings, category;
    private TextView tv_name, tv_genres, tv_homepage, tv_year, tv_synopsis;
    private ImageView imgView_poster;

    private RatingBar ratingBar;

    Context context = GlobalApplication.getAppContext();

    Button favoriteButton;

    private FavoriteItemViewModel favoriteItemViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_favorite);

        Bundle b = getIntent().getExtras();
        idThings = b.getInt("Movie_ID");
        category = b.getInt("Category");

        mProgressView = findViewById(R.id.progressBarDetail);
        mScrollView = findViewById(R.id.scrollViewDetail);

        tv_name = findViewById(R.id.tv_movie_name);
        ratingBar = findViewById(R.id.ratingBar);
        tv_genres = findViewById(R.id.tv_genres_content);
        tv_homepage = findViewById(R.id.tv_homepage_content);
        tv_year = findViewById(R.id.tv_movie_year_content);
        tv_synopsis = findViewById(R.id.tv_movie_synopsis_content);
        imgView_poster = findViewById(R.id.imgView_poster);
        favoriteButton = findViewById(R.id.btn_favorite);

        favoriteItemViewModel = ViewModelProviders.of(this).get(FavoriteItemViewModel.class);
        favoriteItemViewModel.getFavoriteMutableLiveData().observe(this, getFavorite);

        favoriteItemViewModel.setFavoriteMutableLiveData(category, idThings);
    }

    private Observer<Favorite> getFavorite = new Observer<Favorite>() {
        @Override
        public void onChanged(Favorite favorite) {
            if(favorite!=null)
            {
                pseudoAdapterFavorite(favorite);
                showLoading(false);
            }
        }
    };

    private void pseudoAdapterFavorite(final Favorite favorite){
        tv_name.setText(favorite.getTitle());
        tv_homepage.setText(favorite.getHomepage());
        tv_synopsis.setText(favorite.getSynopsis());
        tv_year.setText(favorite.getDate_available());
        ratingBar.setRating((float) favorite.getVote_average()/2.0f);

        Glide
                .with(getBaseContext())
                .load(Constants.IMAGE_ROOT_LARGE + favorite.getPoster_path())
                .into(imgView_poster);
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
}

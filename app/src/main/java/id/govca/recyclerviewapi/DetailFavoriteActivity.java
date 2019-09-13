package id.govca.recyclerviewapi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import id.govca.recyclerviewapi.viewmodel.MovieViewModel;
import id.govca.recyclerviewapi.viewmodel.TvShowViewModel;

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

    private MovieViewModel movieViewModel;
    private TvShowViewModel tvShowViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_favorite);
    }
}

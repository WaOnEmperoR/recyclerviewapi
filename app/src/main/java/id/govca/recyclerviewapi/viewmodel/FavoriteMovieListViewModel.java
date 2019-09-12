package id.govca.recyclerviewapi.viewmodel;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import id.govca.recyclerviewapi.DatabaseClient;
import id.govca.recyclerviewapi.GlobalApplication;
import id.govca.recyclerviewapi.entity.Favorite;

public class FavoriteMovieListViewModel extends ViewModel {

    private MutableLiveData<List<Favorite>> listFavoriteMovies = new MutableLiveData<>();
    private final String TAG = this.getClass().getSimpleName();

    Context context = GlobalApplication.getAppContext();

    public MutableLiveData<List<Favorite>> getListFavoriteMovies() {
        return listFavoriteMovies;
    }

    public void setListFavoriteMovies() {
        Log.d(TAG, "Calling Set List Favorite Movies");

        new GetFavoriteMovie().execute();
    }

    private class GetFavoriteMovie extends AsyncTask<Void, Void, List<Favorite>>{

        @Override
        protected List<Favorite> doInBackground(Void... voids) {
            return DatabaseClient.getInstance(context)
                    .getAppDatabase()
                    .getFavoriteDAO()
                    .fetchFavoriteMovies();
        }

        @Override
        protected void onPostExecute(List<Favorite> favorites) {
            super.onPostExecute(favorites);
            getListFavoriteMovies().setValue(favorites);
        }
    }
}

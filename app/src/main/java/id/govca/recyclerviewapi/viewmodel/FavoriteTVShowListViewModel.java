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

public class FavoriteTVShowListViewModel extends ViewModel {
    private MutableLiveData<List<Favorite>> listFavoriteTVShows = new MutableLiveData<>();
    private final String TAG = this.getClass().getSimpleName();

    Context context = GlobalApplication.getAppContext();

    public MutableLiveData<List<Favorite>> getListFavoriteTVShows() {
        return listFavoriteTVShows;
    }

    public void setListFavoriteTVShows() {
        Log.d(TAG, "Calling Set List Favorite TV Shows");

        new GetFavoriteTVShow().execute();
    }

    private class GetFavoriteTVShow extends AsyncTask<Void, Void, List<Favorite>>{

        @Override
        protected List<Favorite> doInBackground(Void... voids) {
            return DatabaseClient.getInstance(context)
                    .getAppDatabase()
                    .getFavoriteDAO()
                    .fetchFavoriteTVShows();
        }

        @Override
        protected void onPostExecute(List<Favorite> favorites) {
            super.onPostExecute(favorites);
            getListFavoriteTVShows().setValue(favorites);
        }
    }
}

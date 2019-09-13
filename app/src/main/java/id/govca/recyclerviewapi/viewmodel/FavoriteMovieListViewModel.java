package id.govca.recyclerviewapi.viewmodel;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.util.List;
import java.util.concurrent.Callable;

import id.govca.recyclerviewapi.DatabaseClient;
import id.govca.recyclerviewapi.GlobalApplication;
import id.govca.recyclerviewapi.entity.Favorite;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class FavoriteMovieListViewModel extends ViewModel {

    private MutableLiveData<List<Favorite>> listFavoriteMovies = new MutableLiveData<>();
    private CompositeDisposable disposable = new CompositeDisposable();
    private final String TAG = this.getClass().getSimpleName();

    Context context = GlobalApplication.getAppContext();

    public MutableLiveData<List<Favorite>> getListFavoriteMovies() {
        return listFavoriteMovies;
    }

    public void setListFavoriteMovies() {
        Log.d(TAG, "Calling Set List Favorite Movies");

        ObserveListFavorites();
    }

    private Observable<List<Favorite>> getFavorites(){
        Observable<List<Favorite>> observable = Observable.fromCallable(new Callable<List<Favorite>>() {
            @Override
            public List<Favorite> call() throws Exception {
                return DatabaseClient.getInstance(context)
                        .getAppDatabase()
                        .getFavoriteDAO()
                        .fetchFavoriteMovies();
            }
        });

        return observable;
    }

    private void ObserveListFavorites()
    {
        Observable<List<Favorite>> favoritesObservable = getFavorites();

        disposable.add(
                favoritesObservable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableObserver<List<Favorite>>() {
                            @Override
                            public void onNext(List<Favorite> favorites) {
                                listFavoriteMovies.setValue(favorites);
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(TAG, "Observable error : " + e.getMessage());
                                DynamicToast.makeError(context, e.getMessage(), 5).show();
                            }

                            @Override
                            public void onComplete() {
                                Log.d(TAG, "onComplete from RxJava");
                                DynamicToast.makeSuccess(context, "Finished Loading Data", 3).show();
                                this.dispose();
                            }
                        })
        );
    }

}

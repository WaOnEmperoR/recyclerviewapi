package id.govca.recyclerviewapi.viewmodel;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.util.concurrent.Callable;

import id.govca.recyclerviewapi.DatabaseClient;
import id.govca.recyclerviewapi.GlobalApplication;
import id.govca.recyclerviewapi.entity.Favorite;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class FavoriteItemViewModel extends ViewModel {

    private MutableLiveData<Favorite> favoriteMutableLiveData = new MutableLiveData<>();
    private CompositeDisposable disposable = new CompositeDisposable();
    private final String TAG = this.getClass().getSimpleName();

    Context context = GlobalApplication.getAppContext();

    public MutableLiveData<Favorite> getFavoriteMutableLiveData() {
        return favoriteMutableLiveData;
    }

    public void setFavoriteMutableLiveData(int type, int idThings) {
        Log.d(TAG, "Calling Set Favorite Detail");
        ObserveFavoriteItem(type, idThings);
    }

    private Observable<Favorite> getFavoriteItem(final int type, final int idThings){
        Observable<Favorite> observable = Observable.fromCallable(new Callable<Favorite>() {
            @Override
            public Favorite call() throws Exception {
                return DatabaseClient.getInstance(context)
                        .getAppDatabase()
                        .getFavoriteDAO()
                        .fetchSingleFavorite(type, idThings);
            }
        });

        return observable;
    }

    private void ObserveFavoriteItem(int type, int idThings)
    {
        Observable<Favorite> favoriteObservable = getFavoriteItem(type, idThings);

        disposable.add(
                favoriteObservable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableObserver<Favorite>() {
                        @Override
                        public void onNext(Favorite favorite) {
                            getFavoriteMutableLiveData().setValue(favorite);
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

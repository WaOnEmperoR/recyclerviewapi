package id.govca.recyclerviewapi.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.util.Locale;

import id.govca.recyclerviewapi.DetailActivity;
import id.govca.recyclerviewapi.GlobalApplication;
import id.govca.recyclerviewapi.adapter.ListMovieAdapter;
import id.govca.recyclerviewapi.helper.Constants;
import id.govca.recyclerviewapi.pojo.Movie;
import id.govca.recyclerviewapi.pojo.MovieList;
import id.govca.recyclerviewapi.rest.ApiClient;
import id.govca.recyclerviewapi.rest.ApiInterface;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MovieListViewModel extends ViewModel {
    private MutableLiveData<MovieList> listMovies = new MutableLiveData<>();
    private CompositeDisposable disposable = new CompositeDisposable();
    private final String TAG = this.getClass().getSimpleName();

    Context context = GlobalApplication.getAppContext();

    public MutableLiveData<MovieList> getListMovies() {
        return listMovies;
    }

    public void setListMovies(String param_lang) {
        Log.d(TAG, "Calling Set List Movies");
        ObserveMovie(param_lang);
    }

    private Observable<MovieList> getMovieListObs(String param_lang){
        final ApiInterface mApiService = ApiClient.getClient().create(ApiInterface.class);

        return mApiService.RxGetMovieList(Constants.API_KEY, param_lang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private void ObserveMovie(String param_lang)
    {
        Observable<MovieList> movieListObservable = getMovieListObs(param_lang);

        disposable.add(
                movieListObservable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableObserver<MovieList>() {
                            @Override
                            public void onNext(MovieList movieList) {
                                for (int i=0; i<movieList.getMovieArrayList().size(); i++)
                                {
                                    Log.d(TAG, movieList.getMovieArrayList().get(i).getTitle());
                                }

                                listMovies.setValue(movieList);
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

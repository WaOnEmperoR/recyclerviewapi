package id.govca.recyclerviewapi.viewmodel;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import id.govca.recyclerviewapi.GlobalApplication;
import id.govca.recyclerviewapi.helper.Constants;
import id.govca.recyclerviewapi.pojo.MovieDetail;
import id.govca.recyclerviewapi.rest.ApiClient;
import id.govca.recyclerviewapi.rest.ApiInterface;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MovieViewModel extends ViewModel {
    private MutableLiveData<MovieDetail> movieDetail = new MutableLiveData<>();
    private CompositeDisposable disposable = new CompositeDisposable();
    private final String TAG = this.getClass().getSimpleName();

    Context context = GlobalApplication.getAppContext();

    public MutableLiveData<MovieDetail> getMovieDetail() {
        return movieDetail;
    }

    public void setMovieDetail(int id, String param_lang) {
        Log.d(TAG, "Calling Set Movie Detail");
        ObserveMovieDetail(id, param_lang);
    }

    private Observable<MovieDetail> getMovieDetailObs(int idThings, String language){
        final ApiInterface mApiService = ApiClient.getClient().create(ApiInterface.class);

        return mApiService.RxMovieDetails(idThings, Constants.API_KEY, language)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private void ObserveMovieDetail(int idThings, String param_lang)
    {
        Observable<MovieDetail> movieDetailObservable = getMovieDetailObs(idThings, param_lang);

        disposable.add(
                movieDetailObservable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableObserver<MovieDetail>() {
                            @Override
                            public void onNext(MovieDetail movieDetail) {
                                MovieViewModel.this.movieDetail.setValue(movieDetail);
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

package id.govca.recyclerviewapi.viewmodel;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import id.govca.recyclerviewapi.GlobalApplication;
import id.govca.recyclerviewapi.helper.Constants;
import id.govca.recyclerviewapi.pojo.MovieDetail;
import id.govca.recyclerviewapi.pojo.TVShow;
import id.govca.recyclerviewapi.pojo.TVShowDetail;
import id.govca.recyclerviewapi.rest.ApiClient;
import id.govca.recyclerviewapi.rest.ApiInterface;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class TvShowViewModel extends ViewModel {
    private MutableLiveData<TVShowDetail> tvShowDetailMutableLiveData = new MutableLiveData<>();
    private CompositeDisposable disposable = new CompositeDisposable();
    private final String TAG = this.getClass().getSimpleName();

    Context context = GlobalApplication.getAppContext();

    public MutableLiveData<TVShowDetail> getTvShowDetail() {
        return tvShowDetailMutableLiveData;
    }

    public void setTvShowDetail(int id, String param_lang) {
        Log.d(TAG, "Calling Set Movie Detail");
        ObserveTVShowDetail(id, param_lang);
    }

    private Observable<TVShowDetail> getTVShowDetailObs(int idThings, String language){
        final ApiInterface mApiService = ApiClient.getClient().create(ApiInterface.class);

        return mApiService.RxTVShowDetails(idThings, Constants.API_KEY, language)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private void ObserveTVShowDetail(int idThings, String param_lang) {
        Observable<TVShowDetail> tvShowDetailObservable = getTVShowDetailObs(idThings, param_lang);

        disposable.add(
                tvShowDetailObservable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableObserver<TVShowDetail>() {
                        @Override
                        public void onNext(TVShowDetail tvShowDetail) {
                            tvShowDetailMutableLiveData.setValue(tvShowDetail);
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

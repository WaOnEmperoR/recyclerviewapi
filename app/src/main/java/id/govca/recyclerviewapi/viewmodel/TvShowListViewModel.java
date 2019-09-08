package id.govca.recyclerviewapi.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import id.govca.recyclerviewapi.helper.Constants;
import id.govca.recyclerviewapi.pojo.MovieList;
import id.govca.recyclerviewapi.pojo.TVShowList;
import id.govca.recyclerviewapi.rest.ApiClient;
import id.govca.recyclerviewapi.rest.ApiInterface;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class TvShowListViewModel extends ViewModel {
    private MutableLiveData<TVShowList> listTvShows = new MutableLiveData<>();
    private CompositeDisposable disposable = new CompositeDisposable();
    private final String TAG = this.getClass().getSimpleName();

    public MutableLiveData<TVShowList> getListTvShows() {
        return listTvShows;
    }

    public void setListTvShows(String param_lang) {
        Log.d(TAG, "Calling Set List TV Shows");
        ObserveTVShow(param_lang);
    }

    private Observable<TVShowList> getTVShowListObs(String param_lang)
    {
        final ApiInterface mApiService = ApiClient.getClient().create(ApiInterface.class);

        return mApiService.RxGetTVShowList(Constants.API_KEY, param_lang, 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private void ObserveTVShow (String param_lang)
    {
        Observable<TVShowList> tvShowListObservable = getTVShowListObs(param_lang);

        disposable.add(
                tvShowListObservable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableObserver<TVShowList>() {
                        @Override
                        public void onNext(TVShowList tvShowList) {
                            for (int i=0; i<tvShowList.getTvShowArrayList().size(); i++)
                            {
                                Log.d(TAG, tvShowList.getTvShowArrayList().get(i).getName());
                            }

                            listTvShows.setValue(tvShowList);
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e(TAG, "Observable error : " + e.getMessage());
                        }

                        @Override
                        public void onComplete() {
                            Log.d(TAG, "onComplete from RxJava");
                            this.dispose();
                        }
                    })
        );
    }
}

package id.govca.recyclerviewapi.viewmodel;

import androidx.lifecycle.MutableLiveData;

import id.govca.recyclerviewapi.pojo.TVShowList;
import io.reactivex.disposables.CompositeDisposable;

public class TvShowListViewModel {
    private MutableLiveData<TVShowList> listTvShows = new MutableLiveData<>();
    private CompositeDisposable disposable = new CompositeDisposable();
    private final String TAG = this.getClass().getSimpleName();

    public MutableLiveData<TVShowList> getListTvShows() {
        return listTvShows;
    }

    public void setListTvShows(MutableLiveData<TVShowList> listTvShows) {
        this.listTvShows = listTvShows;
    }
}

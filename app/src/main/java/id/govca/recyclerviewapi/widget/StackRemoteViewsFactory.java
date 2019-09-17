package id.govca.recyclerviewapi.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import androidx.lifecycle.MutableLiveData;

import com.bumptech.glide.Glide;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import id.govca.recyclerviewapi.GlobalApplication;
import id.govca.recyclerviewapi.R;
import id.govca.recyclerviewapi.entity.Favorite;
import id.govca.recyclerviewapi.helper.Constants;
import id.govca.recyclerviewapi.helper.DatabaseClient;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private final Context mContext;
    private CompositeDisposable disposable = new CompositeDisposable();
    private final String TAG = this.getClass().getSimpleName();

    private Context context = GlobalApplication.getAppContext();

    private List<String> listFavoriteMovies = new ArrayList<>();

    StackRemoteViewsFactory(Context context) {
        mContext = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        getFavoritesObservable();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return listFavoriteMovies.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);

//        Bitmap theBitmap = null;
//        try {
//            theBitmap = Glide.
//                    with(context).
//                    asBitmap().
//                    load(listFavoriteMovies.get(position)).
//                    into(150, 225). // Width and height
//                    get();
//        } catch (ExecutionException e) {
//            Log.e(TAG, e.getMessage());
//        } catch (InterruptedException e) {
//            Log.e(TAG, e.getMessage());
//        }

        Log.d(TAG, listFavoriteMovies.get(position));
        Bitmap bitmap = callHttpConnection(listFavoriteMovies.get(position));

//        rv.setImageViewBitmap(R.id.imageView, mWidgetItems.get(position));
        rv.setImageViewBitmap(R.id.imageView, bitmap);
        Bundle extras = new Bundle();
        extras.putInt(MovieStackWidget.EXTRA_ITEM, position);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent);
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    private Bitmap callHttpConnection(String url) {
        URL ulrn = null;
        Bitmap bmp = null;

        HttpURLConnection con = null;
        try {
            ulrn = new URL(url);
            con = (HttpURLConnection) ulrn.openConnection();
            InputStream is = con.getInputStream();
            bmp = BitmapFactory.decodeStream(is);

        } catch (MalformedURLException e) {
            Log.e(TAG, e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        } finally {
            if (con != null)
                con.disconnect();
        }

        return bmp;
    }

    private Observable<List<Favorite>> getFavorites() {
        Observable<List<Favorite>> observable = Observable.fromCallable(new Callable<List<Favorite>>() {
            @Override
            public List<Favorite> call() {
                return DatabaseClient.getInstance(context)
                        .getAppDatabase()
                        .getFavoriteDAO()
                        .fetchFavoriteAll();
            }
        });

        return observable;
    }

    private void getFavoritesObservable(){
        Observable<List<Favorite>> favoriteObservable = getFavorites();

        disposable.add(
                favoriteObservable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableObserver<List<Favorite>>() {
                        @Override
                        public void onNext(List<Favorite> favorites) {
                            for(int i=0; i<favorites.size(); i++)
                            {
                                String path = favorites.get(i).getPoster_path().substring(1);
                                String full_path = Constants.IMAGE_ROOT_SMALL + path;

                                listFavoriteMovies.add(full_path);
                            }
                            Log.d(TAG, String.valueOf(listFavoriteMovies.size()));
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e(TAG, e.getMessage());
                        }

                        @Override
                        public void onComplete() {
                            Log.d(TAG, "onComplete from RXJava");
                            this.dispose();
                        }
                    })
        );
    }

}

package id.govca.recyclerviewapi.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import id.govca.recyclerviewapi.R;
import id.govca.recyclerviewapi.entity.Favorite;
import id.govca.recyclerviewapi.helper.Constants;
import id.govca.recyclerviewapi.helper.DatabaseClient;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory{
    private final List<Bitmap> mWidgetItems = new ArrayList<>();
    private List<String> listFavoriteMovies = new ArrayList<>();

    private CompositeDisposable disposable = new CompositeDisposable();
    private final Context mContext;

    public static final Uri CONTENT_URI = new Uri.Builder().scheme("content")
            .authority("id.govca.recyclerviewapi")
            .appendPath("favorite")
            .build();

    private final String TAG = this.getClass().getSimpleName();

    StackRemoteViewsFactory(Context context) {
        mContext = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        Cursor cursor;

        final long identityToken = Binder.clearCallingIdentity();

        cursor = mContext.getContentResolver().query(CONTENT_URI, null, null, null, null);

        while (cursor.moveToNext())
        {
            String poster_path = cursor.getString(cursor.getColumnIndexOrThrow("poster_path"));
            String fullPath = Constants.IMAGE_ROOT_SMALL + poster_path;

            Log.d(TAG, fullPath);

            listFavoriteMovies.add(fullPath);
        }

        Binder.restoreCallingIdentity(identityToken);
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
        Log.d(TAG, "enter " + position);
        Bitmap theBitmap = null;
        try {
            theBitmap = Glide.
                    with(mContext).
                    asBitmap().
                    load(listFavoriteMovies.get(position)).
                    into(150, 225). // Width and height
                    get();
        } catch (ExecutionException e) {
            Log.e(TAG, e.getMessage());
        } catch (InterruptedException e) {
            Log.e(TAG, e.getMessage());
        }
        rv.setImageViewBitmap(R.id.imageView, theBitmap);

        Bundle extras = new Bundle();
        extras.putInt(ImagesBannerWidget.EXTRA_ITEM, position);
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

    private Observable<List<Favorite>> getFavorites() {
        Observable<List<Favorite>> observable = Observable.fromCallable(new Callable<List<Favorite>>() {
            @Override
            public List<Favorite> call() {
                return DatabaseClient.getInstance(mContext)
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

                                    Log.d(TAG, full_path);
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

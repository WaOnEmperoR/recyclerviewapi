package id.govca.recyclerviewapi.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import id.govca.recyclerviewapi.DatabaseClient;
import id.govca.recyclerviewapi.GlobalApplication;
import id.govca.recyclerviewapi.R;
import id.govca.recyclerviewapi.entity.Favorite;
import id.govca.recyclerviewapi.helper.Constants;
import id.govca.recyclerviewapi.pojo.Movie;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class ListFavoriteMovieAdapter extends RecyclerView.Adapter<ListFavoriteMovieAdapter.ListViewHolder> {

    private ArrayList<Favorite> listFavoriteMovie = new ArrayList<>();

    Context context = GlobalApplication.getAppContext();
    private final String TAG = this.getClass().getSimpleName();

    private ListFavoriteMovieAdapter.OnItemClickCallback onItemClickCallback;
    public void setOnItemClickCallback(ListFavoriteMovieAdapter.OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public interface OnItemClickCallback {
        void onItemClicked(Favorite data);
    }

    public ListFavoriteMovieAdapter()
    {
    }

    public void setData(ArrayList<Favorite> items) {
        listFavoriteMovie.clear();
        listFavoriteMovie.addAll(items);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite, parent, false);
        return new ListFavoriteMovieAdapter.ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListViewHolder holder, int position) {
        final Favorite favorite = listFavoriteMovie.get(position);

        Glide
                .with(holder.itemView.getContext())
                .load(Constants.IMAGE_ROOT_SMALL + favorite.getPoster_path())
                .into(holder.img_poster);

        holder.tv_movie_name.setText(favorite.getTitle());
        holder.tv_year.setText(favorite.getDate_available());
        holder.tv_rating.setText(String.valueOf(favorite.getVote_average()));
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObserveFavoriteItemDelete(0, favorite.getThingsId());
                removeAt(holder.getAdapterPosition());
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickCallback.onItemClicked(listFavoriteMovie.get(holder.getAdapterPosition()));
            }
        });
    }

    public void removeAt(int position) {
        listFavoriteMovie.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, listFavoriteMovie.size());
    }

    @Override
    public int getItemCount() {
        return listFavoriteMovie.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        ImageView img_poster;
        TextView tv_rating, tv_year, tv_movie_name;
        Button btnDelete;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);

            img_poster = itemView.findViewById(R.id.imgView_poster);
            tv_rating = itemView.findViewById(R.id.tv_show_rating);
            tv_year = itemView.findViewById(R.id.tv_show_year);
            tv_movie_name = itemView.findViewById(R.id.tv_movie_title);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }

    }

    private Observable<Void> deleteFavoriteItem(final int type, final int idThings){
         Observable<Void> observable = Observable.fromCallable(new Callable<Void>() {
             @Override
             public Void call() throws Exception {
                 DatabaseClient.getInstance(context)
                         .getAppDatabase()
                         .getFavoriteDAO()
                         .deleteFavorite(type, idThings);

                 return null;
             }
         });

        return observable;
    }

    private void ObserveFavoriteItemDelete(int type, int idThings)
    {
        Observable<Void> deleteObservable = deleteFavoriteItem(type, idThings);

        CompositeDisposable disposable = new CompositeDisposable();

        disposable.add(
                deleteObservable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableObserver<Void>() {
                            @Override
                            public void onNext(Void aVoid) {

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

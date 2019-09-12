package id.govca.recyclerviewapi.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import id.govca.recyclerviewapi.R;
import id.govca.recyclerviewapi.entity.Favorite;
import id.govca.recyclerviewapi.helper.Constants;
import id.govca.recyclerviewapi.pojo.Movie;

public class ListFavoriteMovieAdapter extends RecyclerView.Adapter<ListFavoriteMovieAdapter.ListViewHolder> {

    private ArrayList<Favorite> listFavoriteMovie = new ArrayList<>();

    private ListMovieAdapter.OnItemClickCallback onItemClickCallback;
    public void setOnItemClickCallback(ListMovieAdapter.OnItemClickCallback onItemClickCallback) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new ListFavoriteMovieAdapter.ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        Favorite favorite = listFavoriteMovie.get(position);

        Glide
                .with(holder.itemView.getContext())
                .load(Constants.IMAGE_ROOT_SMALL + favorite.getPoster_path())
                .into(holder.img_poster);

        holder.tv_movie_name.setText(favorite.getTitle());
        holder.tv_year.setText(favorite.getDate_available());
        holder.tv_rating.setText(String.valueOf(favorite.getVote_average()));
    }

    @Override
    public int getItemCount() {
        return listFavoriteMovie.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        ImageView img_poster;
        TextView tv_rating, tv_year, tv_movie_name;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);

            img_poster = itemView.findViewById(R.id.imgView_poster);
            tv_rating = itemView.findViewById(R.id.tv_show_rating);
            tv_year = itemView.findViewById(R.id.tv_show_year);
            tv_movie_name = itemView.findViewById(R.id.tv_movie_title);
        }
    }
}

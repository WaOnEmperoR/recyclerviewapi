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
import id.govca.recyclerviewapi.helper.Constants;
import id.govca.recyclerviewapi.pojo.Movie;

public class ListMovieAdapter extends RecyclerView.Adapter<ListMovieAdapter.ListViewHolder>{
    private ArrayList<Movie> listMovie;

    private OnItemClickCallback onItemClickCallback;
    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public interface OnItemClickCallback {
        void onItemClicked(Movie data);
    }

    public ListMovieAdapter(ArrayList<Movie> list)
    {
        this.listMovie = list;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListMovieAdapter.ListViewHolder holder, int position) {
        Movie movie = listMovie.get(position);

        Glide
                .with(holder.itemView.getContext())
                .load(Constants.IMAGE_ROOT_SMALL + movie.getPoster_path())
                .into(holder.img_poster);

        holder.tv_movie_name.setText(movie.getTitle());
        holder.tv_year.setText(movie.getRelease_date());
        holder.tv_rating.setText(String.valueOf(movie.getVote_average()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickCallback.onItemClicked(listMovie.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return listMovie.size();
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

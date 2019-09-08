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
import id.govca.recyclerviewapi.pojo.TVShow;

public class ListTvShowAdapter extends RecyclerView.Adapter<ListTvShowAdapter.ListViewHolder>{
    private ArrayList<TVShow> listTvShow = new ArrayList<>();

    private ListTvShowAdapter.OnItemClickCallback onItemClickCallback;
    public void setOnItemClickCallback(ListTvShowAdapter.OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public ListTvShowAdapter()
    {
    }

    public void setData(ArrayList<TVShow> items) {
        listTvShow.clear();
        listTvShow.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new ListTvShowAdapter.ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListTvShowAdapter.ListViewHolder holder, int position) {
        TVShow tvShow = listTvShow.get(position);

        Glide
                .with(holder.itemView.getContext())
                .load(Constants.IMAGE_ROOT_SMALL + tvShow.getPoster_path())
                .into(holder.img_poster);

        holder.tv_movie_name.setText(tvShow.getName());
        holder.tv_year.setText(tvShow.getFirst_air_date());
        holder.tv_rating.setText(String.valueOf(tvShow.getVote_average()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickCallback.onItemClicked(listTvShow.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return listTvShow.size();
    }

    public interface OnItemClickCallback {
        void onItemClicked(TVShow data);
    }

    public ListTvShowAdapter(ArrayList<TVShow> list)
    {
        this.listTvShow = list;
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

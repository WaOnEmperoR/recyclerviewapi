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

public class ListFavoriteTVShowAdapter extends RecyclerView.Adapter<ListFavoriteTVShowAdapter.ListViewHolder>{

    private ArrayList<Favorite> listFavoriteTVShow = new ArrayList<>();

    private ListFavoriteTVShowAdapter.OnItemClickCallback onItemClickCallback;
    public void setOnItemClickCallback(ListFavoriteTVShowAdapter.OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public interface OnItemClickCallback {
        void onItemClicked(Favorite data);
    }

    public ListFavoriteTVShowAdapter()
    {
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new ListFavoriteTVShowAdapter.ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListViewHolder holder, int position) {
        Favorite favorite = listFavoriteTVShow.get(position);

        Glide
                .with(holder.itemView.getContext())
                .load(Constants.IMAGE_ROOT_SMALL + favorite.getPoster_path())
                .into(holder.img_poster);

        holder.tv_movie_name.setText(favorite.getTitle());
        holder.tv_year.setText(favorite.getDate_available());
        holder.tv_rating.setText(String.valueOf(favorite.getVote_average()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickCallback.onItemClicked(listFavoriteTVShow.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return listFavoriteTVShow.size();
    }

    public void setData(ArrayList<Favorite> items) {
        listFavoriteTVShow.clear();
        listFavoriteTVShow.addAll(items);
        notifyDataSetChanged();
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

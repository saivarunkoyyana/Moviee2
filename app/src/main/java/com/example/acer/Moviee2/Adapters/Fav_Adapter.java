package com.example.acer.Moviee2.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.acer.Moviee2.ModelClass.EntityData;
import com.example.acer.Moviee2.UI.FavouriteDetails;
import com.example.acer.Moviee2.R;
import com.example.acer.Moviee2.UI.MainActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Fav_Adapter extends RecyclerView.Adapter<Fav_Adapter.ViewHolder> {
    private final Context context;
    private final List<EntityData> entityDataList;

    public Fav_Adapter(MainActivity favourite, List<EntityData> entityData) {
        this.context = favourite;
        this.entityDataList = entityData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.row, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Picasso.with(context).load(entityDataList.get(position).getMoviePoster()).into(viewHolder.imageView);

    }

    @Override
    public int getItemCount() {
        return entityDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageview);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();


                    Intent i = new Intent(context, FavouriteDetails.class);
                    i.putExtra("image", entityDataList.get(position).getMoviePoster());
                    i.putExtra("id", "" + entityDataList.get(position).getMovieId());
                    i.putExtra("title", entityDataList.get(position).getMovieName());
                    i.putExtra("overview", entityDataList.get(position).getMovieDesc());
                    i.putExtra("release_date", entityDataList.get(position).getMovieRelease());
                    i.putExtra("vote_avg", entityDataList.get(position).getMovieVoteAvg());
                    i.putExtra("position", position);
                    i.putExtra("backdrop", entityDataList.get(position).getBackdrop());
                    context.startActivity(i);
                }
            });
        }

    }
}

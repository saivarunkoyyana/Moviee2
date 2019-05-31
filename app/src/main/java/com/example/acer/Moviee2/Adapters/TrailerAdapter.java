package com.example.acer.Moviee2.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.acer.Moviee2.R;
import com.example.acer.Moviee2.ModelClass.TrailerModel;
import com.example.acer.Moviee2.UI.DetailActivity;

import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHoler> {
    private final Context context;
    private final List<TrailerModel> trailerModelList;

    public TrailerAdapter(DetailActivity detailActivity, List<TrailerModel> trailerModelList) {
        this.context = detailActivity;
        this.trailerModelList = trailerModelList;

    }

    @NonNull
    @Override
    public TrailerAdapter.ViewHoler onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.trailer, viewGroup, false);


        return new ViewHoler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerAdapter.ViewHoler viewHoler, int position) {
        viewHoler.trailertitle.setText(trailerModelList.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return trailerModelList.size();
    }

    public class ViewHoler extends RecyclerView.ViewHolder {
        final TextView trailertitle;

        ViewHoler(@NonNull View itemView) {
            super(itemView);
            trailertitle = itemView.findViewById(R.id.trailer_text);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        String key = trailerModelList.get(pos).getKey();
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + key));
                        context.startActivity(intent);
                    }

                }
            });
        }
    }
}

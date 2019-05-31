package com.example.acer.Moviee2.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.acer.Moviee2.R;
import com.example.acer.Moviee2.ModelClass.ReviewModel;
import com.example.acer.Moviee2.UI.DetailActivity;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    private final Context context;
    private final List<ReviewModel> reviewModelList;

    public ReviewAdapter(DetailActivity detailActivity, List<ReviewModel> reviewModelList) {
        this.context = detailActivity;
        this.reviewModelList = reviewModelList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.review, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.author.setText(reviewModelList.get(position).getAuthor());
        viewHolder.review.setText(reviewModelList.get(position).getContent());


    }

    @Override
    public int getItemCount() {
        return reviewModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView review;
        final TextView author;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            review = itemView.findViewById(R.id.review_tv);
            author = itemView.findViewById(R.id.author);

        }
    }
}

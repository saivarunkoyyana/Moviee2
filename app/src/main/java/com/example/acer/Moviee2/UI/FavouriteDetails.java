package com.example.acer.Moviee2.UI;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.acer.Moviee2.ModelClass.EntityData;
import com.example.acer.Moviee2.R;
import com.example.acer.Moviee2.ViewModel;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.marcoscg.materialtoast.MaterialToast;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavouriteDetails extends AppCompatActivity {
    private ViewModel viewModel;
    private LikeButton button;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_details);
        ImageView backdropimage = findViewById(R.id.backdrop);
        final Context context = this;
        ImageView poster = findViewById(R.id.poster);
        TextView title_tv = findViewById(R.id.title);
        TextView releasedate_tv = findViewById(R.id.releasedate);
        TextView overview_tv = findViewById(R.id.overview);
        TextView vote_tv = findViewById(R.id.vote);
        button = findViewById(R.id.favbutton);
        viewModel = ViewModelProviders.of(this).get(ViewModel.class);
        checkfavourite();


        final String title = getIntent().getStringExtra("title");
        final String releasedate = getIntent().getStringExtra("release_date");
        final String overview = getIntent().getStringExtra("overview");
        final String vote = getIntent().getStringExtra("vote_avg");
        id = getIntent().getStringExtra("id");
        final String image = getIntent().getStringExtra("image");
        final String backdrop = getIntent().getStringExtra("backdrop");

        Picasso.with(context).load(backdrop).into(backdropimage);
        Picasso.with(context).load(image).into(poster);
        title_tv.setText(title);
        releasedate_tv.setText(releasedate);
        overview_tv.setText(overview);
        vote_tv.setText(vote);
        button.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                EntityData entityData = new EntityData();
                entityData.setMovieId(Integer.parseInt(id));
                entityData.setMovieDesc(overview);
                entityData.setMovieName(title);
                entityData.setMovieRelease(releasedate);
                entityData.setMoviePoster(image);
                entityData.setBackdrop(backdrop);
                entityData.setMovieVoteAvg(vote);
                viewModel.insert(entityData);
                likeButton.setLiked(true);
                MaterialToast.makeText(FavouriteDetails.this, "Added To Favourites", R.mipmap.added, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                EntityData entityData = new EntityData();
                entityData.setMovieId(Integer.parseInt(id));
                entityData.setMovieDesc(overview);
                entityData.setMovieName(title);
                entityData.setMovieRelease(releasedate);
                entityData.setMoviePoster(image);
                entityData.setMovieVoteAvg(vote);
                viewModel.delete(entityData);
                likeButton.setLiked(false);
                MaterialToast.makeText(FavouriteDetails.this, "Removed From Favourites", R.mipmap.removed, Toast.LENGTH_SHORT).show();


            }
        });

    }

    private void checkfavourite() {
        viewModel.getListLiveData().observe(this, new Observer<List<EntityData>>() {
            @Override
            public void onChanged(@Nullable List<EntityData> entityData) {

                for (int i = 0; i < entityData.size(); i++) {
                    String mid = Integer.toString(entityData.get(i).MovieId);

                    Log.d("td", id + " " + mid);

                    if (mid.equalsIgnoreCase(id)) {
                        button.setLiked(true);
                    }

                }

            }
        });

    }

}

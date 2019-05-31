package com.example.acer.Moviee2.UI;

import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.acer.Moviee2.ModelClass.EntityData;
import com.example.acer.Moviee2.ModelClass.Moviedata;
import com.example.acer.Moviee2.R;
import com.example.acer.Moviee2.Adapters.ReviewAdapter;
import com.example.acer.Moviee2.ModelClass.ReviewModel;
import com.example.acer.Moviee2.Adapters.TrailerAdapter;
import com.example.acer.Moviee2.ModelClass.TrailerModel;
import com.example.acer.Moviee2.ViewModel;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.marcoscg.materialtoast.MaterialToast;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {
    private List<TrailerModel> trailerModelList;
    private List<ReviewModel> reviewModelList;
    private List<EntityData> entityDataList;
    private String id;
    private RecyclerView trailer_recyclerview;
    private RecyclerView review_recyclerview;
     ViewModel viewModel;
    private LikeButton button;
    private RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ImageView backdropimage = findViewById(R.id.backdrop);
        List<Moviedata> moviedataList = new ArrayList<>();
        final Context context = this;
        ImageView poster = findViewById(R.id.poster);
        TextView title_tv = findViewById(R.id.title);
        TextView releasedate_tv = findViewById(R.id.releasedate);
        TextView overview_tv = findViewById(R.id.overview);
        TextView vote_tv = findViewById(R.id.vote);
        requestQueue = Volley.newRequestQueue(this);
        trailerModelList = new ArrayList<>();
        trailer_recyclerview = findViewById(R.id.trailerrecyclerview);
        review_recyclerview = findViewById(R.id.review_recyclerview);
        reviewModelList = new ArrayList<>();

        entityDataList = new ArrayList<>();
        viewModel = ViewModelProviders.of(DetailActivity.this).get(ViewModel.class);

        button = findViewById(R.id.favbutton);


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

        gettrailer();
        getreview();
        networkConnected();
        checkfavourite();
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
                MaterialToast.makeText(DetailActivity.this, "Added To Favourites", R.mipmap.added, Toast.LENGTH_SHORT).show();


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
                MaterialToast.makeText(DetailActivity.this, "Removed From Favourites", R.mipmap.removed, Toast.LENGTH_SHORT).show();


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


    private boolean networkConnected() {
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();

        } catch (Exception ignored) {
        }
        return connected;
    }


    private void gettrailer() {
        String id = getIntent().getStringExtra("id");
        String trailerurl = "https://api.themoviedb.org/3/movie/" + id + "/videos?api_key=d0bdeeeec25f53b2fd49cdf915dcd616";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, trailerurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    String id;
                    String key;
                    String name;
                    String type;
                    JSONObject root = new JSONObject(response);
                    JSONArray jsonArray = root.getJSONArray("results");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        id = obj.getString("id");
                        key = obj.getString("key");
                        name = obj.getString("name");
                        type = obj.getString("type");
                        TrailerModel modelClass = new TrailerModel(id, key, name, type);
                        trailerModelList.add(modelClass);
                    }
                    if (trailerModelList.size() == 0)
                        MaterialToast.makeText(DetailActivity.this, "no trailers available", R.mipmap.notavailable, Toast.LENGTH_SHORT).show();
                    TrailerAdapter myAdapter2 = new TrailerAdapter(DetailActivity.this, trailerModelList);
                    trailer_recyclerview.setLayoutManager(new LinearLayoutManager(DetailActivity.this));
                    trailer_recyclerview.setAdapter(myAdapter2);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        requestQueue.add(stringRequest);
    }

    private void getreview() {
        String id = getIntent().getStringExtra("id");
        String reviewurl = "https://api.themoviedb.org/3/movie/" + id + "/reviews?api_key=d0bdeeeec25f53b2fd49cdf915dcd616";
        StringRequest stringRequest1 = new StringRequest(Request.Method.GET, reviewurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (networkConnected()) {
                    try {
                        String author;
                        String content = null;
                        JSONObject root = new JSONObject(response);
                        JSONArray array = root.getJSONArray("results");

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            author = obj.getString("author");
                            content = obj.getString("content");
                            ReviewModel pojo = new ReviewModel(author, content);
                            reviewModelList.add(pojo);
                        }
                        if (reviewModelList.size() == 0)
                            MaterialToast.makeText(DetailActivity.this, "no reviews available", R.mipmap.notavailable, Toast.LENGTH_SHORT).show();
                        ReviewAdapter adapter_review = new ReviewAdapter(DetailActivity.this, reviewModelList);
                        review_recyclerview.setLayoutManager(new LinearLayoutManager(DetailActivity.this));
                        review_recyclerview.setAdapter(adapter_review);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    AlertDialog.Builder builder;
                    builder = new AlertDialog.Builder(DetailActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                    builder.setTitle(" Network Connection").setMessage("Please Check Internet Connection ")
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    builder.show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        requestQueue.add(stringRequest1);

    }


}


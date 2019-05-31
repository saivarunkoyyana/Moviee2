package com.example.acer.Moviee2.UI;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.acer.Moviee2.ModelClass.EntityData;
import com.example.acer.Moviee2.Adapters.Fav_Adapter;
import com.example.acer.Moviee2.ModelClass.Moviedata;
import com.example.acer.Moviee2.Adapters.MyAdapter;
import com.example.acer.Moviee2.R;
import com.example.acer.Moviee2.ViewModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RequestQueue requestQueue;
    private ArrayList<Moviedata> moviedatalist;
    private final String imagelink = "https://image.tmdb.org/t/p/w500";
    private final String backdroplink = "https://image.tmdb.org/t/p/w1280";
    private final String movietype = "popular";
    private final String movietypes = "top_rated";
    private SharedPreferences spf;

    private static final String n_Pref = "preference";
    private static final String st = "st";
    private static final String v_Popular = "popular";
    private static final String Top_rated = "top_rated";
    private static final String Favourites = "favourites";
    List<EntityData> entityDataList;
    ViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerview);
        requestQueue = Volley.newRequestQueue(this);
        moviedatalist = new ArrayList<>();
        spf = getSharedPreferences(n_Pref, MODE_PRIVATE);
        String key = spf.getString(st, null);
        entityDataList = new ArrayList<>();


        if (getApplication().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
        } else if (getApplication().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 4));
        }


        if (key == null) {
            loadmovies(movietype);
        } else {

            if (key.equalsIgnoreCase(v_Popular)) {
                loadmovies(movietype);
            } else if (key.equalsIgnoreCase(Top_rated)) {
                loadmovies(movietypes);
            } else {
                getfavourites();
            }
        }


        networkConnected();

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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 4));
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.popular_movies:
                SharedPreferences.Editor ed = spf.edit();
                ed.putString(st, v_Popular);
                ed.apply();

                loadmovies(movietype);
                break;
            case R.id.top_rated:
                ed = spf.edit();
                ed.putString(st, Top_rated);
                ed.apply();

                loadmovies(movietypes);
                break;
            case R.id.favMovies:
                ed = spf.edit();
                ed.putString(st, Favourites);
                ed.apply();
                getfavourites();

                break;


        }
        return super.onOptionsItemSelected(item);
    }

    private void getfavourites() {
        viewModel = ViewModelProviders.of(this).get(ViewModel.class);

        viewModel.getListLiveData().observe(this, new Observer<List<EntityData>>() {
            @Override
            public void onChanged(@Nullable List<EntityData> entityData) {
                if (entityData.isEmpty()) {
                    AlertDialog.Builder builder;
                    builder = new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                    builder.setTitle(" No Favourite Movies").setMessage("Go Check Out Movies")
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    loadmovies(movietype);
                                }
                            });
                    builder.show();
                }

                Fav_Adapter fav_adapter = new Fav_Adapter(MainActivity.this, entityData);

                recyclerView.setAdapter(fav_adapter);
                if (getApplication().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
                } else {
                    recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 4));
                }
                recyclerView.setHasFixedSize(true);

            }
        });


    }

    private void loadmovies(String movietype) {


        final String url1 = "https://api.themoviedb.org/3/movie/" + movietype + "?api_key=d0bdeeeec25f53b2fd49cdf915dcd616";

        StringRequest stringRequest1 = new StringRequest(Request.Method.GET, url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (networkConnected()) {
                    try {

                        String title;
                        String id;
                        String vote;
                        String overview;
                        String releasedate;
                        String image;
                        String backdrop;


                        moviedatalist = new ArrayList<>();

                        JSONObject root = new JSONObject(response);

                        JSONArray results = root.getJSONArray("results");
                        for (int i = 0; i < results.length(); i++) {
                            JSONObject obj = results.getJSONObject(i);

                            image = imagelink + obj.optString("poster_path");
                            title = obj.optString("title");
                            id = obj.getString("id");
                            vote = obj.getString("vote_average");
                            overview = obj.getString("overview");
                            releasedate = obj.getString("release_date");
                            backdrop = backdroplink + obj.optString("backdrop_path");


                            Moviedata movie = new Moviedata(backdrop, image, title, id, vote, overview, releasedate);
                            moviedatalist.add(movie);


                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    MyAdapter adapter = new MyAdapter(MainActivity.this, moviedatalist);


                    recyclerView.setAdapter(adapter);

                    recyclerView.setHasFixedSize(true);
                } else {
                    AlertDialog.Builder builder;
                    builder = new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                    builder.setTitle(" Network Connection").setMessage("Please Check Internet Connection")
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

package com.example.acer.Moviee2;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;


import com.example.acer.Moviee2.Database.MovieDAO;
import com.example.acer.Moviee2.Database.Roomdatabase;
import com.example.acer.Moviee2.ModelClass.EntityData;

import java.util.List;

class Repository {
    private final MovieDAO movieDAO;
    private final LiveData<List<EntityData>> listLiveData;

    public Repository(Application application) {
        Roomdatabase roomDatabase = (Roomdatabase) Roomdatabase.getDatabase(application);
        movieDAO = roomDatabase.movieDAO();
        listLiveData = movieDAO.getalldata();
    }

    public LiveData<List<EntityData>> getrepodata() {
        return listLiveData;
    }

    public void insert(EntityData entityData) {
        new insertAsyncTask(movieDAO).execute(entityData);
    }

    public void delete(EntityData entityData) {
        new deleteasyntask(movieDAO).execute(entityData);
    }

    private class insertAsyncTask extends AsyncTask<EntityData, Void, Void> {
        final MovieDAO movieDAO;

        insertAsyncTask(MovieDAO movieDAO) {
            this.movieDAO = movieDAO;
        }

        @Override
        protected Void doInBackground(EntityData... entityData) {
            movieDAO.insertInto(entityData[0]);
            return null;
        }
    }

    private class deleteasyntask extends AsyncTask<EntityData, Void, Void> {
        final MovieDAO movieDAO;

        deleteasyntask(MovieDAO movieDAO) {
            this.movieDAO = movieDAO;
        }

        @Override
        protected Void doInBackground(EntityData... entityData) {
            movieDAO.deleteFrom(entityData[0]);
            return null;
        }
    }
}

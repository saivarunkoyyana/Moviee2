package com.example.acer.Moviee2;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.acer.Moviee2.ModelClass.EntityData;

import java.util.List;

 public class ViewModel extends AndroidViewModel {

    private final Repository repository;
    private final LiveData<List<EntityData>> listLiveData;

    public ViewModel(@NonNull Application application) {
        super(application);

        repository = new Repository(application);
        listLiveData = repository.getrepodata();
    }


    public LiveData<List<EntityData>> getListLiveData() {
        return listLiveData;
    }

    public void insert(EntityData entityData) {
        repository.insert(entityData);
    }

    public void delete(EntityData entityData) {
        repository.delete(entityData);
    }
}



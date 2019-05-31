package com.example.acer.Moviee2.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.acer.Moviee2.ModelClass.EntityData;

import java.util.List;

@Dao
public interface MovieDAO {
    @Query("SELECT * FROM favoritesData")
    LiveData<List<EntityData>> getalldata();

    @Insert
    void insertInto(EntityData entityData);

    @Delete
    void deleteFrom(EntityData entityData);


}

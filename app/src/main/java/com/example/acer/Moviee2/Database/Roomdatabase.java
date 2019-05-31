package com.example.acer.Moviee2.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.acer.Moviee2.ModelClass.EntityData;

@Database(entities = {EntityData.class}, version = 1, exportSchema = false)
public abstract class Roomdatabase extends RoomDatabase {
    public abstract MovieDAO movieDAO();

    private static volatile RoomDatabase INSTANCE;

    public static RoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            Roomdatabase.class, "word_database").fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}



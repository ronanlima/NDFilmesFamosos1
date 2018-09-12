package com.udacity.ronanlima.ndfilmesfamosos1.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.util.Log;

import com.udacity.ronanlima.ndfilmesfamosos1.bean.Movie;

/**
 * Created by rlima on 05/08/18.
 */

@Database(entities = {Movie.class}, version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class AppDataBase extends RoomDatabase {
    public static final String TAG = AppDataBase.class.getCanonicalName().toUpperCase();

    public static final Object LOCK = new Object();
    public static final String DATABASE_NAME = "favoriteMovies";
    public static AppDataBase sInstance;

    public static AppDataBase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(TAG, "Create new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(), AppDataBase.class, AppDataBase.DATABASE_NAME).build();
            }
        }
        return sInstance;
    }

    public abstract MovieDAO movieDAO();
}

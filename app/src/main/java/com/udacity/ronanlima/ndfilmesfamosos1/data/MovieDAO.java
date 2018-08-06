package com.udacity.ronanlima.ndfilmesfamosos1.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by rlima on 05/08/18.
 */

@Dao
public interface MovieDAO {

    @Query("SELECT * FROM movie")
    LiveData<List<MovieEntry>> getAllMovies();

    @Insert
    void insert(MovieEntry movie);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(MovieEntry movie);

    @Delete
    void delete(MovieEntry movie);

    @Query("SELECT * FROM movie WHERE id = :id")
    LiveData<MovieEntry> findMovieById(int id);
}

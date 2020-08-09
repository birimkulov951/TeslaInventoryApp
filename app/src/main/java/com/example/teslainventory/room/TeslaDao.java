package com.example.teslainventory.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TeslaDao {

    @Insert
    void insert(Tesla tesla);

    @Update
    void update(Tesla tesla);

    @Delete
    void delete(Tesla tesla);

    @Query("DELETE FROM tesla_table")
    void deleteAllTeslaCars();

    @Query("SELECT * FROM tesla_table ORDER BY mPriority DESC")
    LiveData<List<Tesla>> getAllTeslaCars();

}

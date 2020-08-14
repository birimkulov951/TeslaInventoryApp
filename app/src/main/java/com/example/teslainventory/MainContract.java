package com.example.teslainventory;

import androidx.lifecycle.LiveData;

import com.example.teslainventory.room.Tesla;

import java.util.List;

public interface MainContract {

    interface View {

    }

    interface Presenter {
        // business logic happens here

        LiveData<List<Tesla>> getAllTeslaCars();

        void insert(Tesla tesla);

        void update(Tesla tesla);

        void delete(Tesla tesla);

        void deleteAllTesla();

        int getAllTeslaCarsSize();
    }
}

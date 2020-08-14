package com.example.teslainventory.mvp;

import androidx.lifecycle.LiveData;

import com.example.teslainventory.room.Tesla;

import java.util.List;

public interface MainContract {

    interface View {

        void setOnItemClickListener();

        void swipeToDelete();

        void showEmptyStore();

        void hideEmptyStore();

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

package com.example.teslainventory.mvp;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.teslainventory.Repository;
import com.example.teslainventory.room.Tesla;

import java.util.List;

public class MainPresenter implements MainContract.Presenter{

    private static final String TAG = "MainPresenter";

    private MainContract.View view;

    private Repository repository;
    private LiveData<List<Tesla>> allTeslaCars;

    public MainPresenter(MainContract.View view, Application application) {
        this.view = view;
        repository = new Repository(application);
        allTeslaCars = repository.getAllTesla();
    }

    public void onDestroy() {
        view = null;
    }

    @Override
    public void insert(Tesla tesla) {
        repository.insert(tesla);
    }

    @Override
    public void update(Tesla tesla) {
        repository.update(tesla);
    }

    @Override
    public void delete(Tesla tesla) {
        repository.delete(tesla);
    }

    @Override
    public void deleteAllTesla() {
        repository.deleteAllTesla();
    }

    @Override
    public LiveData<List<Tesla>> getAllTeslaCars() {
        return repository.getAllTesla();
    }

    @Override
    public int getAllTeslaCarsSize() {
        return repository.getAllTesla().getValue().size();
    }

}

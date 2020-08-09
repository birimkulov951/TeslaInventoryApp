package com.example.teslainventory;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.teslainventory.room.Tesla;

import java.util.List;

public class TeslaViewModel extends AndroidViewModel {

    private Repository repository;
    private LiveData<List<Tesla>> allTeslaCars;

    public TeslaViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        allTeslaCars = repository.getAllTesla();
    }

    public void insert(Tesla tesla) {
        repository.insert(tesla);
    }

    public void update(Tesla tesla) {
        repository.update(tesla);
    }
    public void delete(Tesla tesla) {
        repository.delete(tesla);
    }
    public void deleteAllTesla() {
        repository.deleteAllTesla();
    }
    public LiveData<List<Tesla>> getAllTeslaCars() {
        return allTeslaCars;
    }
    public int getAllTeslaCarsSize() {
        return allTeslaCars.getValue().size();
    }

}

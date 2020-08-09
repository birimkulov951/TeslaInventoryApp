package com.example.teslainventory;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.teslainventory.room.Tesla;
import com.example.teslainventory.room.TeslaDao;
import com.example.teslainventory.room.TeslaDatabase;

import java.util.List;

public class Repository {

    private TeslaDao teslaDao;
    private LiveData<List<Tesla>> allTeslaCars;

    public Repository(Application application) {
        TeslaDatabase database = TeslaDatabase.getInstance(application);
        teslaDao = database.teslaDao();
        allTeslaCars = teslaDao.getAllTeslaCars();
    }

    public void insert(Tesla tesla) {
        new InsertTeslaAsyncTask(teslaDao).execute(tesla);
    }
    public void update(Tesla tesla) {
        new UpdateTeslaAsyncTask(teslaDao).execute(tesla);
    }
    public void delete(Tesla tesla) {
        new DeleteTeslaAsyncTask(teslaDao).execute(tesla);
    }
    public void deleteAllTesla() {
        new DeleteAllTeslaAsyncTask(teslaDao).execute();
    }

    public LiveData<List<Tesla>> getAllTesla() {
        return allTeslaCars;
    }


   private static class InsertTeslaAsyncTask extends AsyncTask<Tesla, Void, Void> {

        private TeslaDao teslaDao;

        private InsertTeslaAsyncTask(TeslaDao dao) {
            this.teslaDao = dao;
        }

       @Override
       protected Void doInBackground(Tesla... inventories) {
           teslaDao.insert(inventories[0]);
           return null;
       }

   }

    private static class UpdateTeslaAsyncTask extends AsyncTask<Tesla, Void, Void> {

        private TeslaDao teslaDao;

        private UpdateTeslaAsyncTask(TeslaDao dao) {
            this.teslaDao = dao;
        }

        @Override
        protected Void doInBackground(Tesla... inventories) {
            teslaDao.update(inventories[0]);
            return null;
        }

    }

    private static class DeleteTeslaAsyncTask extends AsyncTask<Tesla, Void, Void> {

        private TeslaDao teslaDao;

        private DeleteTeslaAsyncTask(TeslaDao dao) {
            this.teslaDao = dao;
        }

        @Override
        protected Void doInBackground(Tesla... inventories) {
            teslaDao.delete(inventories[0]);
            return null;
        }

    }

    private static class DeleteAllTeslaAsyncTask extends AsyncTask<Tesla, Void, Void> {

        private TeslaDao teslaDao;

        private DeleteAllTeslaAsyncTask(TeslaDao dao) {
            this.teslaDao = dao;
        }

        @Override
        protected Void doInBackground(Tesla... inventories) {
            teslaDao.deleteAllTeslaCars();
            return null;
        }

    }
}

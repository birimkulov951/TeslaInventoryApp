package com.example.teslainventory.room;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Tesla.class}, version = 1)
public abstract class TeslaDatabase extends RoomDatabase {

    private static TeslaDatabase instance;

    public abstract TeslaDao teslaDao();

    public static synchronized TeslaDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    TeslaDatabase.class,"tesla_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private TeslaDao teslaDao;

        private PopulateDbAsyncTask(TeslaDatabase db) {
            teslaDao = db.teslaDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            return null;
        }

    }
}

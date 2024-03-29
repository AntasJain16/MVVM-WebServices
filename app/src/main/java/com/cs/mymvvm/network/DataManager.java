package com.cs.mymvvm.network;

import com.cs.mymvvm.App;
import com.cs.mymvvm.data.db.database.LogDatabase;
import com.cs.mymvvm.network.services.MovieService;
import com.preference.PowerPreference;
import com.preference.Preference;

public class DataManager {

    private static DataManager sInstance;

    private DataManager() {
        // This class is not publicly instantiable
    }

    public static synchronized DataManager getInstance() {
        if (sInstance == null) {
            sInstance = new DataManager();
        }
        return sInstance;
    }

    public Preference getPrefs() {
        return PowerPreference.defult();
    }

    public LogDatabase getLogDatabse() {
        return LogDatabase.getInstance(App.getInstance());
    }

    public MovieService getMovieService() {
        return MovieService.getInstance();
    }

}

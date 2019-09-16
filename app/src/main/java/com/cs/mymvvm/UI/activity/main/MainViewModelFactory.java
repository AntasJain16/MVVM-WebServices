package com.cs.mymvvm.UI.activity.main;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.cs.mymvvm.network.services.MovieService;


public class
MainViewModelFactory implements ViewModelProvider.Factory {

    private final MovieService movieService;

    public MainViewModelFactory(MovieService movieService) {
        this.movieService = movieService;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MainViewModel.class)) {
            return (T) new MainViewModel(movieService);
        }

        throw new IllegalArgumentException("Unknown ViewModel class");
    }

}

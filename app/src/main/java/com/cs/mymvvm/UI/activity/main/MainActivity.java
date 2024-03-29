package com.cs.mymvvm.UI.activity.main;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.cs.mymvvm.network.DataManager;
import com.cs.mymvvm.R;
import com.cs.mymvvm.UI.activity.details.DetailsActivity;
import com.cs.mymvvm.network.model.Movie;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity implements MovieAdapter.OnMovieAdapter {


    MovieAdapter movieAdapter;


    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.empty_view)
    TextView emptyView;

    MainViewModel viewModel;

    private MainViewModel createViewModel() {
        MainViewModelFactory factory = new MainViewModelFactory(DataManager.getInstance().getMovieService());
        return ViewModelProviders.of(this, factory).get(MainViewModel.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        movieAdapter = new MovieAdapter(this);
        recyclerView.setAdapter(movieAdapter);

        viewModel = createViewModel();

        viewModel.getLoadingStatus().observe(this, new LoadingObserver());
        viewModel.getMovies().observe(this, new MovieObserver());
    }

    @OnClick(R.id.network)
    void onNetworkButtonClick() {
        viewModel.loadMoviesNetwork();
    }

    @OnClick(R.id.local)
    void onLocalButtonClick() {
        viewModel.loadMovieLocal();
    }

    @OnClick(R.id.empty)
    void onEmptyButtonClick() {
        viewModel.showEmptyList();
    }

    @OnClick(R.id.Volley)
    void onVolleyButtonClick(){
        viewModel.setVolley(this);

    }



    @Override
    public void onMovieClicked(Movie movie) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("movie", movie);
        startActivity(intent);
    }

    //Observer
    private class LoadingObserver implements Observer<Boolean> {

        @Override
        public void onChanged(@Nullable Boolean isLoading) {
            if (isLoading == null) return;

            if (isLoading) {
                progressBar.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.GONE);
            }
        }
    }

    private class MovieObserver implements Observer<List<Movie>> {

        @Override
        public void onChanged(@Nullable List<Movie> movies) {
            if (movies == null) return;
            movieAdapter.setItems(movies);

            if (movies.isEmpty()) {
                emptyView.setVisibility(View.VISIBLE);
            } else {
                emptyView.setVisibility(View.GONE);
            }
        }
    }
}

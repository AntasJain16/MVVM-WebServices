package com.cs.mymvvm.UI.activity.main;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.support.annotation.NonNull;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cs.mymvvm.Constants;
import com.cs.mymvvm.network.model.Movie;
import com.cs.mymvvm.network.model.MovieResponse;
import com.cs.mymvvm.network.services.MovieService;
import com.cs.mymvvm.network.services.VolleySingleton;
import com.shashank.sony.fancytoastlib.FancyToast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class MainViewModel extends ViewModel {
Context ctx;
    private MutableLiveData<List<Movie>> movieList;
    private MutableLiveData<Boolean> isLoading;
    private MovieService movieService;
    List<Movie> mmovie= new ArrayList<>();
    public MainViewModel(MovieService movieService) {
        this.movieService = movieService;
        movieList = new MutableLiveData<>();
        isLoading = new MutableLiveData<>();
    }
    MutableLiveData<List<Movie>> getMovies() {
        return movieList;
    }
    MutableLiveData<Boolean> getLoadingStatus() {
        return isLoading;
    }
    void loadMoviesNetwork() {
        setIsLoading(true);
        Call<MovieResponse> movieCall = movieService.getMovieApi().getAllMovie();
        movieCall.enqueue(new MovieCallback());
    }
    void loadMovieLocal() {
        setIsLoading(true);
        String name = "Breaking Bad";
        String image = "https://coderwall-assets-0.s3.amazonaws.com/" +
                "uploads/picture/file/622/breaking_bad_css3_svg_raw.png";
        String name2 = "Sherlock";
        String image2="https://cdn.images.express.co.uk" +
                "/img/dynamic/20/590x/" +
                "Sherlock-Holmes-spoliers-shock-sister-Benedict-Cumberbatch-Martin-Freeman-751707.jpg";
        List<Movie> movies = new ArrayList<>();
        movies.add(new Movie(name, image, name));
        movies.add(new Movie(name2, image2, name2));
        movies.add(new Movie(name, image, name));
        setMovies(movies);
        FancyToast.makeText(ctx, "Local Data Fetch Success ", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();

    }
    void showEmptyList() {
        setMovies(Collections.<Movie>emptyList());
    }
    public void setIsLoading(boolean loading) {
        isLoading.postValue(loading);
    }
    public void setMovies(List<Movie> movies) {
        setIsLoading(false);
        movieList.postValue(movies);
    }

    public  void setVolley(final Context context)
    {   ctx=context;
        setIsLoading(true);
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, Constants.URL + "movies/", null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                VolleySingleton.getInstance(context).getRequestQueue();
                try {
                    JSONArray jsonArray = response.getJSONArray("movies");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Movie movie=new Movie();
                        movie.setTitle(jsonArray.getJSONObject(i).getString("title"));
                            movie.setImage(jsonArray.getJSONObject(i).getString("image"));
                            movie.setDescription(jsonArray.getJSONObject(i).getString("description"));
                            mmovie.add(movie);
                FancyToast.makeText(context, "Success Through Volley ", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                        }
                }
                catch (JSONException e) {
                    e.printStackTrace();setIsLoading(false);
                    FancyToast.makeText(context,e.toString(),FancyToast.LENGTH_LONG,FancyToast.ERROR,false).show();
                    setVolley(context);
                } setIsLoading(false);
               setMovies(mmovie);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                FancyToast.makeText(context,error.toString(),FancyToast.LENGTH_LONG,FancyToast.ERROR,false).show();
                setIsLoading(false);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(objectRequest);
    }

    private class MovieCallback implements Callback<MovieResponse> {
        @Override
        public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
            MovieResponse movieResponse = response.body();
            if (movieResponse != null) {
                setMovies(movieResponse.getMovies());
            } else {
                setMovies(Collections.<Movie>emptyList());
            }
        }

        @Override
        public void onFailure(Call<MovieResponse> call, Throwable t) {
            /* setMovies(Collections.<Movie>emptyList());*/
            try {
                call.clone();
            } catch (Exception e) {
                setMovies(Collections.<Movie>emptyList());
            }
        }
    }
}
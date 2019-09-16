package com.cs.mymvvm.network.services;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cs.mymvvm.Constants;
import com.cs.mymvvm.UI.activity.main.MainActivity;
import com.cs.mymvvm.UI.activity.main.MainViewModel;
import com.cs.mymvvm.UI.activity.main.MovieAdapter;
import com.cs.mymvvm.network.model.Movie;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class VolleyService {
    private JsonArrayRequest ArrayRequest;
    private RequestQueue requestQueue;
    MainViewModel viewModel;

    private List<Movie> mmovie = new ArrayList<>();

    MovieAdapter movieAdapter;
    MainViewModel mainViewModel;

    public void jsoncall(final Context context) {

        viewModel.setIsLoading(true);
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, Constants.URL + "movies/", null, 
                                                                new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                VolleySingleton.getInstance(context).getRequestQueue();


                try {
                    JSONArray jsonArray = response.getJSONArray("movies");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Movie movie = new Movie();
                        movie.setTitle(jsonArray.getJSONObject(i).getString("title"));
                        movie.setImage(jsonArray.getJSONObject(i).getString("image"));
                        movie.setDescription(jsonArray.getJSONObject(i).getString("description"));

                        // emptyView.setVisibility(View.INVISIBLE);
                        mmovie.add(movie);
                        FancyToast.makeText(context,"Success",FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    viewModel.setIsLoading(false);
                    Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                }
                viewModel.setIsLoading(false);

                viewModel.setMovies(mmovie);
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                viewModel.setIsLoading(false);
            }
        });


        requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(objectRequest);
    }
}

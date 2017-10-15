package com.example.sharangirdhani.homework05;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.sharangirdhani.homework05.databinding.ActivityMainBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements GetMusicTrakAsyncTask.IData, MusicListAdapter.IMusicListAdapter {
    private ActivityMainBinding binding;
    final static String BASE_URL = "http://ws.audioscrobbler.com/2.0/";
    final static String MUSIC_TRACK_DATA_KEY = "MUSIC_DATA";
    public static String FAVORITES_PREF_KEY = "FAVS";
    public static String FIRST_FETCH = "FIRST_FETCH";
    public static String SIMILAR_FETCH = "SIMILAR_FETCH";
    public static String SEND_LIST = "LIST";
    public static String SEND_TRACK = "TRACK";
    MusicListAdapter musicListAdapter;
    LinearLayoutManager layoutManager;
    MusicTrack selectedTrack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        binding.buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnectedOnline()){
                    String trackSearch = binding.editTextMusicTrack.getText().toString();

                    if(trackSearch.trim().isEmpty()) {
                        Toast.makeText(MainActivity.this, "Music track is empty", Toast.LENGTH_SHORT).show();
                    }
                    else {

                        RequestParams req = new RequestParams("GET", BASE_URL);
                        req.addParam("format", "json");
                        req.addParam("method", "track.search");
                        req.addParam("track", binding.editTextMusicTrack.getText().toString());
                        req.addParam("api_key", "871902992dd4a9f8f44dac1f89bf6acf");
                        req.addParam("limit", "20");
                        new GetMusicTrakAsyncTask(MainActivity.this, MainActivity.FIRST_FETCH).execute(req.getEncodedUrl());
                    }
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Internet Not Connected",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateDataMain();
    }

    void setAdapterAndNotify(ArrayList<MusicTrack> musicTracks){
        musicListAdapter = new MusicListAdapter(musicTracks,MainActivity.this,MainActivity.this);
        binding.recyclerViewMainActivity.setAdapter(musicListAdapter);
        layoutManager = new LinearLayoutManager(this);
        binding.recyclerViewMainActivity.setLayoutManager(layoutManager);
        musicListAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:{
                Toast.makeText(MainActivity.this, "You're already at home",Toast.LENGTH_SHORT).show();
                return false;
            }
            case R.id.quit:
            {
                this.finishAffinity();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isConnectedOnline(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if(networkInfo!=null && networkInfo.isConnected()){
            return true;
        }
        return false;
    }

    @Override
    public void updateData(ArrayList<MusicTrack> musicTracks, boolean isSimilar) {
        Intent intent;
        if(isSimilar){
            intent = new Intent(this, DetailsActivity.class);
            intent.putExtra(SEND_LIST,musicTracks);
            intent.putExtra(SEND_TRACK,selectedTrack);
        }
        else{
            intent = new Intent(this, SearchResultActivity.class);
            intent.putExtra(MUSIC_TRACK_DATA_KEY, musicTracks);
        }
        startActivity(intent);
    }

    @Override
    public void trackDetails(MusicTrack musicTrack) {
        RequestParams req = new RequestParams("GET", BASE_URL);
        req.addParam("method", "track.getsimilar");
        req.addParam("artist", musicTrack.getArtist());
        req.addParam("track", musicTrack.getName());
        req.addParam("api_key", "871902992dd4a9f8f44dac1f89bf6acf");
        req.addParam("format", "json");
        req.addParam("limit", "10");
        selectedTrack = musicTrack;
        new GetMusicTrakAsyncTask(MainActivity.this, MainActivity.SIMILAR_FETCH).execute(req.getEncodedUrl());
    }

    public void updateDataMain() {
        SharedPreferences preferences = getSharedPreferences("PREFS", MODE_PRIVATE);
        String jsonString = preferences.getString(MainActivity.FAVORITES_PREF_KEY, null);
        if(jsonString!=null){
            Gson gson = new Gson();
            ArrayList<MusicTrack> favoritesList = gson.fromJson(jsonString, new TypeToken<ArrayList<MusicTrack>>() {
            }.getType());
            setAdapterAndNotify(favoritesList);
        }
    }
    @Override
    public void refreshList() {
        updateDataMain();
    }

    @Override
    public ArrayList<MusicTrack> fetchFavouritesData(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("PREFS", context.MODE_PRIVATE);
        String jsonString = preferences.getString(MainActivity.FAVORITES_PREF_KEY, null);
        Gson gson = new Gson();
        ArrayList<MusicTrack> favouriteList;
        if(jsonString!=null){
            favouriteList = gson.fromJson(jsonString, new TypeToken<ArrayList<MusicTrack>>() {
            }.getType());
            return favouriteList;
        }
        return null;
    }

    @Override
    public void addDataToFavorites(Context context, ArrayList<MusicTrack> favouriteList, MusicTrack musicTrack) {
        favouriteList.add(musicTrack);
        Toast.makeText(context, "Added to Favorites", Toast.LENGTH_SHORT).show();
        Gson gson = new Gson();
        String jsonString = gson.toJson(favouriteList);
        SharedPreferences preferences = context.getSharedPreferences("PREFS", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(MainActivity.FAVORITES_PREF_KEY, jsonString);
        editor.commit();
    }

    @Override
    public void removeDataFromFavorites(Context context, ArrayList<MusicTrack> favouriteList, MusicTrack musicTrack) {

        favouriteList.remove(musicTrack);
        Toast.makeText(context, "Removed from Favorites", Toast.LENGTH_SHORT).show();
        Gson gson = new Gson();
        String jsonString = gson.toJson(favouriteList);
        SharedPreferences.Editor editor = context.getSharedPreferences("PREFS",0).edit();
        editor.putString(MainActivity.FAVORITES_PREF_KEY,jsonString);
        editor.apply();
    }
}
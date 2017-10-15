package com.example.sharangirdhani.homework05;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import com.example.sharangirdhani.homework05.databinding.ActivitySearchResultBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class SearchResultActivity extends AppCompatActivity implements MusicListAdapter.IMusicListAdapter, GetMusicTrakAsyncTask.IData{

    final static String BASE_URL = "http://ws.audioscrobbler.com/2.0/";
    private ActivitySearchResultBinding bindingSearch;
    private ArrayList<MusicTrack> musicData;
    MusicTrack selectedTrack;
    RecyclerView recyclerView;
    MusicListAdapter musicListAdapter;
    LinearLayoutManager layoutManager;
    public static String SEND_LIST = "LIST";
    public static String SEND_TRACK = "TRACK";
    public static String SIMILAR_FETCH = "SIMILAR_FETCH";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindingSearch = DataBindingUtil.setContentView(this, R.layout.activity_search_result);

        if(getIntent().hasExtra(MainActivity.MUSIC_TRACK_DATA_KEY)) {

            musicData = (ArrayList<MusicTrack>) getIntent().getSerializableExtra(MainActivity.MUSIC_TRACK_DATA_KEY);
            musicListAdapter = new MusicListAdapter(musicData,SearchResultActivity.this,SearchResultActivity.this);
            recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
            recyclerView.setAdapter(musicListAdapter);
            layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            musicListAdapter.notifyDataSetChanged();
        }
        else {
            Toast.makeText(this, "Problem in getting search music data", Toast.LENGTH_SHORT).show();
        }
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
            case R.id.home:
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |  Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;
            case R.id.quit:
                Toast.makeText(this, "Quit Clicked", Toast.LENGTH_SHORT).show();
                this.finishAffinity();
                break;
        }
        return true;
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
        new GetMusicTrakAsyncTask(SearchResultActivity.this, SearchResultActivity.SIMILAR_FETCH).execute(req.getEncodedUrl());
    }

    @Override
    public void refreshList() {

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

    @Override
    public void updateData(ArrayList<MusicTrack> musicTracks, boolean isSimilar) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(SEND_LIST,musicTracks);
        intent.putExtra(SEND_TRACK,selectedTrack);
        startActivity(intent);
    }
}


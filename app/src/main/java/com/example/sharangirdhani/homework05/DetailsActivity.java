/*
Salman Mujtaba: 800969897
Sharan Giridhani
GetMusicTrakAsyncTask
 */
package com.example.sharangirdhani.homework05;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import android.content.SharedPreferences;
import com.example.sharangirdhani.homework05.databinding.ActivityDetailsBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;


public class DetailsActivity extends AppCompatActivity implements MusicListAdapter.IMusicListAdapter, GetMusicTrakAsyncTask.IData {

    public static final String SIMILAR_FETCH = "SIMILAR_FETCH";
    RecyclerView recyclerView;
    MusicTrack selectedTrack;
    final static String BASE_URL = "http://ws.audioscrobbler.com/2.0/";
    MusicListAdapter musicListAdapter;
    LinearLayoutManager layoutManager;
    private ActivityDetailsBinding binding;
    private ArrayList<MusicTrack> musicData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_details);
        if(getIntent().hasExtra(SearchResultActivity.SEND_LIST) && getIntent().hasExtra(SearchResultActivity.SEND_TRACK)) {
            selectedTrack = (MusicTrack) getIntent().getExtras().getSerializable(SearchResultActivity.SEND_TRACK);
            setView();
            musicData = (ArrayList<MusicTrack>) getIntent().getSerializableExtra(SearchResultActivity.SEND_LIST);
            setAdapterAndNotify(musicData);
        }
        else{
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

    public void trackDetails(MusicTrack musicTrack) {
        RequestParams req = new RequestParams("GET", BASE_URL);
        req.addParam("method", "track.getsimilar");
        req.addParam("artist", musicTrack.getArtist());
        req.addParam("track", musicTrack.getName());
        req.addParam("api_key", "871902992dd4a9f8f44dac1f89bf6acf");
        req.addParam("format", "json");
        req.addParam("limit", "10");

        selectedTrack = musicTrack;
        setView();

        new GetMusicTrakAsyncTask(DetailsActivity.this, DetailsActivity.SIMILAR_FETCH).execute(req.getEncodedUrl());
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

    void setView()
    {
        binding.textViewName.setText(selectedTrack.getName());
        binding.textViewArtist.setText(selectedTrack.getArtist());
        binding.textViewURL.setText((selectedTrack.getUrl()));
        Picasso.with(this).load(selectedTrack.getImgLarge()).into(binding.imageViewPicture);
    }
    @Override
    public void updateData(ArrayList<MusicTrack> musicTracks, boolean isSimilar) {
        setAdapterAndNotify(musicTracks);
    }

    void setAdapterAndNotify(ArrayList<MusicTrack> musicTracks){
        musicListAdapter = new MusicListAdapter(musicTracks,DetailsActivity.this,DetailsActivity.this);
        binding.recyclerViewSingleTrack.setAdapter(musicListAdapter);
        layoutManager = new LinearLayoutManager(this);
        binding.recyclerViewSingleTrack.setLayoutManager(layoutManager);
        musicListAdapter.notifyDataSetChanged();
    }
}

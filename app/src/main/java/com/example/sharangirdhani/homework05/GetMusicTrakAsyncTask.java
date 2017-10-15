package com.example.sharangirdhani.homework05;

import android.os.AsyncTask;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class GetMusicTrakAsyncTask extends AsyncTask<String, Void, ArrayList<MusicTrack>> {

    IData activity;
    String action;

    public GetMusicTrakAsyncTask(IData activity, String action) {
        this.activity = activity;
        this.action = action;
    }

    @Override
    protected ArrayList<MusicTrack> doInBackground(String... params) {
        BufferedReader reader = null;
        try {
            URL url = new URL(params[0]);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();
            int statusCode = con.getResponseCode();
            if(statusCode == HttpURLConnection.HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = reader.readLine();
                while(line != null) {
                    sb.append(line);
                    line = reader.readLine();
                }
                if(action.equals(MainActivity.FIRST_FETCH)){
                    return MusicUtil.MusicJSONParser.parseMusic(sb.toString(), MainActivity.FIRST_FETCH);
                }
                else if(action.equals("SIMILAR_FETCH")){
                    return MusicUtil.MusicJSONParser.parseMusic(sb.toString(), "SIMILAR_FETCH");
                }
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if(reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<MusicTrack> musicTracks) {
        super.onPostExecute(musicTracks);
        if(action.equals(MainActivity.SIMILAR_FETCH)){
            activity.updateData(musicTracks,true);
            return;
        }
        activity.updateData(musicTracks,false);
    }

    static public interface IData{
        void updateData(ArrayList<MusicTrack> musicTracks, boolean isSimilar);
    }
}

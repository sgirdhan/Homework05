/*
Salman Mujtaba: 800969897
Sharan Girdhani: 800960333
MusicUtil
 */
// In Class 05
// ContactListActivity.java
// Sharan Girdhani     - 800960333
// Salman Mujtaba   - 800969897
//

package com.example.sharangirdhani.homework05;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by sharangirdhani on 9/25/17.
 */

public class MusicUtil {
    static public class MusicJSONParser {
        static ArrayList<MusicTrack> parseMusic(String in, String action) throws JSONException {
            ArrayList<MusicTrack> musicList;

            if(action.equals(MainActivity.FIRST_FETCH)){
                JSONObject musicObject;
                musicList = new ArrayList<>();
                JSONObject text;
                MusicTrack music;
                JSONArray image;

                JSONObject root = new JSONObject(in);
                JSONArray musicJSONArray = root.getJSONObject("results").getJSONObject("trackmatches").getJSONArray("track");

                for (int i = 0; i < musicJSONArray.length(); i++) {
                    musicObject = musicJSONArray.getJSONObject(i);
                    music = new MusicTrack();
                    music.setName(musicObject.getString("name"));
                    music.setUrl(musicObject.getString("url"));
                    music.setArtist(musicObject.getString("artist"));
                    image = musicObject.getJSONArray("image");
                    for (int j = 0; j < image.length(); j++) {
                        text = image.getJSONObject(j);
                        if(text.getString("size").equals("small")){
                            music.setImgSmall(text.getString("#text"));
                        }
                        if(text.getString("size").equals("large")){
                            music.setImgLarge(text.getString("#text"));
                        }
                    }
                    musicList.add(music);
                }

                return musicList;
            }
            else if(action.equals("SIMILAR_FETCH"))
            {
                JSONObject musicObject;
                JSONObject text;
                MusicTrack music;
                JSONArray image;

                musicList = new ArrayList<>();

                JSONObject root = new JSONObject(in);
                JSONArray musicJSONArray = root.getJSONObject("similartracks").getJSONArray("track");

                for (int i = 0; i < musicJSONArray.length(); i++) {
                    musicObject = musicJSONArray.getJSONObject(i);
                    music = new MusicTrack();
                    music.setName(musicObject.getString("name"));
                    music.setUrl(musicObject.getString("url"));

                    music.setArtist(musicObject.getJSONObject("artist").getString("name"));

                    image = musicObject.getJSONArray("image");
                    for (int j = 0; j < image.length(); j++) {
                        text = image.getJSONObject(j);
                        if(text.getString("size").equals("small")){
                            music.setImgSmall(text.getString("#text"));
                        }
                        if(text.getString("size").equals("large")){
                            music.setImgLarge(text.getString("#text"));
                        }
                    }
                    musicList.add(music);
                }
                return musicList;
            }

            return null;
        }
    }
}

package com.example.android_project;

import java.util.Locale;
import java.util.Map.Entry;


import java.util.HashMap;

public class SearchGame{
    private HashMap<Integer,String> game_map;

    public SearchGame(HashMap<Integer,String> _game_map){
        this.game_map = _game_map;
    }

    public HashMap<Integer,String> search(String game_name){
        HashMap<Integer,String> games_found = new HashMap<>();


        for (Entry<Integer, String> entry : game_map.entrySet()) {
            int appid = entry.getKey();
            String name = entry.getValue();

            if (name.trim().toLowerCase(Locale.ROOT).contains(game_name.trim().toLowerCase(Locale.ROOT))) games_found.put(appid,name);
        }
        return games_found;
    }
}

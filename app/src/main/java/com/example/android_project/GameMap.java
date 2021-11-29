package com.example.android_project;

import java.util.HashMap;
import java.util.Map;

public class GameMap { //Singleton
    private Map<Integer,String> game_map;
    private static GameMap instance = null;

    private GameMap() {
        game_map = new HashMap<>();
    }

    public static GameMap getInstance() {
        if (instance == null)
            instance = new GameMap();
        return instance;
    }

    public String put(int id, String name) {
        return game_map.put(id,name);
    }

    public String remove(int id) {
        return game_map.remove(id);
    }

    public String get(int id) {
        return game_map.get(id);
    }

    public Map<Integer, String> getMap() {
        return game_map;
    }

    public void copyMap(Map<Integer,String> map) {
        this.game_map = map;
    }
}

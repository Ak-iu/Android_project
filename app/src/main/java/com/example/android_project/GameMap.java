package com.example.android_project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Système de gestion de la map (id->nom) de tous les jeux
 * Permet de notifier à des listeners des changements dans la map et des erreurs
 */

public class GameMap { //Singleton
    private static GameMap instance = null;
    private final List<GameMapListener> listeners;
    private Map<Integer, String> game_map;
    private boolean hasMap = false;
    private boolean waiting = false;

    private GameMap() {
        game_map = new HashMap<>();
        listeners = new ArrayList<>();
    }

    public static GameMap getInstance() {
        if (instance == null)
            instance = new GameMap();
        return instance;
    }

    public String put(int id, String name) {
        return game_map.put(id, name);
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

    public void copyMap(Map<Integer, String> map) {
        this.game_map = map;
    }

    public void addListener(GameMapListener listener) {
        listeners.add(listener);
    }

    public void removeListener(GameMapListener listener) {
        listeners.remove(listener);
    }

    public void notifyListeners() {
        for (GameMapListener listener : listeners)
            listener.notifyUpdate();
    }

    public void notifyErrorListeners() {
        for (GameMapListener listener : listeners)
            listener.notifyError();
    }

    public void setHasMap(boolean hasMap) {
        this.hasMap = hasMap;
    }

    public boolean hasMap() {
        return hasMap;
    }

    public boolean isWaiting() {
        return waiting;
    }

    public void setWaiting(boolean waiting) {
        this.waiting = waiting;
    }

    /**
     * Interface à implémenter pour les listeners
     */
    public interface GameMapListener {
        void notifyUpdate();

        void notifyError();
    }
}

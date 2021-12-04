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

    /**
     * Crée le système de gestion de la map globale
     */
    private GameMap() {
        game_map = new HashMap<>();
        listeners = new ArrayList<>();
    }

    /**
     * Récupère le singleton de la gestion de la map
     * @return l'instance de gestion
     */
    public static GameMap getInstance() {
        if (instance == null)
            instance = new GameMap();
        return instance;
    }

    /**
     * Rajoute un jeu dans la map
     * @param id id du jeu
     * @param name nom du jeu
     * @return le précédent nom associé à l'id si déjà présent, null sinon
     */
    public String put(int id, String name) {
        return game_map.put(id, name);
    }

    /**
     * Supprime un jeu de la map
     * @param id je uà supprimer
     * @return le nom du jeu si présent, null sinon
     */
    public String remove(int id) {
        return game_map.remove(id);
    }

    /**
     * Récupère le nom associé à l'id
     * @param id id jdu jeu
     * @return le nom si présent, sinon null
     */
    public String get(int id) {
        return game_map.get(id);
    }

    /**
     * @return la map des jeux
     */
    public Map<Integer, String> getMap() {
        return game_map;
    }

    /**
     * Remplace la map actuelle
     * @param map nouvelle map
     */
    public void copyMap(Map<Integer, String> map) {
        this.game_map = map;
    }

    /**
     * Rajoute un observateur dans la liste
     * @param listener l'observateur à ajouter
     */
    public void addListener(GameMapListener listener) {
        listeners.add(listener);
    }

    /**
     * Supprime un observateur de la liste
     * @param listener l'oservateur à supprimer
     */
    public void removeListener(GameMapListener listener) {
        listeners.remove(listener);
    }

    /**
     * Notifie tous les observateurs
     */
    public void notifyListeners() {
        for (GameMapListener listener : listeners)
            listener.notifyUpdate();
    }

    /**
     * Notifie tous les oservateurs d'une erreur
     */
    public void notifyErrorListeners() {
        for (GameMapListener listener : listeners)
            listener.notifyError();
    }

    /**
     * @param hasMap si le singleton a une map à jour
     */
    public void setHasMap(boolean hasMap) {
        this.hasMap = hasMap;
    }

    /**
     * Retourne true si le singleton a une map à jour
     */
    public boolean hasMap() {
        return hasMap;
    }

    /**
     * @return true si le singleton est en attente de chargement d'une map
     */
    public boolean isWaiting() {
        return waiting;
    }

    /**
     * @param waiting met à jour si le singleton est en attente de chargement d'une map
     */
    public void setWaiting(boolean waiting) {
        this.waiting = waiting;
    }

    /**
     * Interface à implémenter pour les listeners
     */
    public interface GameMapListener {
        /**
         * Reçoit la notification de changement de la map
         */
        void notifyUpdate();

        /**
         * Reçoit la notification d'erreur
         */
        void notifyError();
    }
}

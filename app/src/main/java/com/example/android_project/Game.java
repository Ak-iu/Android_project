package com.example.android_project;

import java.util.Comparator;

public class Game implements Comparable<Game> {
    private final int appiid;
    private final String name;

    public Game(int _appid, String _name) {
        this.appiid = _appid;
        this.name = _name;
    }

    /**
     * Obtention de l'ID Steam de Game
     * @return ID de l'application Steam
     */
    public int getAppiid() {
        return appiid;
    }

    /**
     * Obtention du nom de Game
     * @return  Nom du jeu Steam
     */
    public String getName() {
        return name;
    }

    /**
     * Comparateur de Game via le Nom
     * @param game Jeu Steam
     * @return  -1 si inférieur
     *           0 si égal
     *           1 si supérieur
     */
    @Override
    public int compareTo(Game game) {
        return Integer.compare(name.length(), game.name.length());
    }
}

class SortByName implements Comparator<Game> {

    /**
     * Comparateur de 2 objets Game
     * @param g1 jeu 1
     * @param g2 jeu 2
     * @return longueur du nom de g1 moins celui de g2
     */
    @Override
    public int compare(Game g1, Game g2) {
        return g1.getName().length() - g2.getName().length();
    }
}

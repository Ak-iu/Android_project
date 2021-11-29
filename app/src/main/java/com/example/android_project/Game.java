package com.example.android_project;

import java.util.Comparator;

public class Game implements Comparable<Game> {
    private final int appiid;
    private final String name;

    public Game(int _appid, String _name) {
        this.appiid = _appid;
        this.name = _name;
    }

    public int getAppiid() {
        return appiid;
    }

    public String getName() {
        return name;
    }

    @Override
    public int compareTo(Game game) {
        return Integer.compare(name.length(), game.name.length());
    }
}

class SortByName implements Comparator<Game> {

    @Override
    public int compare(Game g1, Game g2) {
        return g1.getName().length() - g2.getName().length();
    }
}

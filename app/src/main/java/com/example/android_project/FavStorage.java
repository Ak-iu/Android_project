package com.example.android_project;

import static com.example.android_project.InternalStorage.readListInFile;
import static com.example.android_project.InternalStorage.writeListInFile;

import android.content.Context;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Système de gestion des favoris
 */
public class FavStorage { //Singleton

    private static FavStorage instance = null;
    private final List<Integer> fav_list;

    /**
     * Crée le système de gestion en récuperant la liste des favoris en mémoire
     * @param ctx contexte de création pour lire la fav_list
     */
    private FavStorage(Context ctx) {
        fav_list = readListInFile(ctx);
        //System.out.println(fav_list);
    }

    /**
     * Récupère le singleton de la gestion des favoris
     * @param ctx contexte de création
     * @return l'instance de gestion
     */
    public static FavStorage getInstance(Context ctx) {
        if (instance == null)
            instance = new FavStorage(ctx);
        return instance;
    }

    /**
     * Test si l'id correspond à un jeu favori
     * @param appid jeu à tester
     * @return true si le jeu est favori
     */
    public boolean isFav(int appid) {
        return fav_list.contains(appid);
    }

    /**
     * Retourne la liste des jeux favoris
     * @return les ids des jeux favoris
     */
    public List<Integer> getFav_list() {
        return Collections.unmodifiableList(fav_list);
    }

    /**
     * Ajoute un jeu à la liste des favoris
     * @param appid l'id du jeu à ajouter
     * @param ctx contexte pour l'écriture en mémoire
     */
    public void addToFav(int appid, Context ctx) {
        if (!isFav(appid)) {
            try {
                fav_list.add(appid);
                writeListInFile(fav_list, ctx);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //System.out.println("add " + appid + " liste = " + fav_list + " file = " + readListInFile(ctx));
    }

    /**
     * Supprime un id de la liste des favoris
     * @param appid l'id du jeu
     * @param ctx contexte pour l'écriture en mémoire
     */
    public void removeFromFav(int appid, Context ctx) {
        if (isFav(appid)) {
            try {
                fav_list.remove((Integer) appid);
                writeListInFile(fav_list, ctx);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //System.out.println("remove liste = " + fav_list + " file = " + readListInFile(ctx));
    }


}

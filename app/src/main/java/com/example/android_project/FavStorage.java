package com.example.android_project;

import static com.example.android_project.InternalStorage.readListInFile;
import static com.example.android_project.InternalStorage.writeListInFile;

import android.content.Context;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class FavStorage { //Singleton

    private static FavStorage instance = null;
    private final List<Integer> fav_list;

    private FavStorage(Context ctx) {
        System.out.println("new favstorage instance");
        fav_list = readListInFile(ctx);
        System.out.println(fav_list);
    }

    public static FavStorage getInstance(Context ctx) {
        if (instance == null)
            instance = new FavStorage(ctx);
        return instance;
    }

    public boolean isFav(int appid) {
        return fav_list.contains(appid);
    }

    public List<Integer> getFav_list() {
        return Collections.unmodifiableList(fav_list);
    }

    public void addToFav(int appid, Context ctx) {
        if (!isFav(appid)) {
            try {
                fav_list.add(appid);
                writeListInFile(fav_list, ctx);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("add " + appid + " liste = " + fav_list + " file = " + readListInFile(ctx));
    }

    public void removeFromFav(int appid, Context ctx) {
        if (isFav(appid)) {
            try {
                fav_list.remove((Integer) appid);
                writeListInFile(fav_list, ctx);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("remove liste = " + fav_list + " file = " + readListInFile(ctx));
    }


}

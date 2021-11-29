package com.example.android_project;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.Collections;
import java.util.LinkedList;
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
                writeListInFile(fav_list,ctx);
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
                writeListInFile(fav_list,ctx);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("remove liste = " + fav_list + " file = " + readListInFile(ctx));
    }

    private void writeListInFile (List<Integer> list, Context ctx) throws IOException {
        FileOutputStream fileOutputStream = ctx.openFileOutput("fav_list", Context.MODE_PRIVATE);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        for (int id : list) {
            objectOutputStream.writeInt(id);
        }
        objectOutputStream.close();
        fileOutputStream.close();
    }

    private void addAppIdInFile(int appid, Context ctx) throws IOException { //Doesn't work ?
        FileOutputStream fileOutputStream = ctx.openFileOutput("fav_list", Context.MODE_APPEND);
        ObjectOutputStream outputStream = new ObjectOutputStream(fileOutputStream);
        outputStream.writeInt(appid);
        outputStream.close();
        fileOutputStream.close();
    }

    private List<Integer> readListInFile(Context ctx) {
        List<Integer> fav_list = new LinkedList<>();
        try {
            FileInputStream fileInputStream = ctx.openFileInput("fav_list");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            int id;
            do {
                try {
                    id = objectInputStream.readInt();
                    fav_list.add(id);
                } catch (IOException e) {
                    id = -1;
                }
            } while (id != -1);
            objectInputStream.close();
            fileInputStream.close();
            return fav_list;
        } catch (IOException e) {
            return fav_list;
        }
    }

}

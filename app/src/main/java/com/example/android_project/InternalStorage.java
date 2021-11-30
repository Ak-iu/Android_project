package com.example.android_project;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Classe abstraite contenant des fonctions de gestion de la mémoire
 */

public abstract class InternalStorage {

    public static void writeFileOnInternalStorage(Context mcoContext, String sFile, String sBody) {
        File dir = mcoContext.getFilesDir();
        try {
            File gpxfile = new File(dir, sFile);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void writeFileOnInternalStorage(Context mcoContext, String sFile, Object object, int mode) throws IOException {
        FileOutputStream fileOutputStream = mcoContext.openFileOutput(sFile, mode);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(object);
        objectOutputStream.close();
    }

    public static void writeLongOnInternalStorage(Context mcoContext, String sFile, Long n, int mode) throws IOException {
        FileOutputStream fileOutputStream = mcoContext.openFileOutput(sFile, mode);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeLong(n);
        objectOutputStream.close();
    }

    public static String readStringOnInternalStorage(Context mcoContext, String sFile) throws IOException {
        FileInputStream in = mcoContext.openFileInput(sFile);
        String string = "";
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        while (true) {
            try {
                if ((string = reader.readLine()) == null) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            stringBuilder.append(string).append("\n");
        }
        in.close();
        return stringBuilder.toString();
    }

    public static Map<Integer, String> readMapOnInternalStorage(Context mcoContext, String sFile) throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = mcoContext.openFileInput(sFile);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        Map<Integer, String> hmap = (Map<Integer, String>) objectInputStream.readObject();
        objectInputStream.close();

        return hmap;
    }

    public static Long readLongOnInternalStorage(Context context, String sFile) throws IOException {
        ObjectInputStream objectInputStream = new ObjectInputStream(context.openFileInput(sFile));
        Long n = objectInputStream.readLong();
        objectInputStream.close();
        return n;
    }

    public static void writeListInFile(List<Integer> list, Context ctx) throws IOException {
        FileOutputStream fileOutputStream = ctx.openFileOutput("fav_list", Context.MODE_PRIVATE);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        for (int id : list) {
            objectOutputStream.writeInt(id);
        }
        objectOutputStream.close();
        fileOutputStream.close();
    }

    public static void addAppIdInFile(int appid, Context ctx) throws IOException { //Doesn't work ?
        FileOutputStream fileOutputStream = ctx.openFileOutput("fav_list", Context.MODE_APPEND);
        ObjectOutputStream outputStream = new ObjectOutputStream(fileOutputStream);
        outputStream.writeInt(appid);
        outputStream.close();
        fileOutputStream.close();
    }

    public static List<Integer> readListInFile(Context ctx) {
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

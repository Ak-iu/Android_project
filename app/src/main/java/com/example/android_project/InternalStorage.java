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
import java.util.Map;

public class InternalStorage {

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

}

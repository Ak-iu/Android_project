package com.example.android_project;

import android.content.Context;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

public class InternalStorage {


    public void writeFileOnInternalStorage(Context mcoContext, String sFile, String sBody) {
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
    public void writeFileOnInternalStorage(Context mcoContext,String sFile,HashMap<Integer,String> hmap) throws IOException {
        String path = mcoContext.getFilesDir() +"/"+ sFile;
        FileOutputStream fileOutputStream = new FileOutputStream(path);
        ObjectOutputStream objectOutputStream= new ObjectOutputStream(fileOutputStream);

        objectOutputStream.writeObject(hmap);
        objectOutputStream.close();
    }
    public String readStringOnInternalStorage(Context mcoContext, String sFile) throws IOException {
        String path = mcoContext.getFilesDir() +"/"+ sFile;
        FileInputStream in = new FileInputStream(path);
        String string = "";
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        while (true) {
            try {
                if ((string = reader.readLine()) == null) break;
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            stringBuilder.append(string).append("\n");
        }
        in.close();
        return stringBuilder.toString();
    }

    public HashMap<Integer,String> readHashMapOnInternalStorage(Context mcoContext, String sFile) throws IOException, ClassNotFoundException {
        String path = mcoContext.getFilesDir() +"/"+ sFile;
        FileInputStream fileInputStream  = new FileInputStream(path);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

        Map hmap = (HashMap) objectInputStream.readObject();
        objectInputStream.close();

        return (HashMap) hmap;
    }

}

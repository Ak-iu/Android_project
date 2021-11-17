package com.example.android_project;

import android.content.Context;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class InternalStorage {


    public void writeFileOnInternalStorage(Context mcoContext, String sFileName, String sBody) {
        File dir = new File(mcoContext.getFilesDir(),"steam_tracker");
        if (!dir.exists()) {
            dir.mkdir();
        }

        try {
            File gpxfile = new File(dir, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public String ReadOnInternalStorage(Context mcoContext, String path) throws IOException {
        path = mcoContext.getFilesDir() + path;
        FileInputStream in = new FileInputStream(path);
        String string = "";
        StringBuilder stringBuilder = new StringBuilder();
        //InputStream is = new FileInputStream(in);
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
        Toast.makeText(mcoContext, stringBuilder.toString(),
                Toast.LENGTH_LONG).show();

        return stringBuilder.toString();
    }
}
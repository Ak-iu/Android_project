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

    /**
     * @param mcoContext contexte de l'écriture
     * @param sFile nom du fichier dans lequel écrire
     * @param sBody string à écrire dans le fichier
     */
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

    /**
     * Écrit un objet dans la mémoire
     * @param mcoContext contexte d'écriture
     * @param sFile nom du fichier
     * @param object objet à écrire
     * @param mode mode d'écriture (private ou append)
     * @throws IOException problème lors de l'écriture
     */
    public static void writeFileOnInternalStorage(Context mcoContext, String sFile, Object object, int mode) throws IOException {
        FileOutputStream fileOutputStream = mcoContext.openFileOutput(sFile, mode);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(object);
        objectOutputStream.close();
    }

    /**
     * Écrit un long dans la mémoire
     * @param mcoContext contexte d'écriture
     * @param sFile nom du fichier
     * @param n long à écrire
     * @param mode mode d'écriture (private ou append)
     * @throws IOException problème lors de l'écriture
     */
    public static void writeLongOnInternalStorage(Context mcoContext, String sFile, Long n, int mode) throws IOException {
        FileOutputStream fileOutputStream = mcoContext.openFileOutput(sFile, mode);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeLong(n);
        objectOutputStream.close();
    }

    /**
     * Lit un String depuis la mémoire
     * @param mcoContext contexte de lecture
     * @param sFile nom du fichier
     * @throws IOException problème lors de l'écriture
     */
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

    /**
     * Lit une map int->string depuis la mémoire
     * @param mcoContext contexte de lecture
     * @param sFile nom du fichier
     * @return la map écrite en mémoire
     * @throws IOException erreur lors de l'ouverture ou la fermeture du fichier
     * @throws ClassNotFoundException l'objet n'a pas pu être lu dans le fichier
     */
    public static Map<Integer, String> readMapOnInternalStorage(Context mcoContext, String sFile) throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = mcoContext.openFileInput(sFile);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        Map<Integer, String> hmap = (Map<Integer, String>) objectInputStream.readObject();
        objectInputStream.close();

        return hmap;
    }

    /**
     * Lit un long depuis la mémoire
     * @param context contexte de lecture
     * @param sFile nom du fichier
     * @return le long écrit en mémoire
     * @throws IOException erreur lors de l'ouverture ou la fermeture du fichier
     */
    public static Long readLongOnInternalStorage(Context context, String sFile) throws IOException {
        ObjectInputStream objectInputStream = new ObjectInputStream(context.openFileInput(sFile));
        Long n = objectInputStream.readLong();
        objectInputStream.close();
        return n;
    }

    /**
     * Écrit une liste d'entiers en mémoire dans le ficier fav_list
     * @param list liste à écrire en mémoire
     * @param ctx contexte d'écriture
     * @throws IOException problème lors de l'écriture
     */
    public static void writeListInFile(List<Integer> list, Context ctx) throws IOException {
        FileOutputStream fileOutputStream = ctx.openFileOutput("fav_list", Context.MODE_PRIVATE);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        for (int id : list) {
            objectOutputStream.writeInt(id);
        }
        objectOutputStream.close();
        fileOutputStream.close();
    }

    /**
     * Ajoute l'id d'un jeu dans la liste en mémoire dans le fichier fav_list
     * @param appid id du jeu
     * @param ctx contexte d'écriture
     * @throws IOException erreur lors de l'écriture
     */
    public static void addAppIdInFile(int appid, Context ctx) throws IOException { //Doesn't work ?
        FileOutputStream fileOutputStream = ctx.openFileOutput("fav_list", Context.MODE_APPEND);
        ObjectOutputStream outputStream = new ObjectOutputStream(fileOutputStream);
        outputStream.writeInt(appid);
        outputStream.close();
        fileOutputStream.close();
    }

    /**
     * Lit une liste depuis le fichier fav_list
     * @param ctx contexte de lecture
     * @return la liste écrite en mémoire
     */
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

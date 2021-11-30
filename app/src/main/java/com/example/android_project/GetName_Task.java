package com.example.android_project;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class GetName_Task extends AsyncTask {
    private final String urlDetails = "https://store.steampowered.com/api/appdetails?";
    private final int appid;
    private boolean internet_error = false;

    //Results
    private String name;

    public GetName_Task(int appid) {
        this.appid = appid;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        try {
            internet_error = false;
            name = getName();
        } catch (IOException e) {
            internet_error = true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getName() throws IOException, JSONException {
        URL url = new URL(urlDetails + "appids=" + appid);
        URLConnection c = url.openConnection();
        //System.out.println(url);
        c.connect();
        BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line).append("\n");
        }
        br.close();
        String result = sb.toString();

        JSONObject data = new JSONObject(result).getJSONObject(appid + "").getJSONObject("data");
        return data.getString("name");
    }

    @Override
    protected void onPostExecute(Object o) {
        GameMap gameMap = GameMap.getInstance();
        if (!internet_error) {
            gameMap.put(appid, name);
            gameMap.notifyListeners();
        } else
            gameMap.notifyErrorListeners();
    }
}

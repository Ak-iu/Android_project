package com.example.android_project;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class GetDetails_Task extends AsyncTask {
    private String urlPlayers = "https://api.steampowered.com/ISteamUserStats/GetNumberOfCurrentPlayers/v1/?";
    private String urlDetails = "https://store.steampowered.com/api/appdetails?";
    private int appid;
    private boolean internet_error = false;
    private DetailsActivity parent;

    //Results
    private int result_player_count;
    private String result_details;

    public GetDetails_Task(DetailsActivity parent, int appid) {
        this.appid = appid;
        this.parent = parent;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        try {
            result_player_count = getPlayerCount();
            result_details = getDetails();

        } catch (IOException e) {
            internet_error = true;
            System.out.println("Internet Error");
            //e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private int getPlayerCount() throws JSONException , IOException{
        URL url = new URL(urlPlayers + "appid=" + appid);
        URLConnection c = url.openConnection();
        System.out.println(url);
        c.connect();
        BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line).append("\n");
        }
        br.close();
        String result = sb.toString();
        JSONObject obj = new JSONObject(result);
        return obj.getJSONObject("response").getInt("player_count");
    }

    private String getDetails() throws IOException, JSONException{
        URL url = new URL(urlDetails + "appids=" + appid + "&l="+ parent.getString(R.string.lang) + "&cc=" + parent.getString(R.string.currency));
        URLConnection c = url.openConnection();
        System.out.println(url);
        c.connect();
        BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line).append("\n");
        }
        br.close();
        String result = sb.toString();

        StringBuilder details = new StringBuilder();

        JSONObject data = new JSONObject(result).getJSONObject(appid+"").getJSONObject("data");

        //Prix
        JSONObject prix = data.getJSONObject("price_overview");
        int discount = prix.getInt("discount_percent");
        if (discount > 0) {
            details.append(parent.getString(R.string.discount_price)).append(" : ").append(prix.getString("final_formatted")).append("\n");
            details.append(parent.getString(R.string.discount)).append(" : -").append(discount).append("%\n\n");
        }
        else {
            details.append(parent.getString(R.string.price)).append(" : ").append(prix.getString("final_formatted")).append("\n\n");
        }

        //Description
        details.append(parent.getString(R.string.details_descr)).append(" :\n").append(data.getString("short_description")).append("\n\n");

        //Developpeurs
        details.append(parent.getString(R.string.devs)).append(" :");
        JSONArray devs = data.getJSONArray("developers");
        for (int i=0; i<devs.length(); i++)
            details.append("\n - ").append(devs.get(i));
        details.append("\n\n");

        //Editeurs
        details.append(parent.getString(R.string.publish)).append(" :");
        JSONArray publishers = data.getJSONArray("publishers");
        for (int i=0; i<publishers.length(); i++)
            details.append("\n - ").append(publishers.get(i));
        details.append("\n\n");

        //Date de sortie
        details.append(parent.getString(R.string.release_date)).append(" : ");
        details.append(data.getJSONObject("release_date").getString("date"));
        details.append("\n\n");

        //Score metacritic
        details.append(parent.getString(R.string.score_mc)).append(" : ");
        details.append(data.getJSONObject("metacritic").getInt("score"));
        details.append("\n\n");

        return details.toString();
    }

    @Override
    protected void onPostExecute(Object o) {
        if (!internet_error)
            parent.returnResultGetDetails(result_player_count,result_details);
        else
            parent.internetError();
    }
}

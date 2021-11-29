package com.example.android_project;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import java.util.HashMap;
import java.util.Map;

public class FavActivity extends AppCompatActivity implements GamesListFragment.OnListFragmentInteractionListener {

    private GamesListFragment gamesListFragment;
    private FavStorage favStorage;
    private GameMap game_map;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav);

        game_map = GameMap.getInstance();
        favStorage = FavStorage.getInstance(this);
        gamesListFragment = new GamesListFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.GamesFavFragment, gamesListFragment).commit();

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onResume() {
        super.onResume();
        Map<Integer, String> games_fav = new HashMap<>();
        for (int id : favStorage.getFav_list())
            games_fav.put(id, game_map.get(id));
        gamesListFragment.updateList(games_fav, "");
    }

    @Override
    public void onListFragmentInteraction(int appid, String name) {
        Intent intent = new Intent(this,DetailsActivity.class);
        intent.putExtra("name",name);
        intent.putExtra("appid",appid);
        System.out.println(name + " : " +appid);
        startActivity(intent);
    }
}
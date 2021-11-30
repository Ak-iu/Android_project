package com.example.android_project;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MostPlayedGamesActivity extends AppCompatActivity implements GamesListFragment.OnListFragmentInteractionListener,GameMap.GameMapListener{

    private ArrayList<Integer> MostPlayedGamesList;

    public void setMpListLoaded(boolean mpListLoaded) {
        this.mpListLoaded = mpListLoaded;
    }

    private boolean mpListLoaded = false;
    private GamesListFragment gamesListFragment;
    private GameMap game_map;
    private TextView alert_text = null;
    private Button retry_button = null;
    private BottomNavigationView bottomNavigationView;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_most_played_game);

        GetGameMPList();

        game_map = GameMap.getInstance();
        game_map.addListener(this);

        gamesListFragment = new GamesListFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.GamesMostPlayedFragment, gamesListFragment).commit();

        alert_text = findViewById(R.id.alert_text_mostPlayed);
        retry_button = findViewById(R.id.btnRetry_mostPlayed);
        retry_button.setVisibility(View.INVISIBLE);
        bottomNavigationView = findViewById(R.id.bottom_navigation_mostPlayed);
        configureBottomView();

        alert_text.setText(getString(R.string.loading_game_list));

        if (!game_map.isWaiting())
            notifyError();

        retry_button.setOnClickListener(v -> {
            alert_text.setText(R.string.loading_game_list);
            alert_text.setTextColor(getColor(R.color.white));
            retry_button.setVisibility(View.INVISIBLE);
            GetGameList();
        });
    }


    public void setGameMPList(ArrayList<Integer> gameList) {
        MostPlayedGamesList = gameList;
    }

    private void configureBottomView() {
        bottomNavigationView.setSelectedItemId(R.id.mostPlayed);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> updateMainFragment(item.getItemId()));
    }

    private boolean updateMainFragment(Integer integer) {
        switch (integer) {
            case R.id.search:
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.favorites:
                Intent intent_f = new Intent(this, FavActivity.class);
                startActivity(intent_f);
                //game_map.removeListener(this);
                finish();
                break;
            case R.id.mostPlayed:
                /*Intent intent_mp = new Intent(this, MostPlayedGamesActivity.class);
                startActivity(intent_mp);
                game_map.removeListener(this);
                finish();*/
                break;

        }

        return true;
    }

    @Override
    public void onListFragmentInteraction(int appid, String name) {
        Intent intent = new Intent(this, MostPlayedGamesActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("appid", appid);
        System.out.println(name + " : " + appid);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void notifyUpdate() {
        Map<Integer, String> games_mp = new HashMap<>();

        if( mpListLoaded ) {
            for (int id : MostPlayedGamesList)
                games_mp.put(id, game_map.get(id));

            alert_text.setVisibility(View.INVISIBLE);
            retry_button.setVisibility(View.INVISIBLE);
            gamesListFragment.updateList(games_mp, "");
        }


    }

    @Override
    public void notifyError() {
        alert_text.setText(getString(R.string.no_internet_connection));
        alert_text.setTextColor(getResources().getColor(R.color.design_default_color_error));
        retry_button.setVisibility(View.VISIBLE);
    }

    private void GetGameList() {
        GetGameList_Task ggl = new GetGameList_Task(this);
        ggl.execute();
    }

    private void GetGameMPList() {
        GetMostPlayedGames_Task gmpg = new GetMostPlayedGames_Task(this);
        gmpg.execute();
    }

}
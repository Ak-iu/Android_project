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

import java.util.HashMap;
import java.util.Map;

public class FavActivity extends AppCompatActivity implements GamesListFragment.OnListFragmentInteractionListener, GameMap.GameMapListener {

    private GamesListFragment gamesListFragment;
    private FavStorage favStorage;
    private GameMap game_map;
    private TextView alert_text = null;
    private Button retry_button = null;
    private BottomNavigationView bottomNavigationView;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav);

        game_map = GameMap.getInstance();
        game_map.addListener(this);

        favStorage = FavStorage.getInstance(this);
        gamesListFragment = new GamesListFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.GamesFavFragment, gamesListFragment).commit();

        alert_text = findViewById(R.id.alert_text_fav);
        retry_button = findViewById(R.id.btnRetry_fav);
        retry_button.setVisibility(View.INVISIBLE);
        bottomNavigationView = findViewById(R.id.bottom_navigation_fav);
        configureBottomView();

        alert_text.setText(getString(R.string.loading_game_list));

        if (game_map.hasMap())
            alert_text.setVisibility(View.INVISIBLE);
        else if (!game_map.isWaiting())
            notifyError();

        retry_button.setOnClickListener(v -> {
            System.out.println("Retry");
            alert_text.setText(R.string.loading_game_list);
            alert_text.setTextColor(getColor(R.color.white));
            retry_button.setVisibility(View.INVISIBLE);
            GetGameList();
        });

    }

    private boolean updateMainFragment(Integer integer) {
        switch (integer){
            case R.id.search:
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                game_map.removeListener(this);
                finish();
                break;

            case R.id.favorites:
                /*Intent intent = new Intent(this,FavActivity.class);
                startActivity(intent);
                game_map.removeListener(this);
                finish();*/
                break;
        }
        return true;
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void notifyUpdate() {
        Map<Integer, String> games_fav = new HashMap<>();
        for (int id : favStorage.getFav_list())
            games_fav.put(id, game_map.get(id));
        alert_text.setVisibility(View.INVISIBLE);
        gamesListFragment.updateList(games_fav, "");
    }

    @Override
    public void notifyError() {
        alert_text.setText(getString(R.string.no_internet_connection));
        alert_text.setTextColor(getResources().getColor(R.color.design_default_color_error));
        retry_button.setVisibility(View.VISIBLE);
    }

    private void configureBottomView(){
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> updateMainFragment(item.getItemId()));
    }

    private void GetGameList() {
        GetGameList_Task ggl = new GetGameList_Task(this);
        ggl.execute();
    }
}
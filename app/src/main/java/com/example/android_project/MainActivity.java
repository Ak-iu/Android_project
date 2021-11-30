package com.example.android_project;

import static android.widget.Toast.LENGTH_SHORT;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Map;

/**
 * Activity permettant de rechercher et lister un ou des jeux en fonction de leur nom
 */

public class MainActivity extends AppCompatActivity implements GamesListFragment.OnListFragmentInteractionListener, GameMap.GameMapListener {

    private GamesListFragment gamesListFragment;
    private TextView alert_text = null;
    private Button retry_button = null;
    private Button search_button = null;
    private BottomNavigationView bottomNavigationView;
    private GameMap game_map;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        EditText game_name_edit = findViewById(R.id.game_name_edit);
        search_button = findViewById(R.id.search_button);
        search_button.setVisibility(View.INVISIBLE);

        alert_text = findViewById(R.id.alert_text);
        retry_button = findViewById(R.id.btnRetry);
        retry_button.setVisibility(View.INVISIBLE);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        configureBottomView();

        show_loading_text();

        //Async Task pour obtenir la liste des jeux
        game_map = GameMap.getInstance();
        game_map.addListener(this);
        if (game_map.hasMap()) {
            alert_text.setVisibility(View.INVISIBLE);
            search_button.setVisibility(View.VISIBLE);
        } else if (!game_map.isWaiting()) {
            GetGameList();
        }

        search_button.setOnClickListener(v -> search_game(game_name_edit));
        retry_button.setOnClickListener(v -> {
            alert_text.setText(R.string.loading_game_list);
            alert_text.setTextColor(getColor(R.color.white));
            retry_button.setVisibility(View.INVISIBLE);
            GetGameList();
        });

        gamesListFragment = new GamesListFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.GamesFoundFragment, gamesListFragment).commit();
    }

    private void configureBottomView() {
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> updateMainFragment(item.getItemId()));
    }

    private boolean updateMainFragment(Integer integer) {
        switch (integer) {
            case R.id.search:
                /*Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                finish();*/
                break;

            case R.id.favorites:
                Intent intent_f = new Intent(this, FavActivity.class);
                startActivity(intent_f);
                game_map.removeListener(this);
                finish();
                break;
            case R.id.mostPlayed:
                Intent intent_mp = new Intent(this, MostPlayedGamesActivity.class);
                startActivity(intent_mp);
                game_map.removeListener(this);
                finish();
                break;

        }
        return true;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void search_game(EditText game_name_edit) {
        // game name typed by user
        String game_name = String.valueOf(game_name_edit.getText());
        // check if the game name is empty
        if (game_name.equals("")) {
            Toast.makeText(getApplicationContext(), getString(R.string.missing_name), LENGTH_SHORT).show();
        }
        // if not null search the game in the list
        else {
            try {
                SearchGame sg = new SearchGame(game_map.getMap());
                Map<Integer, String> gamesFound = sg.search(game_name);

                if (gamesFound.size() == 0)
                    Toast.makeText(getApplicationContext(), getString(R.string.no_game_result), LENGTH_SHORT).show();
                gamesListFragment.updateList(gamesFound, game_name);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void GetGameList() {
        GetGameList_Task ggl = new GetGameList_Task(this);
        ggl.execute();
    }


    public void show_loading_text() {
        alert_text.setText(getString(R.string.loading_game_list));
    }

    @Override
    public void notifyError() {
        alert_text.setText(getString(R.string.no_internet_connection));
        alert_text.setTextColor(getResources().getColor(R.color.design_default_color_error));
        retry_button.setVisibility(View.VISIBLE);
    }

    @Override
    public void onListFragmentInteraction(int appid, String name) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("appid", appid);
        startActivity(intent);
    }

    @Override
    public void notifyUpdate() {
        alert_text.setVisibility(View.INVISIBLE);
        search_button.setVisibility(View.VISIBLE);
    }
}
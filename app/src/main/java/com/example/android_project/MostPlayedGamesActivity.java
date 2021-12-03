package com.example.android_project;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *  L'activitée affiche les 100 jeux les plus joués sur Steam surant les 2 dernières semaines
 */
public class MostPlayedGamesActivity extends AppCompatActivity implements GamesListFragment.OnListFragmentInteractionListener, GameMap.GameMapListener {

    private List<Integer> mostPlayedGamesList;
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

        game_map = GameMap.getInstance();
        game_map.addListener(this);

        gamesListFragment = new GamesListFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.GamesMostPlayedFragment, gamesListFragment).commit();

        alert_text = findViewById(R.id.alert_text_mostPlayed);
        retry_button = findViewById(R.id.btnRetry_mostPlayed);
        retry_button.setVisibility(View.INVISIBLE);
        bottomNavigationView = findViewById(R.id.bottom_navigation_mostPlayed);
        configureBottomView();

        if (CheckPermission.checkPermissionForReadExternalStorage(this) && CheckPermission.checkPermissionForInternet(this)) {
            alert_text.setText(getString(R.string.loading_game_list));

            if (game_map.hasMap())
                alert_text.setVisibility(View.INVISIBLE);
            else if (!game_map.isWaiting())
                notifyError();

            retry_button.setOnClickListener(v -> {
                alert_text.setText(R.string.loading_game_list);
                alert_text.setTextColor(getColor(R.color.white));
                retry_button.setVisibility(View.INVISIBLE);
                GetGameList();
                GetGameMPList();
            });
            GetGameMPList();
        }
    }

    /**
     * Configure le menu de navigation
     */
    private void configureBottomView() {
        bottomNavigationView.setSelectedItemId(R.id.mostPlayed);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> updateMainFragment(item.getItemId()));
    }

    /**
     * Gestion des actions du menu de navigation
     * @param integer ID de la zone clickable sélectionné par l'utilisateur
     * @return true si aucune erreur
     */
    private boolean updateMainFragment(Integer integer) {
        switch (integer) {
            case R.id.search:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                game_map.removeListener(this);
                finish();
                break;

            case R.id.favorites:
                Intent intent_f = new Intent(this, FavActivity.class);
                startActivity(intent_f);
                game_map.removeListener(this);
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

    /**
     *
     * @param appid ID de l'application Steam
     * @param name  Nom de l'application Steam
     */
    @Override
    public void onListFragmentInteraction(int appid, String name) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("appid", appid);
        //System.out.println(name + " : " + appid);
        startActivity(intent);
    }

    /**
     *
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void notifyUpdate() {
        fillFragment();
    }

    /**
     *
     */
    @Override
    public void notifyError() {
        alert_text.setText(getString(R.string.no_internet_connection));
        alert_text.setTextColor(getResources().getColor(R.color.design_default_color_error));
        retry_button.setVisibility(View.VISIBLE);
    }

    /**
     * Gestion du retour sur l'activité
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onResume() {
        super.onResume();
        if (CheckPermission.checkPermissionForReadExternalStorage(this) && CheckPermission.checkPermissionForInternet(this))
            if (mpListLoaded && game_map.hasMap())
                gamesListFragment.updateList(game_map.getMap(), "");
    }

    /**
     * Obtention de liste des jeux Steam de manière Asynchrone
     */
    private void GetGameList() {
        GetGameList_Task ggl = new GetGameList_Task(this);
        ggl.execute();
    }
    /**
     * Obtention de liste des jeux Steam les plys jouées de manière Asynchrone
     */
    private void GetGameMPList() {
        GetMostPlayedGames_Task gmpg = new GetMostPlayedGames_Task(this);
        gmpg.execute();
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void returnList(List<Integer> list) {
        mostPlayedGamesList = list;
        mpListLoaded = true;
        fillFragment();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void fillFragment() {
        if (mpListLoaded && game_map.hasMap()) {
            Map<Integer, String> games_mp = new LinkedHashMap<>();
            for (int id : mostPlayedGamesList) {
                String name = game_map.get(id);
                if (name == null) {
                    new GetName_Task(id).execute();
                }
                games_mp.put(id, name);
            }
            alert_text.setVisibility(View.INVISIBLE);
            retry_button.setVisibility(View.INVISIBLE);
            gamesListFragment.updateList(games_mp, "");
        }
    }
}
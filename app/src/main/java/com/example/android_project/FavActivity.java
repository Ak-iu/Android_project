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

import java.util.HashMap;
import java.util.Map;

/**
 * Activity pour lister et accéder à tous les jeux mis en favoris
 */
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

        alert_text = findViewById(R.id.alert_text_fav);
        retry_button = findViewById(R.id.btnRetry_fav);
        retry_button.setVisibility(View.INVISIBLE);

        bottomNavigationView = findViewById(R.id.bottom_navigation_fav);
        configureBottomView();

        gamesListFragment = new GamesListFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.GamesFavFragment, gamesListFragment).commit();

        if (CheckPermission.checkPermissionForReadExternalStorage(this) && CheckPermission.checkPermissionForInternet(this)) {

            favStorage = FavStorage.getInstance(this);
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
            });
        }
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
                /*Intent intent = new Intent(this,FavActivity.class);
                startActivity(intent);
                game_map.removeListener(this);
                finish();*/
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

    /**
     * Met à jour la map des favoris et le fragment
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onResume() {
        super.onResume();
        if (CheckPermission.checkPermissionForReadExternalStorage(this) && CheckPermission.checkPermissionForInternet(this)) {
            Map<Integer, String> games_fav = new HashMap<>();
            for (int id : favStorage.getFav_list()) {
                String name = game_map.get(id);
                if (name == null)
                    new GetName_Task(id).execute();
                games_fav.put(id, name);
            }
            gamesListFragment.updateList(games_fav, "");
        }
    }

    /**
     * Lance l'activity pour afficher les détails du jeu selectionné
     * @param appid id du jeu
     * @param name nom de jeu
     */
    @Override
    public void onListFragmentInteraction(int appid, String name) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("appid", appid);
        intent.putExtra("name", appid);
        //System.out.println(name + " : " + appid);
        startActivity(intent);
    }

    /**
     * Met à jour la map des favoris et le fragment
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void notifyUpdate() {
        Map<Integer, String> games_fav = new HashMap<>();
        for (int id : favStorage.getFav_list()) {
            String name = game_map.get(id);
            if (name == null)
                new GetName_Task(id).execute();
            games_fav.put(id, name);
        }
        alert_text.setVisibility(View.INVISIBLE);
        gamesListFragment.updateList(games_fav, "");
    }

    /**
     * Afficher les informations et actions d'erreurs
     */
    @Override
    public void notifyError() {
        alert_text.setText(getString(R.string.no_internet_connection));
        alert_text.setTextColor(getResources().getColor(R.color.design_default_color_error));
        retry_button.setVisibility(View.VISIBLE);
    }

    /**
     * Configure le menu de navigation
     */
    private void configureBottomView() {
        bottomNavigationView.setSelectedItemId(R.id.favorites);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> updateMainFragment(item.getItemId()));
    }

    /**
     * Crée et lance une tache pour récupérer la liste des favoris
     */
    private void GetGameList() {
        GetGameList_Task ggl = new GetGameList_Task(this);
        ggl.execute();
    }
}
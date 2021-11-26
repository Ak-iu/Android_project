package com.example.android_project;

import static android.widget.Toast.LENGTH_SHORT;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private GamesListFragment gamesListFragment;
    private TextView alert_text = null;
    private GetGameList_Task ggl = new GetGameList_Task(this);
    private Map<Integer, String> game_map = null;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText game_name_edit = findViewById(R.id.game_name_edit);
        Button search_button = findViewById(R.id.search_button);
        alert_text = findViewById(R.id.alert_text);
        show_loading_text();

        //Async Task pour obtenir la liste des jeux
        ggl.execute();

        search_button.setOnClickListener(v -> search_game(game_name_edit/*, finalGame_map*/));

        gamesListFragment = new GamesListFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.GamesFoundFragment, gamesListFragment).commit();
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void search_game(EditText game_name_edit/*, HashMap<Integer,String> game_map*/) {
        // game name typed by user
        String game_name = String.valueOf(game_name_edit.getText());
        // check if the game name is empty
        if (game_name.equals("")) {
            Toast.makeText(getApplicationContext(), getString(R.string.missing_name), LENGTH_SHORT).show();
        }
        // if not null search the game in the list
        else {
            System.out.println("début du chargement");

            if (!ggl.isFinished())
                Toast.makeText(getApplicationContext(), "Chargement des jeux", LENGTH_SHORT).show();

            System.out.println("fin du chargement");

            try {
                game_map = (Map<Integer, String>) ggl.get();
                System.out.println("map acquise");

                SearchGame sg = new SearchGame(game_map);
                HashMap<Integer, String> gamesFound = sg.search(game_name);

                if (gamesFound.size() == 0)
                    Toast.makeText(getApplicationContext(), getString(R.string.no_game_result), LENGTH_SHORT).show();
                gamesListFragment.updateList(gamesFound, game_name);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void show_loading_text(){
        alert_text.setText(getString(R.string.loading_game_list));
    }

    public void disableAlert() {
        alert_text.setVisibility(View.INVISIBLE);
    }

    public void internetError() {
        alert_text.setText(getString(R.string.no_internet_connection));
        alert_text.setTextColor(getResources().getColor(R.color.orange));
    }
}
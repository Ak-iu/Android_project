package com.example.android_project;

import static android.widget.Toast.LENGTH_SHORT;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    GamesListFragment gamesListFragment;



    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText game_name_edit =  findViewById(R.id.game_name_edit);
        Button search_button = findViewById(R.id.search_button);
        
        // Get the entire game list of steam
        GetGameList ggl = new GetGameList();
        HashMap<Integer,String> game_map = null;
        try {
            game_map = (HashMap<Integer, String>) ggl.execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        HashMap<Integer, String> finalGame_map = game_map;

        search_button.setOnClickListener(v -> search_game(game_name_edit, finalGame_map));


        gamesListFragment = new GamesListFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.GamesFoundFragment, gamesListFragment).commit();
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void search_game(EditText game_name_edit, HashMap<Integer,String> game_map){
        // game name typed by user
        String game_name = String.valueOf(game_name_edit.getText());
        // check if the game name is empty
        if (game_name.equals("")){
            Toast.makeText(getApplicationContext(),getString(R.string.missing_name), LENGTH_SHORT).show();
        }
        // if not null search the game in the list
        else{
            SearchGame sg = new SearchGame(game_map);
            HashMap<Integer,String> gamesFound;
            gamesFound = sg.search(game_name);

            if( gamesFound.size() == 0 )
                Toast.makeText(getApplicationContext(),getString(R.string.no_game_result), LENGTH_SHORT).show();
            gamesListFragment.updateList(gamesFound,game_name);

        }
    }


}
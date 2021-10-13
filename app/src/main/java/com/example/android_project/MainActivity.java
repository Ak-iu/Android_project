package com.example.android_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

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

    }


    public void search_game(EditText game_name_edit, HashMap<Integer,String> game_map){
        // game name typed by user
        String game_name = String.valueOf(game_name_edit.getText());
        // check if the game name is empty
        if (game_name.equals("")){
            Context context = getApplicationContext();
            CharSequence text = getString(R.string.missing_name);
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        // if not null search the game in the list
        else{
            SearchGame sg = new SearchGame(game_map);
            sg.search(game_name);

        }
    }


}
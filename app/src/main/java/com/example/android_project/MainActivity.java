package com.example.android_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText game_name_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        game_name_edit =  findViewById(R.id.game_name_edit);
        Button search_button = findViewById(R.id.search_button);
        search_button.setOnClickListener(v -> search_game());
    }


    public void search_game(){
        String game_name = String.valueOf(game_name_edit.getText());
        if (game_name.equals("")){
            Context context = getApplicationContext();
            CharSequence text = "Please type a game name !";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        else{
            /// search the game
        }

    }
}
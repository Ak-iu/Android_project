package com.example.android_project;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.android_project.databinding.ActivityDetailsBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DetailsActivity extends AppCompatActivity {

    private ActivityDetailsBinding binding;
    private boolean isFavourite;

    //test values
    private String name = "Europa Universalis IV";
    private int appid = 236850;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;
        toolbar.setTitle(getTitle());

        GetDetails_Task gd = new GetDetails_Task(this,appid);
        gd.execute();

        //tmp
        isFavourite = false;

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isFavourite) {
                    Toast.makeText(getApplicationContext(), "Fav deleted", Toast.LENGTH_SHORT).show();
                    fab.setImageDrawable(getResources().getDrawable(R.drawable.dark_blue_star_24));
                    isFavourite = false;
                    removeFromFav();

                } else {
                    Toast.makeText(getApplicationContext(),  "Fav added", Toast.LENGTH_SHORT).show();
                    fab.setImageDrawable(getResources().getDrawable(R.drawable.grey_star_24));
                    isFavourite = true;
                    addToFav();
                }
            }
        });
        if (isFavourite)
            fab.setImageDrawable(getResources().getDrawable(R.drawable.grey_star_24));
        else
            fab.setImageDrawable(getResources().getDrawable(R.drawable.dark_blue_star_24));
    }

    public void addToFav() {
        //TODO
    }

    public void removeFromFav() {
        //TODO
    }

    public void returnResultGetDetails(int player_count, String details) {
        TextView textView = findViewById(R.id.content_details);
        String sb = R.string.details_name + " : " + name + "\n\n" +
                R.string.player_count+ " : " + player_count + "\n\n" +
                details;
        textView.setText(sb);
    }
}
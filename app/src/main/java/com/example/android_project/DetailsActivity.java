package com.example.android_project;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DetailsActivity extends AppCompatActivity {

    public static final String EXTRA_KEY_NAME="name";
    public static final String EXTRA_KEY_APPID="appid";

    private Button retry_button = null;
    private TextView textView = null;
    private FloatingActionButton fab = null;
    private ImageView headerImg = null;

    private boolean isFavourite;

    private String name;
    private int appid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        name = intent.getStringExtra(EXTRA_KEY_NAME);
        appid = intent.getIntExtra(EXTRA_KEY_APPID,-1);

        textView = findViewById(R.id.content_details);
        headerImg = findViewById(R.id.gameHeaderImage);

        retry_button = findViewById(R.id.details_retry);
        retry_button.setVisibility(View.INVISIBLE);
        retry_button.setOnClickListener( v -> {
            textView.setText(R.string.details_loading);
            textView.setTextColor(getResources().getColor(R.color.white));
            retry_button.setVisibility(View.INVISIBLE);
            (new GetDetails_Task(this, appid)).execute();
        });

        fab = findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);

        if (appid == -1) {
            textView.setText(R.string.details_error_id);
            fab.hide();
        }
        else {
            GetDetails_Task gd = new GetDetails_Task(this, appid);
            GetImageHeader_Task gi = new GetImageHeader_Task(this,appid);
            gd.execute();
            gi.execute();

            //TODO
            isFavourite = false;
            fab.setOnClickListener(view -> {
                if (isFavourite) {
                    Toast.makeText(getApplicationContext(), R.string.fav_removed, Toast.LENGTH_SHORT).show();
                    fab.setImageDrawable(getResources().getDrawable(R.drawable.white_add_circle_outline_24));
                    isFavourite = false;
                    removeFromFav();

                } else {
                    Toast.makeText(getApplicationContext(), R.string.fav_added, Toast.LENGTH_SHORT).show();
                    fab.setImageDrawable(getResources().getDrawable(R.drawable.white_star_24));
                    isFavourite = true;
                    addToFav();
                }
            });
            if (isFavourite)
                fab.setImageDrawable(getResources().getDrawable(R.drawable.white_star_24));
            else
                fab.setImageDrawable(getResources().getDrawable(R.drawable.white_add_circle_outline_24));
        }
    }

    public void addToFav() {
        //TODO
    }

    public void removeFromFav() {
        //TODO
    }

    public void returnResultGetDetails(int player_count, String details) {
        String sb = getString(R.string.details_name) + " : " + name + "\n\n" +
                getString(R.string.player_count) + " : " + player_count + "\n\n" +
                details;
        textView.setText(sb);
        fab.setVisibility(View.VISIBLE);
    }

    public void internetError() {
        textView.setText(R.string.no_internet_connection);
        textView.setTextColor(getResources().getColor(R.color.design_default_color_error));
        retry_button.setVisibility(View.VISIBLE);
        fab.setVisibility(View.INVISIBLE);
    }

    public void setHeaderImage(Drawable d){
        headerImg.setImageDrawable(d);
    }
}
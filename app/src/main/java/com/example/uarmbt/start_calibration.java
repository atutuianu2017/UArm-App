package com.example.uarmbt;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;



public class start_calibration extends AppCompatActivity {

    private ImageView image1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_calibration);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ImageButton imageButton =findViewById(R.id.button_calibration1);

        image1 =findViewById(R.id.imageView2);
        final Animation myanim1 = AnimationUtils.loadAnimation(this, R.anim.alpha);
        image1.startAnimation(myanim1);


        imageButton.postDelayed(new Runnable() {
            public void run() {
                imageButton.setVisibility(View.VISIBLE);
            }
        }, 4000);

        //capture button click
        imageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                imageButton.startAnimation(myanim1);
                // Start Main4Activity.class
                Intent myIntent = new Intent(start_calibration.this,
                        step1Activity.class);
                startActivity(myIntent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_start_calibration, menu);
        inflater.inflate(R.menu.menu_start_calibration, menu);


        // return true so that the menu pop up is opened
        return true;
    }

}

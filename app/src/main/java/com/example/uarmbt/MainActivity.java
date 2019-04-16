package com.example.uarmbt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.animation.ObjectAnimator;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView4, imageView5, imageView7;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView5=(ImageView) findViewById(R.id.imageView5);
        imageView4=(ImageView) findViewById(R.id.imageView4);
        imageView7=(ImageView) findViewById(R.id.imageView7);
        Animation myanim = AnimationUtils.loadAnimation(this, R.anim.mytransition);
        Animation myanim2 = AnimationUtils.loadAnimation(this, R.anim.mytransition2);
        Animation myanim3 = AnimationUtils.loadAnimation(this, R.anim.mytransition3);


        imageView4.startAnimation(myanim);
        imageView5.startAnimation(myanim2);
        imageView7.startAnimation(myanim3);

        final Intent i = new Intent(this, start_calibration.class);




        Thread timer = new Thread(){
            public void run(){
                try{
                    sleep(5500);
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
                finally {
                    startActivity(i);
                    finish();
                }
            }
        };
        timer.start();
    }



}



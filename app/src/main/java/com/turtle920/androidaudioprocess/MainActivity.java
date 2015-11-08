package com.turtle920.androidaudioprocess;

import android.media.MediaPlayer;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    MediaPlayer mp = new MediaPlayer();
    float leftVol=0.5f, rightVol=0.5f;
    String song = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Music/tf.mp3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            mp.setDataSource(song);
            mp.prepare();
            mp.setVolume(leftVol,rightVol);
        } catch (Exception e) {
            Log.e("DEBUG", "" + e.toString());
            e.printStackTrace();
        }

        Button button1 = (Button) findViewById(R.id.button_start);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.start();
            }
        });

        Button button2 = (Button) findViewById(R.id.button_pause);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.pause();
            }
        });

        Button button3 = (Button) findViewById(R.id.button_replay);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.stop();
                try {
                    mp.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        Button button4 = (Button)findViewById(R.id.button_l);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (leftVol+0.05f<=1.0f) leftVol+=0.05f;
                if (rightVol-0.05f>=0f) rightVol-=0.05f;
                Log.e("DEBUG",""+leftVol+" "+rightVol);
                mp.setVolume(leftVol, rightVol);
            }
        });

        Button button5 = (Button)findViewById(R.id.button_r);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (leftVol-0.05f>=0f) leftVol-=0.05f;
                if (rightVol+0.05f<=1.0f) rightVol+=0.05f;
                Log.e("DEBUG",""+leftVol+" "+rightVol);
                mp.setVolume(leftVol, rightVol);
            }
        });

    }
}

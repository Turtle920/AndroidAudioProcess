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
    String song = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Music/tf.mp3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            mp.setDataSource(song);
            mp.prepare();
            mp.setVolume(0.5f,0.5f);
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

        Button button4 = (Button)findViewById(R.id.button_up);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.setVolume(0.8f,0.8f);
            }
        });

        Button button5 = (Button)findViewById(R.id.button_down);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.setVolume(0.1f, 0.1f);
            }
        });

    }
}

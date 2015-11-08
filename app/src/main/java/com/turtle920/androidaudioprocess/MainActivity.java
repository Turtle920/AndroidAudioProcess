package com.turtle920.androidaudioprocess;

import android.media.MediaPlayer;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        

        MediaPlayer mp = new MediaPlayer();
        String song = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Music/tf.mp3";
        try {
            mp.setDataSource(song);
            mp.prepare();
            mp.start();
        } catch (Exception e) {
            Log.e("DEBUG", "" + e.toString());
            e.printStackTrace();
        }
    }
}

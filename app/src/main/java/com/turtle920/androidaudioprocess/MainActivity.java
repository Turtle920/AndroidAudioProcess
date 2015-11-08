package com.turtle920.androidaudioprocess;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;

import java.text.DecimalFormat;

public class MainActivity extends Activity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor sensor;
    private float[] gravity = {10, 10, 10};
    private float[] acc = new float[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);//TYPE_GYROSCOPE
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override  /* 对于陀螺仪，测量的是x、y、z三个轴向的角速度，分别从values[0]、values[1]、values[2]中读取，单位为弧度/秒。*/
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            showInfo("事件：" + " x:" + event.values[0] + " y:" + event.values[1] + " z:" + event.values[2]);

        for (int i = 0; i < 3; i++) {
            gravity[i] = (float) (0.1 * event.values[i] + 0.9 * gravity[i]);
            acc[i] = event.values[i] - gravity[i];
        }
        DecimalFormat df = new DecimalFormat("0.00000 ");
        Log.e("acc", "acc:" + df.format(acc[0]) + " " + df.format(acc[1]) + " " + df.format(acc[2]));
    }

    //在华为P6的机器上，陀螺仪非常敏感，平放在桌面，由于电脑照成的轻微震动在不断地刷屏，为了避免写UI造成的性能问题，只写Log。
    private void showInfo(String info) {
        //tv.append("\n" + info);
        //Log.d("陀螺仪", info);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}

package com.turtle920.androidaudioprocess;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

//实现传感器事件监听：SensorEventListener
public class MainActivity extends Activity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor acc_sensor;
    private Sensor mag_sensor;
    //加速度传感器数据
    float accValues[] = new float[3];
    //地磁传感器数据
    float magValues[] = new float[3];
    //旋转矩阵，用来保存磁场和加速度的数据
    float r[] = new float[9];
    //模拟方向传感器的数据（原始数据为弧度）
    float values[] = new float[3];
    int counter = 0;

    //校准标准值
    float caliValues[] = new float[3];
    //校准缓存值
    float caliValuesCache[] = new float[3];
    //校准开关
    boolean caliOn = false;
    //校准累计次数
    int caliTimes = 0;
    //校准时需要读取传感器数据的次数
    private final int CALI_REQUIREMENT = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        acc_sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mag_sensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        //给传感器注册监听：
        sensorManager.registerListener(this, acc_sensor, SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(this, mag_sensor, SensorManager.SENSOR_DELAY_FASTEST);

        Button button = (Button) findViewById(R.id.button_calibrate);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                caliOn = true;
            }
        });
    }

    //传感器状态改变时的回调方法
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            accValues = event.values;
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            magValues = event.values;
        }
        /**public static boolean getRotationMatrix (float[] R, float[] I, float[] gravity, float[] geomagnetic)
         * 填充旋转数组r
         * r：要填充的旋转数组
         * I:将磁场数据转换进实际的重力坐标中 一般默认情况下可以设置为null
         * gravity:加速度传感器数据
         * geomagnetic：地磁传感器数据
         */
        SensorManager.getRotationMatrix(r, null, accValues, magValues);
        /**
         * public static float[] getOrientation (float[] R, float[] values)
         * R：旋转数组
         * values ：模拟方向传感器的数据
         */

        SensorManager.getOrientation(r, values);

        if (caliOn) {
            //对本次测到的实测值和前面几次进行加权平均
            caliValuesCache[0] = (caliValuesCache[0] * caliTimes + values[0] * 1) / (caliTimes + 1);
            caliValuesCache[1] = (caliValuesCache[1] * caliTimes + values[1] * 1) / (caliTimes + 1);
            caliValuesCache[2] = (caliValuesCache[2] * caliTimes + values[2] * 1) / (caliTimes + 1);
            caliTimes++;
            TextView textView = (TextView)findViewById(R.id.textView_percent);
            textView.setText(""+(caliTimes*100/CALI_REQUIREMENT)+"%");
        }

        if (caliTimes == CALI_REQUIREMENT) {
            caliOn = false;
            caliTimes = 0;
            //从缓存里把N次平均数赋给校准标准值
            for (int i = 0; i < 3; i++) {
                caliValues[i]=caliValuesCache[i];
            }
            caliValuesCache[0] = 0;
            caliValuesCache[1] = 0;
            caliValuesCache[2] = 0;//清空校准缓存
            TextView textView = (TextView) findViewById(R.id.textView_cali);
            textView.setText(String.format("STDOrient: %+f, %+f, %+f",
                    Math.toDegrees(caliValues[0]), Math.toDegrees(caliValues[1]), Math.toDegrees(caliValues[2])));

        }

        if (counter++ % 10 == 1) {
            //Log.e("DEBUG", "x:"+Math.toDegrees(values[0])+" y:"+Math.toDegrees(values[1])+" z:"+Math.toDegrees(values[2]));
            /*Log.e("DEBUG", String.format("Orientation: %+f, %+f, %+f",
                    Math.toDegrees(values[0]), Math.toDegrees(values[1]), Math.toDegrees(values[2]))
                    + "");*/
            TextView textView1 = (TextView) findViewById(R.id.textView_sensorData);
            textView1.setText(String.format("ABSOrient: %+f, %+f, %+f",
                    Math.toDegrees(values[0]), Math.toDegrees(values[1]), Math.toDegrees(values[2])));

            TextView textView2 = (TextView) findViewById(R.id.textView_relativeData);
            textView2.setText(String.format("RLTOrient: %+d, %+d, %+d",
                    (int)Math.toDegrees(values[0] - caliValues[0]), (int)Math.toDegrees(values[1] - caliValues[1]), (int)Math.toDegrees(values[2] - caliValues[2])));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}

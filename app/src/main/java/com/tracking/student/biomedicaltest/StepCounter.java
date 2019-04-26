package com.tracking.student.biomedicaltest;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class StepCounter extends AppCompatActivity implements SensorEventListener {
     private SensorManager sensorManager;
    private boolean color = false;
    EditText vmax,vmin,cval;
    private TextView view;
    private long lastUpdate;
    int counter=0;
    double max_val=2,min_val=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_counter);
        view = findViewById(R.id.textView);
        //view.setBackgroundColor(Color.GREEN);
        vmax=findViewById(R.id.max_val);
        vmin=findViewById(R.id.min_val);
        //cval=findViewById(R.id.countervalue);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lastUpdate = System.currentTimeMillis();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            getAccelerometer(event);
        }
    }
    private void getAccelerometer(SensorEvent event) {
        float[] values = event.values;
        // Movement
        float x = values[0];
        float y = values[1];
        float z = values[2];
        float accelationSquareRoot = (x * x + y * y + z * z)/ (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
        long actualTime = event.timestamp;
        if(vmax.getText().toString().equals("") || vmin.getText().toString().equals(""))
        {
            max_val=2;
            min_val=1;
        }
        else {
            max_val = Float.valueOf(vmax.getText().toString());
            min_val = Float.valueOf(vmin.getText().toString());
        }
        if(accelationSquareRoot>=min_val && accelationSquareRoot<=max_val)
        {

            if (actualTime - lastUpdate < 200) {
                return;
            }
            counter++;

            //view.setText("Counter: "+String.valueOf(counter)+" Acceleration: "+accelationSquareRoot+" Actual Time: "+String.valueOf(actualTime));
        }
        view.setText(String.valueOf(counter/2));
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void sstart() {
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void sstop() {
        sensorManager.unregisterListener(this);
        counter=0;
        this.view.setText("0");

    }

    public void stop(View view) {
        sstop();
    }

    public void start(View view) {
    sstart();
    }


    public void mwalk(View view) {
        EditText et=findViewById(R.id.mwalk);
        TextView tv=findViewById(R.id.mwalkresult);
        double countingval=Double.valueOf(this.view.getText().toString());
        double meter=Double.valueOf(et.getText().toString());
        double res=meter/countingval;
        tv.setText(String.valueOf(res));
    }
}

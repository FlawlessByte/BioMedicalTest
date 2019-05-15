package com.tracking.student.biomedicaltest;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import androidx.appcompat.app.AppCompatActivity;

public class StepCounter extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private boolean color = false;
    EditText vmax,vmin,cval;
    private TextView viewCounter;
    private long lastUpdate;
    int counter=0;
    private LineChart lineChart;
    double max_val=2,min_val=1;
    private Thread thread;
    private boolean plotData = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_counter);
        viewCounter = findViewById(R.id.textViewCounter);

        lineChart = (LineChart) findViewById(R.id.chart);
        lineChart.getDescription().setEnabled(true);
        lineChart.getDescription().setText("Real Time Accelerometer Plot");
        lineChart.setTouchEnabled(false);
        lineChart.setDragEnabled(false);
        lineChart.setScaleEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.setPinchZoom(false);
        lineChart.setBackgroundColor(Color.WHITE);

        LineData data = new LineData();
        data.setValueTextColor(Color.WHITE);

        lineChart.setData(data);


        // get the legend (only possible after setting data)
        Legend l = lineChart.getLegend();

        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.WHITE);

        XAxis xl = lineChart.getXAxis();
        xl.setTextColor(Color.WHITE);
        xl.setDrawGridLines(true);
        xl.setAvoidFirstLastClipping(true);
        xl.setEnabled(true);

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMaximum(10f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setEnabled(false);

        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.setDrawBorders(false);

        feedMultiple();

        //view.setBackgroundColor(Color.GREEN);
        vmax=findViewById(R.id.max_val);
        vmin=findViewById(R.id.min_val);
        //cval=findViewById(R.id.countervalue);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lastUpdate = System.currentTimeMillis();
    }


    private void addEntry(SensorEvent event) {

        LineData data = lineChart.getData();

        if (data != null) {

            ILineDataSet set = data.getDataSetByIndex(0);
            // set.addEntry(...); // can be called as well

            if (set == null) {
                set = createSet();
                data.addDataSet(set);
            }

//            data.addEntry(new Entry(set.getEntryCount(), (float) (Math.random() * 80) + 10f), 0);
            data.addEntry(new Entry(set.getEntryCount(), event.values[0] + 5), 0);
            data.notifyDataChanged();

            // let the chart know it's data has changed
            lineChart.notifyDataSetChanged();

            // limit the number of visible entries
            lineChart.setVisibleXRangeMaximum(150);
            // mChart.setVisibleYRange(30, AxisDependency.LEFT);

            // move to the latest entry
            lineChart.moveViewToX(data.getEntryCount());

        }
    }

    private LineDataSet createSet() {

        LineDataSet set = new LineDataSet(null, "Dynamic Data");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setLineWidth(3f);
        set.setColor(Color.MAGENTA);
        set.setHighlightEnabled(false);
        set.setDrawValues(false);
        set.setDrawCircles(false);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setCubicIntensity(0.2f);
        return set;
    }

    private void feedMultiple() {

        if (thread != null){
            thread.interrupt();
        }

        thread = new Thread(new Runnable() {

            @Override
            public void run() {
                while (true){
                    plotData = true;
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        });

        thread.start();
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (thread != null) {
            thread.interrupt();
        }
        sensorManager.unregisterListener(this);
    }


    @Override
    protected void onDestroy() {
        sensorManager.unregisterListener(StepCounter.this);
        thread.interrupt();
        super.onDestroy();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(plotData){
            addEntry(event);
            plotData = false;
        }

        Log.d("SensorEvent","detected");
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            getAccelerometer(event);
            Log.d("SensorType","Accelerometer");
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
        Log.d("Counter", String.valueOf(counter/2));
        viewCounter.setText(String.valueOf(counter/2));
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void sstart() {
        Log.d("StartButton","pressed");
        Log.d("StartButton", "registering sensor");
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void sstop() {
        Log.d("StopButton","pressed");
        sensorManager.unregisterListener(this);
        counter=0;
        this.viewCounter.setText("0");

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
        double countingval=Double.valueOf(this.viewCounter.getText().toString());
        double meter=Double.valueOf(et.getText().toString());
        double res=meter/countingval;
        tv.setText(String.valueOf(res));
    }
}

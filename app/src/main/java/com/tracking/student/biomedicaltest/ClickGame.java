package com.tracking.student.biomedicaltest;
    
import android.os.CountDownTimer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tracking.student.biomedicaltest.R;

import java.util.Random;

import androidx.appcompat.app.AppCompatActivity;

public class ClickGame extends AppCompatActivity implements View.OnClickListener {
    Random r;
    long mil;
    EditText clickval;
    TextView c;
    int flag=0;
    int counter=0;
    Button b1,b2,b3,b4,b5,b6,b7,b8,b9;
    long svalue = 0,fvalue=0;
    double resvalue;
    CountDownTimer countTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_game);
        clickval=findViewById(R.id.clicktime);
        initViews();

    }

    private void initViews() {
        b1=findViewById(R.id.b1);b1.setOnClickListener(this);
        b2=findViewById(R.id.b2);b2.setOnClickListener(this);
        b3=findViewById(R.id.b3);b3.setOnClickListener(this);
        b4=findViewById(R.id.b4);b4.setOnClickListener(this);
        b5=findViewById(R.id.b5);b5.setOnClickListener(this);
        b6=findViewById(R.id.b6);b6.setOnClickListener(this);
        b7=findViewById(R.id.b7);b7.setOnClickListener(this);
        b8=findViewById(R.id.b8);b8.setOnClickListener(this);
        b9=findViewById(R.id.b9);b9.setOnClickListener(this);
        c=findViewById(R.id.counting);
        viewGone();

    }
    public void viewGone() {
            b1.setVisibility(View.GONE);
            b2.setVisibility(View.GONE);
            b3.setVisibility(View.GONE);
            b4.setVisibility(View.GONE);
            b5.setVisibility(View.GONE);
            b6.setVisibility(View.GONE);
            b7.setVisibility(View.GONE);
            b8.setVisibility(View.GONE);
            b9.setVisibility(View.GONE);
    }

    public int randomNumber(int startval,int endval)
    {
        r = new Random();
        int i1 = r.nextInt(endval - startval) + startval;

        return i1;
    }
    CountDownTimer countDownTimer;

    @Override
    protected void onPause() {
        super.onPause();
        countDownTimer.cancel();
    }

    public void loopermethod(String s)
    {
        countDownTimer=new CountDownTimer(Long.valueOf(s)*1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mil=millisUntilFinished+1000;
                Toast.makeText(ClickGame.this, String.valueOf(millisUntilFinished/1000), Toast.LENGTH_SHORT).show();
                buttonClick();
            }

            @Override
            public void onFinish() {
                resvalue=Double.valueOf(counter)/Double.valueOf(clickval.getText().toString());
                c.append(" Avg: "+resvalue);
                Toast.makeText(ClickGame.this, "Game Over", Toast.LENGTH_SHORT).show();
                viewGone();

            }
        }.start();
    }

    private void buttonClick() {

        int rand=randomNumber(1,9);
        switch (rand)
        {
            case 1: b1.setVisibility(View.VISIBLE);
            break;
            case 2:b2.setVisibility(View.VISIBLE);
            break;
            case 3:b3.setVisibility(View.VISIBLE);
                break;
            case 4:b4.setVisibility(View.VISIBLE);
                break;
            case 5:b5.setVisibility(View.VISIBLE);
                break;
            case 6:b6.setVisibility(View.VISIBLE);
                break;
            case 7:b7.setVisibility(View.VISIBLE);
                break;
            case 8:b8.setVisibility(View.VISIBLE);
                break;
            case 9:b9.setVisibility(View.VISIBLE);
                break;
        }

    }

    public void clickstart(View view) {
        counter=0;
        loopermethod(clickval.getText().toString());
    }

    @Override
    public void onClick(View v) {
        counter++;

        c.setText(String.valueOf(counter));
        findViewById(v.getId()).setVisibility(View.GONE);
;

    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        countDownTimer.cancel();
        finish();
    }
}

package com.tracking.student.biomedicaltest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

//''
public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void stepcounter(View view) {
        startActivity(new Intent(getApplicationContext(), StepCounter.class));
    }

    public void clickgame(View view) {
        startActivity(new Intent(getApplicationContext(), ClickGame.class));
    }

    public void drawPattern(View view) {
        startActivity(new Intent(getApplicationContext(), DrawPattern.class));
    }
}
package com.tracking.student.biomedicaltest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class DrawPattern extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_pattern);
    }

    public void drawPattern(View view){
        startActivity(new Intent(this, CanvasActivity.class));
    }
}

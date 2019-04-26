package com.tracking.student.biomedicaltest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;


import com.raed.drawingview.DrawingView;

import androidx.appcompat.app.AppCompatActivity;

public class CanvasActivity extends AppCompatActivity {
    private DrawingView drawingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_canvas);


        drawingView = (DrawingView) findViewById(R.id.drawing_view);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.spiral_edit);
        drawingView.setBackgroundImage(bitmap);


    }
}

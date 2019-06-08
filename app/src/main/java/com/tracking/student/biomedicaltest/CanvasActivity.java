package com.tracking.student.biomedicaltest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;


import com.raed.drawingview.DrawingView;

import java.text.DecimalFormat;

import androidx.appcompat.app.AppCompatActivity;

public class CanvasActivity extends AppCompatActivity {
    private DrawingView drawingView;
    private Bitmap bitmap;
    private TextView accuracyTextView;
    private static DecimalFormat df2 = new DecimalFormat("#.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_canvas);


        drawingView = (DrawingView) findViewById(R.id.drawing_view);
        accuracyTextView = findViewById(R.id.accuracyTextView);

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.spiral_back);
        drawingView.setBackgroundImage(bitmap);

    }

    public void checkPattern(View view){
        findDifference(bitmap,drawingView.exportDrawing());
    }


    private void findDifference(Bitmap firstImage, Bitmap secondImage) {
        int width = firstImage.getWidth();
        int height = firstImage.getHeight();


        secondImage = getResizedBitmap(secondImage, width, height);


        Bitmap bmp = secondImage.copy(secondImage.getConfig(), true);

        if (firstImage.getWidth() != secondImage.getWidth()
                || firstImage.getHeight() != secondImage.getHeight()) {
            return;
        }

        float totalPixels = 0;
        float coloredPixels = 0;
        for (int i = 0; i < firstImage.getWidth(); i++) {
            for (int j = 0; j < firstImage.getHeight(); j++) {
                totalPixels++;
                if (firstImage.getPixel(i, j) != secondImage.getPixel(i, j)) {
                    bmp.setPixel(i, j, Color.BLUE);
                    coloredPixels++;
                }
                else
                    bmp.setPixel(i,j,Color.WHITE);
            }
        }

        drawingView.setBackgroundImage(null);
        drawingView.setBackgroundImage(bmp);

        Log.d("Total Pixels",""+totalPixels);
        Log.d("colored Pixels",""+coloredPixels);

        float change = (coloredPixels/totalPixels)*100;
        Log.d("Change",""+change);
        accuracyTextView.setText("Total accuracy : "+(100-change));
    }




    private static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        if (bm != null && !bm.isRecycled()) {
            bm.recycle();
            bm = null;
        }
        return resizedBitmap;
    }
}

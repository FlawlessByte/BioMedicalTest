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

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.spiral_edit);
        drawingView.setBackgroundImage(bitmap);

    }

    public void checkPattern(View view){
        Bitmap bm = drawingView.exportDrawingWithoutBackground();
//        drawingView.clear();
//        drawingView.setBackgroundImage(bm);
        double perc = getDifferencePercent(bitmap, bm);
        Log.d("Percentage", ""+perc);
        accuracyTextView.setText("Accuracy : "+df2.format(perc)+"%");
    }








    private static double getDifferencePercent(Bitmap img1, Bitmap img2) {
        int width = img1.getWidth();
        int height = img1.getHeight();


        img2 = getResizedBitmap(img2, width, height);

        int width2 = img2.getWidth();
        int height2 = img2.getHeight();

        if (width != width2 || height != height2) {
            throw new IllegalArgumentException(String.format("Images must have the same dimensions: (%d,%d) vs. (%d,%d)", width, height, width2, height2));
        }

        long diff = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                diff += pixelDiff(getRGB(x, y, img1), getRGB(x, y, img2));
            }
        }
        long maxDiff = 3L * 255 * width * height;

        return 100.0 * diff / maxDiff;
    }

    private static int pixelDiff(int rgb1, int rgb2) {
        int r1 = (rgb1 >> 16) & 0xff;
        int g1 = (rgb1 >>  8) & 0xff;
        int b1 =  rgb1        & 0xff;
        int r2 = (rgb2 >> 16) & 0xff;
        int g2 = (rgb2 >>  8) & 0xff;
        int b2 =  rgb2        & 0xff;
        return Math.abs(r1 - r2) + Math.abs(g1 - g2) + Math.abs(b1 - b2);
    }


    private static int getRGB(int x, int y, Bitmap img){
        int colour = img.getPixel(x, y);

        int red = Color.red(colour);
        int blue = Color.blue(colour);
        int green = Color.green(colour);
        int alpha = Color.alpha(colour);

        return (alpha << 24) | (red << 16) | (green << 8) | blue;


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

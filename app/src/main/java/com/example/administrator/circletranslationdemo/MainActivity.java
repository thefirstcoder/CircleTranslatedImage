package com.example.administrator.circletranslationdemo;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

public class MainActivity extends AppCompatActivity {


    private CircleImageView mMoveIV;
    private int screenWidth;
    private int screenHeight;
    private Random random;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        random = new Random();
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;
        mMoveIV = (CircleImageView) findViewById(R.id.moveIV);
        mMoveIV.setOnTouchListener(moveListener);
    }

    private View.OnTouchListener moveListener = new View.OnTouchListener() {

        private int startY;
        private int startX;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    startX = (int) event.getRawX();
                    startY = (int) event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    int dx = (int) event.getRawX() - startX;
                    int dy = (int) event.getRawY() - startY;

                    int left = mMoveIV.getLeft() + dx;
                    int right = mMoveIV.getRight() + dx;
                    int top = mMoveIV.getTop() + dy;
                    int bottom = mMoveIV.getBottom() + dy;

                    if (left < 0) {
                        left = 0;
                        right = left + mMoveIV.getWidth();
                    }
                    if (right > screenWidth) {
                        right = screenWidth;
                        left = right - mMoveIV.getWidth();
                    }
                    if (top < 0) {
                        top = 0;
                        bottom = top + mMoveIV.getHeight();
                    }
                    if (bottom > screenHeight) {
                        bottom = screenHeight;
                        top = bottom - mMoveIV.getHeight();
                    }
                    mMoveIV.layout(left, top, right, bottom);
                    startX = (int) event.getRawX();
                    startY = (int) event.getRawY();
                    break;
                case MotionEvent.ACTION_UP:
                    if (mMoveIV.getLeft() == 0 ||
                            mMoveIV.getTop() == 0 || mMoveIV.getRight() == screenWidth
                            || mMoveIV.getBottom() == screenHeight) {
                        int r = random.nextInt(255);
                        int g = random.nextInt(255);
                        int b = random.nextInt(255);
                        ColorDrawable colorDrawable = new ColorDrawable(Color.argb(255, r, g, b));
                        mMoveIV.setImageDrawable(colorDrawable);
                    }
                    break;
            }
            return true;
        }
    };

}

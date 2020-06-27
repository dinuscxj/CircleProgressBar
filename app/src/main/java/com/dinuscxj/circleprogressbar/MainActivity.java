package com.dinuscxj.circleprogressbar;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.dinuscxj.progressbar.CircleProgressBar;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {
    private CircleProgressBar mLineProgressBar;
    private CircleProgressBar mSolidProgressBar;
    private CircleProgressBar mCustomProgressBar1;
    private CircleProgressBar mCustomProgressBar2;
    private CircleProgressBar mCustomProgressBar3;
    private CircleProgressBar mCustomProgressBar4;
    private CircleProgressBar mCustomProgressBar5;
    private CircleProgressBar mCustomProgressBar6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLineProgressBar = findViewById(R.id.line_progress);
        mSolidProgressBar = findViewById(R.id.solid_progress);
        mCustomProgressBar1 = findViewById(R.id.custom_progress1);
        mCustomProgressBar2 = findViewById(R.id.custom_progress2);
        mCustomProgressBar3 = findViewById(R.id.custom_progress3);
        mCustomProgressBar4 = findViewById(R.id.custom_progress4);
        mCustomProgressBar5 = findViewById(R.id.custom_progress5);
        mCustomProgressBar6 = findViewById(R.id.custom_progress6);

        mCustomProgressBar5.setProgressFormatter(new CircleProgressBar.ProgressFormatter() {
            @Override
            public CharSequence format(int progress, int max) {
                return progress + "s";
            }
        });
        mCustomProgressBar6.setProgressFormatter(null);

        mLineProgressBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLineProgressBar.setStyle(CircleProgressBar.SOLID_LINE);
                mLineProgressBar.setProgressTextColor(Color.RED);
                mLineProgressBar.setProgressStartColor(Color.GREEN);
                mLineProgressBar.setProgressEndColor(Color.BLUE);
                mLineProgressBar.setProgressBackgroundColor(Color.YELLOW);
                mLineProgressBar.setDrawBackgroundOutsideProgress(true);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        simulateProgress();
    }

    private void simulateProgress() {
        ValueAnimator animator = ValueAnimator.ofInt(0, 100);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int progress = (int) animation.getAnimatedValue();
                mLineProgressBar.setProgress(progress);
                mSolidProgressBar.setProgress(progress);
                mCustomProgressBar1.setProgress(progress);
                mCustomProgressBar2.setProgress(progress);
                mCustomProgressBar3.setProgress(progress);
                mCustomProgressBar4.setProgress(progress);
                mCustomProgressBar5.setProgress(progress);
                mCustomProgressBar6.setProgress(progress);
            }
        });
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setDuration(4000);
        animator.start();
    }

}

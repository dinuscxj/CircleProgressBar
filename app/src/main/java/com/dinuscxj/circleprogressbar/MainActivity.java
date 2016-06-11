package com.dinuscxj.circleprogressbar;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.dinuscxj.progressbar.CircleProgressBar;


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

        mLineProgressBar = (CircleProgressBar) findViewById(R.id.line_progress);
        mSolidProgressBar = (CircleProgressBar) findViewById(R.id.solid_progress);
        mCustomProgressBar1 = (CircleProgressBar) findViewById(R.id.custom_progress1);
        mCustomProgressBar2 = (CircleProgressBar) findViewById(R.id.custom_progress2);
        mCustomProgressBar3 = (CircleProgressBar) findViewById(R.id.custom_progress3);
        mCustomProgressBar4 = (CircleProgressBar) findViewById(R.id.custom_progress4);
        mCustomProgressBar5 = (CircleProgressBar) findViewById(R.id.custom_progress5);
        mCustomProgressBar6 = (CircleProgressBar) findViewById(R.id.custom_progress6);
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

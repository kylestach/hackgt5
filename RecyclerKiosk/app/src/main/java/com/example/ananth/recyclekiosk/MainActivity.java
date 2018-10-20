package com.example.ananth.recyclekiosk;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ProgressBar xpProgressBar;
    private ValueAnimator animator;
    private boolean rotatedFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);
        TextView name = findViewById(R.id.nameTextView);
        name.setText(DataManager.user.getName());
        getSupportActionBar().hide();
        xpProgressBar = findViewById(R.id.xpProgressBar);
        //xpProgressBar.setScaleY(2.0f);
        animator = ValueAnimator.ofInt(0, 50);
        animator.setDuration(1000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                xpProgressBar.setProgress((int) animation.getAnimatedValue());
            }
        });
        //xpProgressBar.setProgress(50);
        xpProgressBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.buzzYellow), PorterDuff.Mode.SRC_IN);
        xpProgressBar.setProgress(0);
        animator.setStartDelay(1000);
        animator.start();
        ViewPager pager = findViewById(R.id.mainViewPager);
        ScreenSlidePagerAdapter adapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        ImageView cameraImageView = findViewById(R.id.cameraImage);
        cameraImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CameraActivity.class));
            }
        });
        rotatedFab = false;
        final FloatingActionButton menuFAB = findViewById(R.id.fabMain);
        menuFAB.setSize(FloatingActionButton.SIZE_NORMAL);
        menuFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rotatedFab){
                    final OvershootInterpolator interpolator = new OvershootInterpolator();
                    ViewCompat.animate(menuFAB).
                            rotation(0f).
                            withLayer().
                            setDuration(300).
                            setInterpolator(interpolator).
                            start();
                    rotatedFab = false;
                }
                else{
                    final OvershootInterpolator interpolator = new OvershootInterpolator();
                    ViewCompat.animate(menuFAB).
                            rotation(135f).
                            withLayer().
                            setDuration(300).
                            setInterpolator(interpolator).
                            start();
                    rotatedFab = true;
                }
            }
        });
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        List<ScoreItem> tempData;

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
            tempData = new ArrayList<>();
            for (int i = 0; i < 20; i++) {
                tempData.add(new ScoreItem("Score " + i, i));
            }
        }

        @Override
        public Fragment getItem(int position) {
            PersonalScoreFragment fragment = new PersonalScoreFragment();
            fragment.setDataset(tempData);
            if (position == 0) {
                fragment.setTitle("My Items");
            }
            if (position == 1) {
                fragment.setTitle("Leaderboard");
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}

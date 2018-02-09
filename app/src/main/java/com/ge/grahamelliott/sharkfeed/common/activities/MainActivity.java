package com.ge.grahamelliott.sharkfeed.common.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.ge.grahamelliott.sharkfeed.R;
import com.ge.grahamelliott.sharkfeed.photolist.PhotoListFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getFragments().isEmpty()) {
            // only create new fragment if no fragments present (they will be retained across config changes)
            fragmentManager.beginTransaction().replace(R.id.container, PhotoListFragment.newInstance()).commit();
        }

        //TODO: init views, restore saved state
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            // remove all retained fragments
            FragmentManager fragmentManager = getSupportFragmentManager();
            for (Fragment fragment : fragmentManager.getFragments()) {
                fragmentManager.beginTransaction().remove(fragment).commit();
            }
        }
    }
}

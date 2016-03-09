package com.test.swipelistitemapp.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.test.swipelistitemapp.R;
import com.test.swipelistitemapp.ui.fragment.PostListFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment, PostListFragment.newInstance(1))
                    .addToBackStack(PostListFragment.class.getCanonicalName())
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (!isFinishing() && getSupportFragmentManager().getBackStackEntryCount() == 0) {
            finish();
        }
    }
}

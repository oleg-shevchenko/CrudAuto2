package com.olegsh.crudauto.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.olegsh.crudauto.R;
import com.olegsh.crudauto.adapter.DriverAdapter;
import com.olegsh.crudauto.utils.Constants;

public class MainActivity extends AppCompatActivity implements DriverAdapter.OnDriverClickListener {

    private final static String LOG_TAG = "MainActivity";

    private static final int DRIVER_LIST_FRAGMENT_ID = 1;
    private static final int DRIVER_INFO_FRAGMENT_ID = 2;
    private int currentFragmentId = 0;
    private long currentDriverInfoId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        if(savedInstanceState != null) {
            currentFragmentId = savedInstanceState.getInt(Constants.EXTRA_INT_CURRENT_FRAGMENT);
            currentDriverInfoId = savedInstanceState.getLong(Constants.EXTRA_LONG_CURRENT_DRIVER);
        }
        initFragments(currentFragmentId);
    }

    private void initFragments(int currentFragmentId) {
        Log.d(LOG_TAG, "initFragments()");
        if(currentFragmentId == DRIVER_INFO_FRAGMENT_ID) {
            DriverInfoFragment driverInfoFragment = new DriverInfoFragment();
            Bundle args = new Bundle(1);
            args.putLong(Constants.EXTRA_LONG_DRIVER_ID, currentDriverInfoId);
            driverInfoFragment.setArguments(args);
            replaceFragment(driverInfoFragment, true);
        } else {
            replaceFragment(new DriversListFragment(), false);
        }
    }

    private void replaceFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_frame, fragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        if(addToBackStack) transaction.addToBackStack("abc");
        transaction.commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Constants.EXTRA_INT_CURRENT_FRAGMENT, currentFragmentId);
        outState.putLong(Constants.EXTRA_LONG_CURRENT_DRIVER, currentDriverInfoId);

    }

    @Override
    public void onDriverClick(long id) {
        currentDriverInfoId = id;
        currentFragmentId = DRIVER_INFO_FRAGMENT_ID;
        initFragments(DRIVER_INFO_FRAGMENT_ID);
    }
}

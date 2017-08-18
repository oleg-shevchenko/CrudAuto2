package com.olegsh.crudauto.view;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.olegsh.crudauto.App;
import com.olegsh.crudauto.R;
import com.olegsh.crudauto.adapter.DriverAdapter;
import com.olegsh.crudauto.loader.DriverLoader;
import com.olegsh.crudauto.model.DaoSession;
import com.olegsh.crudauto.model.Driver;
import com.olegsh.crudauto.view.dialog.EditDriverDialogFragment;

import java.util.List;

public class DriversListFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Driver>>, EditDriverDialogFragment.OnDriverChangeListener {

    private final static String LOG_TAG = "DriversListFragment";
    private final int DRIVERS_LOADER_ID = 1;
    private DaoSession daoSession;
    private RecyclerView recyclerView;
    private DriverAdapter adapter;

    public DriversListFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        daoSession = ((App) getActivity().getApplication()).getDaoSession();
        adapter = new DriverAdapter(null, getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        recyclerView = new RecyclerView(getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        return recyclerView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //TODO: непонятно, почему id один и тот же, а onCreateLoader() всегда вызывается при поворотах
        getLoaderManager().initLoader(DRIVERS_LOADER_ID, null, this);

        //Set FloatingActionButton click listener
        getActivity().findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Show EditDriverDialogFragment to add new driver
                EditDriverDialogFragment dialog = new EditDriverDialogFragment();
                dialog.setDriverChangeListener(DriversListFragment.this);
                dialog.show(getFragmentManager(), "dlg1");
            }
        });
        getActivity().setTitle(getString(R.string.title_fragment_driverlist));
    }

    @Override
    public void onDriverChange(Driver driver) {
        //Add new driver in DB
        daoSession.getDriverDao().insert(driver);
        //Reload list
        getLoaderManager().getLoader(DRIVERS_LOADER_ID).onContentChanged();
    }

    @Override
    public Loader<List<Driver>> onCreateLoader(int id, Bundle args) {
        Log.d(LOG_TAG, "onCreateLoader()");
        return new DriverLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<Driver>> loader, List<Driver> data) {
        Log.d(LOG_TAG, "onLoadFinished()");
        adapter.setData(data);
    }

    @Override
    public void onLoaderReset(Loader<List<Driver>> loader) {
        Log.d(LOG_TAG, "onLoaderReset()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy()");
        daoSession = null;
        adapter.setData(null);
    }


}

package com.olegsh.crudauto.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.olegsh.crudauto.App;
import com.olegsh.crudauto.model.DaoSession;
import com.olegsh.crudauto.model.Driver;
import com.olegsh.crudauto.model.DriverDao;
import com.olegsh.crudauto.view.dialog.ProgressDialogSimpleCircle;

import org.greenrobot.greendao.query.Query;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Oleg on 15.08.2017.
 */

public class DriverLoader extends AsyncTaskLoader<List<Driver>> {

    private final static String LOG_TAG = "DriverLoader";
    private DriverDao driverDao;

    public DriverLoader(Context context) {
        super(context);
        this.driverDao = ((App) ((AppCompatActivity) context).getApplication()).getDaoSession().getDriverDao();
    }

    @Override
    protected void onStartLoading() {
        Log.d(LOG_TAG, "onStartLoading()");
        forceLoad();
    }

    @Override
    public List<Driver> loadInBackground() {
        Log.d(LOG_TAG, "loadInBackground()");
//        try {
//            TimeUnit.SECONDS.sleep(1L);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        Query<Driver> query = driverDao.queryBuilder().build();
        return query.list();
    }

    @Override
    public void deliverResult(List<Driver> data) {
        super.deliverResult(data);
    }

    @Override
    protected void onReset() {
        super.onReset();
        driverDao = null;
    }
}

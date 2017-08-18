package com.olegsh.crudauto;

import android.app.Application;

import com.olegsh.crudauto.model.DaoMaster;
import com.olegsh.crudauto.model.DaoSession;

import org.greenrobot.greendao.database.Database;

/**
 * Created by Oleg on 14.08.2017.
 */

public class App extends Application {
    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "crudauto-db");
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}

package com.olegsh.crudauto.view;


import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.olegsh.crudauto.App;
import com.olegsh.crudauto.R;
import com.olegsh.crudauto.adapter.VehiclesAdapter;
import com.olegsh.crudauto.model.DaoSession;
import com.olegsh.crudauto.model.Driver;
import com.olegsh.crudauto.model.DriverDao;
import com.olegsh.crudauto.model.Vehicle;
import com.olegsh.crudauto.model.VehicleDao;
import com.olegsh.crudauto.utils.Constants;
import com.olegsh.crudauto.view.dialog.EditDriverDialogFragment;
import com.olegsh.crudauto.view.dialog.EditVehicleDialogFragment;
import com.olegsh.crudauto.view.dialog.ProgressDialogSimpleCircle;

import org.greenrobot.greendao.query.DeleteQuery;
import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.WhereCondition;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

public class DriverInfoFragment extends Fragment implements EditDriverDialogFragment.OnDriverChangeListener {

    private final String LOG_TAG = "DriverInfoFragment";
    private TextView tvName, tvCategory;
    private RecyclerView rvVehicles;
    private DaoSession daoSession;
    private DriverDao driverDao;
    private VehicleDao vehicleDao;
    private Driver driver;
    private VehiclesAdapter adapter;
    private static AsyncRemoveDriver asyncRemoveDriver;

    public DriverInfoFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        daoSession = ((App) getActivity().getApplication()).getDaoSession();
        driverDao = daoSession.getDriverDao();
        vehicleDao = daoSession.getVehicleDao();
        driver = driverDao.load(getArguments().getLong(Constants.EXTRA_LONG_DRIVER_ID));
        if(asyncRemoveDriver != null) asyncRemoveDriver.setTargetFragment(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_driver_info, container, false);
        tvName = (TextView) v.findViewById(R.id.textName);
        tvCategory = (TextView) v.findViewById(R.id.textCategory);
        rvVehicles = (RecyclerView) v.findViewById(R.id.listVehicles);
        rvVehicles.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new VehiclesAdapter(null, getActivity());
        rvVehicles.setAdapter(adapter);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setTextViews();
        //Expand appbar if collapsed
        ((AppBarLayout) getActivity().findViewById(R.id.main_appbar)).setExpanded(true, true);
        getActivity().setTitle(driver.getName());

        adapter.setData(vehicleDao._queryDriver_Vehicles(driver.getId()));

        //Show add vehicle dialog on fab click
        getActivity().findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Create add vehicle dialog fragment
                EditVehicleDialogFragment dialog = new EditVehicleDialogFragment();
                Bundle args = new Bundle();
                args.putLong(Constants.EXTRA_LONG_DRIVER_ID, driver.getId());
                //If put only driver id to arguments, than dialog will start in ADD mode
                dialog.setArguments(args);
                //Implement dialog callback
                dialog.setVehicleChangeListener(new EditVehicleDialogFragment.OnVehicleEditListener() {
                    @Override
                    public void onVehicleEdit(Vehicle vehicle) {
                        vehicleDao.insert(vehicle);
                        adapter.setData(vehicleDao._queryDriver_Vehicles(driver.getId()));
                    }
                });
                dialog.show(getFragmentManager(), "dlg_add_vehicle");
            }
        });
    }

    //
    private void setTextViews() {
        tvName.setText(driver.getName());
        tvCategory.setText(driver.getCategory());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_driver_info, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //Edit driver action
            case R.id.action_edit_driver:
                EditDriverDialogFragment dialog = new EditDriverDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putString(Constants.EXTRA_STR_DRIVER_NAME, driver.getName());
                bundle.putString(Constants.EXTRA_STR_DRIVER_CATEGORY, driver.getCategory());
                bundle.putLong(Constants.EXTRA_LONG_DRIVER_ID, driver.getId());
                dialog.setArguments(bundle);
                dialog.setDriverChangeListener(this);
                dialog.show(getFragmentManager(), "dlgEditDriver");
                break;
            //Remove driver action
            case R.id.action_remove_driver:
                //Show alert dialog before remove
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(getString(R.string.dialog_delete_driver_title))
                        .setMessage(getString(R.string.dialog_delete_driver_message, driver.getName(), driver.getCategory()))
                        .setPositiveButton(getString(R.string.dialog_positive_button), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                asyncRemoveDriver = new AsyncRemoveDriver(DriverInfoFragment.this);
                                asyncRemoveDriver.execute(driver);
                            }
                        })
                        .setNegativeButton(getString(R.string.dialog_negative_button), null)
                        .show();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onDriverChange(Driver driver) {
        if(driver != null && driver.getId() != null) {
            driverDao.update(driver);
            this.driver = driver;
            setTextViews();
        }
    }

    //AsyncTask для удаления водителя и всех его машин из БД
    private static class AsyncRemoveDriver extends AsyncTask<Driver, Void, Driver> {
        //Мягкая ссылка на фрагмен для доступа к полям и методам, поскольку они недоступны из-за статичности класса
        private WeakReference<DriverInfoFragment> wrFragment;

        private VehicleDao vehicleDao;
        private DriverDao driverDao;

        ///В конструктор передаем ссылку на фрагмент и с ее помощью создаем мягкую ссылку
        private AsyncRemoveDriver(DriverInfoFragment fragment) {
            wrFragment = new WeakReference<DriverInfoFragment>(fragment);
            vehicleDao = fragment.vehicleDao;
            driverDao = fragment.driverDao;
        }

        //Метод для установки текущего фрагмента (напр. при перерисовке)
        private void setTargetFragment(DriverInfoFragment fragment) {
            wrFragment.clear();
            wrFragment = new WeakReference<DriverInfoFragment>(fragment);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressDialogSimpleCircle.getInstance().show(wrFragment.get().getFragmentManager(), "dlg_progress");
        }

        @Override
        protected Driver doInBackground(Driver... drivers) {
            //No cascade in greendao, so delete vehicles manualy
            DeleteQuery<Vehicle> query = vehicleDao.queryBuilder().where(VehicleDao.Properties.DriverId.eq(drivers[0].getId())).buildDelete();
            query.executeDeleteWithoutDetachingEntities();
            //delete driver
            driverDao.delete(drivers[0]);
            return drivers[0];
        }

        @Override
        protected void onPostExecute(Driver driver) {
            super.onPostExecute(driver);
            DriverInfoFragment fragment = wrFragment.get();
            if(fragment != null && fragment.getView() != null) {
                Snackbar.make(fragment.getView(), fragment.getString(R.string.snack_delete_driver,
                        driver.getName(), driver.getCategory()),
                        Snackbar.LENGTH_SHORT).show();
                //Go to previous fragment
                fragment.getFragmentManager().popBackStack();
            }
            ProgressDialogSimpleCircle.getInstance().dismiss();
        }
    }

}

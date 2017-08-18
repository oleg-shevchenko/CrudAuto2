package com.olegsh.crudauto.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.olegsh.crudauto.App;
import com.olegsh.crudauto.R;
import com.olegsh.crudauto.model.Vehicle;
import com.olegsh.crudauto.model.VehicleDao;
import com.olegsh.crudauto.utils.Constants;
import com.olegsh.crudauto.view.dialog.EditVehicleDialogFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oleg on 15.08.2017.
 */

public class VehiclesAdapter extends RecyclerView.Adapter<VehiclesAdapter.VehicleHolder> {

    private List<Vehicle> data;
    private LayoutInflater inflater;
    private VehicleDao vehicleDao;
    private Context context;

    public VehiclesAdapter(List<Vehicle> vehicles, Context context) {
        this.data = vehicles == null ? new ArrayList<Vehicle>(0) : vehicles;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        vehicleDao = ((App) ((AppCompatActivity) context).getApplication()).getDaoSession().getVehicleDao();
        this.context = context;
    }

    @Override
    public VehicleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_vehicle, parent, false);
        return new VehicleHolder(view);
    }

    @Override
    public void onBindViewHolder(VehicleHolder holder, int position) {
        Vehicle vehicle = data.get(position);
        holder.bindVehicle(vehicle);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<Vehicle> vehicleList) {
        data = vehicleList;
        notifyDataSetChanged();
    }

    class VehicleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvModel, tvNumber;

        public VehicleHolder(View itemView) {
            super(itemView);
            tvModel = itemView.findViewById(R.id.textModel);
            tvNumber = itemView.findViewById(R.id.textNumber);
            itemView.findViewById(R.id.imgEdit).setOnClickListener(this);
            itemView.findViewById(R.id.imgDelete).setOnClickListener(this);
        }

        @Override
        public void onClick(final View v) {
            switch (v.getId()) {
                //Click on edit icon
                case R.id.imgEdit: {
                    //Create add vehicle dialog fragment
                    Vehicle vehicle = data.get(getLayoutPosition());
                    EditVehicleDialogFragment dialog = new EditVehicleDialogFragment();
                    Bundle args = new Bundle();
                    args.putLong(Constants.EXTRA_LONG_DRIVER_ID, vehicle.getDriverId());
                    args.putLong(Constants.EXTRA_LONG_VEHICLE_ID, vehicle.getId());
                    args.putString(Constants.EXTRA_STR_VEHICLE_MODEL, vehicle.getName());
                    args.putString(Constants.EXTRA_STR_VEHICLE_NUMBER, vehicle.getNumber());
                    //If put all arguments, than dialog will start in EDIT mode
                    dialog.setArguments(args);
                    //Implement dialog callback
                    dialog.setVehicleChangeListener(new EditVehicleDialogFragment.OnVehicleEditListener() {
                        @Override
                        public void onVehicleEdit(Vehicle vehicle) {
                            //Update entry in DB and adapter list
                            vehicleDao.update(vehicle);
                            data.set(getLayoutPosition(), vehicle);
                            //rebuild view
                            notifyDataSetChanged();
                        }
                    });
                    dialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "dlg_edit_vehicle");
                    break;
                }
                //Click on delete icon
                case R.id.imgDelete: {
                    //Show alert dialog first
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle(context.getString(R.string.dialog_delete_vehicle_title))
                            .setMessage(context.getString(R.string.dialog_delete_vehicle_message,
                                    tvModel.getText().toString(),
                                    tvNumber.getText().toString()))
                            .setPositiveButton(context.getString(R.string.dialog_positive_button), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Vehicle vehicle = data.get(getLayoutPosition());
                                    vehicleDao.delete(vehicle);
                                    data.remove(getLayoutPosition());
                                    notifyDataSetChanged();
                                    Snackbar.make(v, context.getString(R.string.snack_delete_vehicle,
                                            vehicle.getName(), vehicle.getNumber()), Snackbar.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton(context.getString(R.string.dialog_negative_button), null)
                            .show();
                    break;
                }
            }
        }

        void bindVehicle(Vehicle vehicle) {
            tvModel.setText(vehicle.getName());
            tvNumber.setText(vehicle.getNumber());
        }
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        data = null;
        inflater = null;
        vehicleDao = null;
        context = null;
    }
}

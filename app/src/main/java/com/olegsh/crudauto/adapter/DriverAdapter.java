package com.olegsh.crudauto.adapter;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.olegsh.crudauto.R;
import com.olegsh.crudauto.model.Driver;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oleg on 14.08.2017.
 */

public class DriverAdapter extends RecyclerView.Adapter<DriverAdapter.DriverHolder> {

    private List<Driver> driverList;
    private LayoutInflater inflater;
    private OnDriverClickListener listener;

    public DriverAdapter(List<Driver> driverList, Context context) {
        this.driverList = driverList == null ? new ArrayList<Driver>(0) : driverList;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        listener = (OnDriverClickListener) context;
    }

    @Override
    public DriverHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_driver, parent, false);
        return new DriverHolder(view);
    }

    @Override
    public void onBindViewHolder(DriverHolder holder, int position) {
        Driver driver = driverList.get(position);
        holder.bindDriver(driver);
    }

    public void setData(List<Driver> driverList) {
        this.driverList = driverList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return driverList.size();
    }

    class DriverHolder extends RecyclerView.ViewHolder {
        private TextView tvDriverName;
        private TextView tvDriverCategory;
        private long id;

        public DriverHolder(final View itemView) {
            super(itemView);
            tvDriverName = (TextView) itemView.findViewById(R.id.textName);
            tvDriverCategory = (TextView) itemView.findViewById(R.id.textCategory);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onDriverClick(id);
                }
            });
        }

        public void bindDriver(Driver driver) {
            tvDriverName.setText(driver.getName());
            tvDriverCategory.setText(driver.getCategory());
            id = driver.getId();
        }
    }

    public interface OnDriverClickListener {
        public void onDriverClick(long id);
    }
}

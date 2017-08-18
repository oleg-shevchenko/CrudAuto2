package com.olegsh.crudauto.view.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.olegsh.crudauto.R;
import com.olegsh.crudauto.model.Vehicle;
import com.olegsh.crudauto.utils.Constants;

/**
 * Created by Oleg on 18.08.2017.
 */

public class EditVehicleDialogFragment extends DialogFragment {

    private TextInputLayout vehicleModelWrapper, vehicleNumberWrapper;
    private OnVehicleEditListener listener;

    private Long vehicleId, driverId;

    //Установить слушатель добавления нового водителя
    public void setVehicleChangeListener(OnVehicleEditListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        //Установим свой view для диалога
        View view = inflater.inflate(R.layout.dialog_add_vehicle, null);
        vehicleModelWrapper = view.findViewById(R.id.etVehicleModel);
        vehicleNumberWrapper = view.findViewById(R.id.etVehicleNumber);

        //Если добавляли аргументы, значит это редактирование. Сохраняем id, и заполняем поля EditText.
        Bundle args = getArguments();
        if(args != null) {
            if(args.containsKey(Constants.EXTRA_LONG_VEHICLE_ID)) {
                vehicleId = args.getLong(Constants.EXTRA_LONG_VEHICLE_ID);
                vehicleModelWrapper.getEditText().setText(args.getString(Constants.EXTRA_STR_VEHICLE_MODEL));
                vehicleNumberWrapper.getEditText().setText(args.getString(Constants.EXTRA_STR_VEHICLE_NUMBER));
                builder.setTitle(getString(R.string.dialog_edit_vehicle_title));
            } else {
                builder.setTitle(getString(R.string.dialog_add_vehicle_title));
            }
            driverId = args.getLong(Constants.EXTRA_LONG_DRIVER_ID);
        } else {
            throw new IllegalArgumentException("EditVehicleDialog creation error. No required arguments.");
        }
        builder.setView(view);
        //Определяем кнопки
        builder.setPositiveButton(getString(R.string.dialog_positive_button), null);
        builder.setNegativeButton(getString(R.string.dialog_negative_button), null);
        return builder.create();
    }

    //Проверка на заполнение требуемых полей
    //Если какое-либо поле не заполнено, то устанавливаем ошибку для соответствующей обертки
    //Возврат true, если все нормально
    private boolean checkFields(String model, String number) {
        boolean error = false;
        if(TextUtils.isEmpty(number)) {
            vehicleNumberWrapper.setError(getString(R.string.dialog_add_vehicle_error_number));
            vehicleNumberWrapper.getEditText().requestFocus();
            error = true;
        } else {
            vehicleNumberWrapper.setErrorEnabled(false);
        }
        if(TextUtils.isEmpty(model)) {
            vehicleModelWrapper.setError(getString(R.string.dialog_add_vehicle_error_model));
            vehicleModelWrapper.getEditText().requestFocus();
            error = true;
        } else {
            vehicleModelWrapper.setErrorEnabled(false);
        }
        return !error;
    }

    @Override
    public void onResume() {
        super.onResume();
        //Установим свой слушатель на BUTTON_POSITIVE, чтобы диалог сам не закрывался
        final AlertDialog dialog = (AlertDialog) getDialog();
        if(dialog != null) {
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String model = vehicleModelWrapper.getEditText().getText().toString();
                    String number = vehicleNumberWrapper.getEditText().getText().toString();
                    if(checkFields(model, number)) {
                        //Если это добавление нового водителя, тогда id = null
                        listener.onVehicleEdit(new Vehicle(vehicleId, driverId, number, model));
                        dismiss();
                    }
                }
            });
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    //Интерфейс обратного вызова для добавления водителя
    public interface OnVehicleEditListener {
        void onVehicleEdit(Vehicle vehicle);
    }
}

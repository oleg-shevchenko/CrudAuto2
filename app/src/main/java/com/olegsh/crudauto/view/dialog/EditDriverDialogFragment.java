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
import com.olegsh.crudauto.model.Driver;
import com.olegsh.crudauto.utils.Constants;

/**
 * Created by Oleg on 15.08.2017.
 */

public class EditDriverDialogFragment extends DialogFragment {
    private TextInputLayout driverNameWrapper, driverCategoryWrapper;
    private OnDriverChangeListener listener;

    Long id;

    //Установить слушатель добавления нового водителя
    public void setDriverChangeListener(OnDriverChangeListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        //Установим свой view для диалога
        View view = inflater.inflate(R.layout.dialog_add_driver, null);
        driverNameWrapper = view.findViewById(R.id.etDialogName);
        driverCategoryWrapper = view.findViewById(R.id.etDialogCategory);

        //Если добавляли аргументы, значит это редактирование. Сохраняем id, и заполняем поля EditText.
        if(getArguments() != null) {
            builder.setTitle(getString(R.string.dialog_edit_driver_title));
            id = getArguments().getLong(Constants.EXTRA_LONG_DRIVER_ID);
            driverNameWrapper.getEditText().setText(getArguments().getString(Constants.EXTRA_STR_DRIVER_NAME));
            driverCategoryWrapper.getEditText().setText(getArguments().getString(Constants.EXTRA_STR_DRIVER_CATEGORY));
        } else {
            builder.setTitle(getString(R.string.dialog_add_driver_title));
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
    private boolean checkFields(String name, String category) {
        boolean error = false;
        if(TextUtils.isEmpty(category)) {
            driverCategoryWrapper.setError(getString(R.string.dialog_add_driver_error_category));
            driverCategoryWrapper.getEditText().requestFocus();
            error = true;
        } else {
            driverCategoryWrapper.setErrorEnabled(false);
        }
        if(TextUtils.isEmpty(name)) {
            driverNameWrapper.setError(getString(R.string.dialog_add_driver_error_name));
            driverNameWrapper.getEditText().requestFocus();
            error = true;
        } else {
            driverNameWrapper.setErrorEnabled(false);
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
                    String name = driverNameWrapper.getEditText().getText().toString();
                    String category = driverCategoryWrapper.getEditText().getText().toString();
                    if(checkFields(name, category)) {
                        //Если это добавление нового водителя, тогда id = null
                        listener.onDriverChange(new Driver(id, name, category));
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
    public interface OnDriverChangeListener {
        void onDriverChange(Driver driver);
    }
}

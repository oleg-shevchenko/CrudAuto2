package com.olegsh.crudauto.view.dialog;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

/**
 * Created by Oleg on 06.07.2017.
 */

//Диалог, который отображает прогрессбар с прозрачным фоном
public class ProgressDialogSimpleCircle extends DialogFragment {

    private static ProgressDialogSimpleCircle instance;

    //Usage: ProgressDialogSimpleCircle.getInstance().show(getSupportFragmentManager(), "spd");
    //ProgressDialogSimpleCircle.getInstance().dismiss();
    public static ProgressDialogSimpleCircle getInstance() {
        if(instance == null) {
            instance = new ProgressDialogSimpleCircle();
            //Установка возможности отмены кнопкой назад
            instance.setCancelable(false);
        }
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Устанавливаем прозрачный фон
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        //В качестве View передаем простой прогрессбар
        return new ProgressBar(getContext(), null, android.R.attr.progressBarStyleLarge);
    }
}

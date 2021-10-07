package com.example.proyectouao;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

public class LoadinDialog {
    private Activity activity;
    private AlertDialog dialog;

    LoadinDialog(Activity myActivity){
        activity = myActivity;
    }

    void startLoading(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.progress_bar,null));
        builder.setCancelable(false);

        dialog = builder.create();
        dialog.show();

    }
    void dismissDialog(){
        dialog.dismiss();
    }
}

package com.example.postcutter;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.Button;

public class LoadingDialog {

        private final Activity activity;
        private AlertDialog dialog;

    public LoadingDialog(Activity activity){
        this.activity = activity;
    }

    public void startLoadingDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custom_dialog, null));
        builder.setCancelable(false);

        dialog = builder.create();
        dialog.show();

        Button cancelButton = dialog.findViewById(R.id.dialog_button);
        cancelButton.setOnClickListener(e -> cancelProcessing());
    }

    public void stopLoadingDialog(){
        dialog.dismiss();
    }

    private void cancelProcessing(){
        stopLoadingDialog();
        activity.finish();
    }
}

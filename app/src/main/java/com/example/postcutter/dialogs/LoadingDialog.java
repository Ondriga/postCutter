package com.example.postcutter.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.Button;

import com.example.postcutter.CutterActivity;
import com.example.postcutter.R;

public class LoadingDialog {

        private final Activity activity;
        private AlertDialog dialog;

    public LoadingDialog(Activity activity){
        this.activity = activity;
    }

    public void startLoadingDialog(CutterActivity.ImageProcess thread){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custom_dialog, null));
        builder.setCancelable(false);

        dialog = builder.create();
        dialog.show();

        Button cancelButton = dialog.findViewById(R.id.dialog_button);
        cancelButton.setOnClickListener(e -> cancelProcessing(thread));
    }

    public void stopLoadingDialog(){
        dialog.dismiss();
    }

    private void cancelProcessing(CutterActivity.ImageProcess thread){
        thread.stopThread();
        dialog.dismiss();
    }
}

package com.example.postcutter.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.widget.Button;

import com.example.postcutter.ImageDetailActivity;
import com.example.postcutter.R;
import com.example.postcutter.functions.ImageAction;


public class CutterSaveDialog {
    private final AlertDialog dialog;
    private final Activity activity;
    private final Bitmap img;
    private final String imgPath;

    public CutterSaveDialog(Activity activity, Bitmap img, String imgPath) {
        this.activity = activity;
        this.img = img;
        this.imgPath = imgPath;

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custom_cutter_save_dialog, null));

        builder.setTitle(R.string.cutter_save_dialog_title);

        dialog = builder.create();
        dialog.show();

        Button buttonSave = dialog.findViewById(R.id.cutterSaveDialog_button_save);
        Button buttonSaveAsNew = dialog.findViewById(R.id.cutterSaveDialog_button_saveAsNew);
        Button buttonCancel = dialog.findViewById(R.id.cutterSaveDialog_button_cancel);

        buttonSave.setOnClickListener(e -> save());
        buttonSaveAsNew.setOnClickListener(e -> saveAsNew());
        buttonCancel.setOnClickListener(e -> cancel());
    }

    private void save() {
        String path = ImageAction.save(activity, img, imgPath);
        closeActivity(path);
    }

    private void saveAsNew() {
        String path = ImageAction.saveAsNew(activity, img);
        closeActivity(path);
    }

    private void cancel() {
        dialog.dismiss();
    }

    private void closeActivity(String path) {
        Intent intent = new Intent();
        intent.putExtra(ImageDetailActivity.IMG_RETURN_PATH, path);
        activity.setResult(ImageDetailActivity.RETURN_REQUEST_CODE, intent);
        dialog.dismiss();

        activity.finish();
    }
}

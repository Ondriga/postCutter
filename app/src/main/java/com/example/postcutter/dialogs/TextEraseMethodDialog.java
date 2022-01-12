package com.example.postcutter.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.RadioGroup;

import com.example.postcutter.ImageDetailActivity;
import com.example.postcutter.R;
import com.example.postcutter.functions.ImageAction;
import com.example.postcutter.functions.TextEraseMethod;

import org.opencv.core.Mat;

import inpainting.Inpainter;
import postCutter.geometricShapes.rectangle.MyRectangle;

public class TextEraseMethodDialog {
    private final AlertDialog dialog;
    private final Activity activity;
    private final TextEraseMethod textEraseMethodTelea;
    private final TextEraseMethod textEraseMethodNS;

    public TextEraseMethodDialog(Activity activity, Mat img, MyRectangle rectangle, String imgPath) {
        this.activity = activity;

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custom_text_erase_dialog, null));

        builder.setTitle(R.string.text_erase_dialog_title);

        dialog = builder.create();
        dialog.show();

        RadioGroup radioGroup = dialog.findViewById(R.id.textEraseDialog_radioGroup);
        textEraseMethodTelea = new TextEraseMethod(
                Inpainter.inpaintingTelea(img, rectangle),
                activity,
                dialog.findViewById(R.id.textEraseDialog_radioButton_Telea),
                rectangle
        );
        textEraseMethodNS = new TextEraseMethod(
                Inpainter.inpaintingNS(img, rectangle),
                activity,
                dialog.findViewById(R.id.textEraseDialog_radioButton_NS),
                rectangle
        );

        Button buttonSave = dialog.findViewById(R.id.textEraseDialog_button_save);
        Button buttonSaveAsNew = dialog.findViewById(R.id.textEraseDialog_button_saveAsNew);
        Button buttonCancel = dialog.findViewById(R.id.textEraseDialog_button_cancel);

        buttonSave.setOnClickListener(e -> save(radioGroup.getCheckedRadioButtonId(), imgPath));
        buttonSaveAsNew.setOnClickListener(e -> saveAsNew(radioGroup.getCheckedRadioButtonId()));
        buttonCancel.setOnClickListener(e -> cancel());
    }

    private Bitmap getImageFromMethod(int methodId) {
        if (textEraseMethodTelea.getRadioButton().getId() == methodId) {
            return textEraseMethodTelea.getImage();
        }
        return textEraseMethodNS.getImage();
    }

    private void save(int methodId, String imgPath) {
        String path = ImageAction.save(activity, getImageFromMethod(methodId), imgPath);

        Intent intent = new Intent();
        intent.putExtra(ImageDetailActivity.IMG_RETURN_PATH, path);
        activity.setResult(ImageDetailActivity.RETURN_REQUEST_CODE, intent);
        dialog.dismiss();

        activity.finish();
    }

    private void saveAsNew(int methodId) {
        String path = ImageAction.saveAsNew(activity, getImageFromMethod(methodId));

        Intent intent = new Intent();
        intent.putExtra(ImageDetailActivity.IMG_RETURN_PATH, path);
        activity.setResult(ImageDetailActivity.RETURN_REQUEST_CODE, intent);
        dialog.dismiss();

        activity.finish();
    }

    private void cancel(){
        dialog.dismiss();
    }

}

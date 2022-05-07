/*
 * Source code for the frontend of Bachelor thesis.
 * TextEraseMethodDialog class
 *
 * (C) Patrik Ondriga (xondri08)
 */

package postCutter.fe.postcutter.dialogs;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.widget.RadioGroup;

import postCutter.fe.postcutter.ImageDetailActivity;
import postCutter.fe.postcutter.R;
import postCutter.fe.postcutter.functions.ImageAction;
import postCutter.fe.postcutter.functions.TextEraseMethod;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.opencv.core.Mat;

import inpainting.Inpainter;
import postCutter.geometricShapes.rectangle.MyRectangle;

/**
 * Representing dialog for save image text replace.
 */
public class TextEraseMethodDialog {
    /// Dialog window.
    private final androidx.appcompat.app.AlertDialog dialog;
    /// Activity in which is this dialog used.
    private final Activity activity;
    /// Telea replace method.
    private final TextEraseMethod textEraseMethodTelea;
    /// NS replace method.
    private final TextEraseMethod textEraseMethodNS;
    /// Image path for replace.
    private final String imgPath;
    /// Group for buttons with results of replaces methods.
    private final RadioGroup radioGroup;

    /**
     * Constructor.
     *
     * @param activity  Activity in which is this dialog used.
     * @param img       image for replace.
     * @param rectangle area for replace.
     * @param imgPath   path of image for replace.
     */
    public TextEraseMethodDialog(Activity activity, Mat img, MyRectangle rectangle, String imgPath) {
        this.activity = activity;
        this.imgPath = imgPath;

        LayoutInflater inflater = activity.getLayoutInflater();
        dialog = new MaterialAlertDialogBuilder(activity)
                .setView(inflater.inflate(R.layout.custom_text_erase_dialog, null))
                .setTitle(R.string.text_erase_dialog_title)
                .setNeutralButton(R.string.save_as_new, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveAsNew();
                    }
                })
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        save();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();

        radioGroup = dialog.findViewById(R.id.textEraseDialog_radioGroup);
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
    }

    /**
     * Only replaced are from image.
     *
     * @return replaced are image.
     */
    private Bitmap getImageFromMethod() {
        int methodId = radioGroup.getCheckedRadioButtonId();
        if (textEraseMethodTelea.getRadioButton().getId() == methodId) {
            return textEraseMethodTelea.getImage();
        }
        return textEraseMethodNS.getImage();
    }

    /**
     * Replace image on path by this new image.
     */
    private void save() {
        String path = ImageAction.save(activity, getImageFromMethod(), imgPath);

        Intent intent = new Intent();
        intent.putExtra(ImageDetailActivity.IMG_RETURN_PATH, path);
        activity.setResult(ImageDetailActivity.RETURN_REQUEST_CODE, intent);
        dialog.dismiss();

        activity.finish();
    }

    /**
     * Save image as new.
     */
    private void saveAsNew() {
        String path = ImageAction.saveAsNew(activity, getImageFromMethod());

        Intent intent = new Intent();
        intent.putExtra(ImageDetailActivity.IMG_RETURN_PATH, path);
        activity.setResult(ImageDetailActivity.RETURN_REQUEST_CODE, intent);
        dialog.dismiss();

        activity.finish();
    }

}

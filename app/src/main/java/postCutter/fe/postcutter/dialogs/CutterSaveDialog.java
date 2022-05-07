/*
 * Source code for the frontend of Bachelor thesis.
 * CutterSaveDialog class
 *
 * (C) Patrik Ondriga (xondri08)
 */

package postCutter.fe.postcutter.dialogs;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;

import postCutter.fe.postcutter.ImageDetailActivity;
import postCutter.fe.postcutter.R;
import postCutter.fe.postcutter.functions.ImageAction;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

/**
 * Representing dialog for save image after crop.
 */
public class CutterSaveDialog {
    /// Dialog window.
    private final androidx.appcompat.app.AlertDialog dialog;
    /// Activity in which is this dialog used.
    private final Activity activity;
    /// Image for save.
    private final Bitmap img;
    /// Path of image for save.
    private final String imgPath;

    /**
     * Constructor.
     *
     * @param activity Activity in which is this dialog used.
     * @param img      Image for save.
     * @param imgPath  Path of image for save.
     */
    public CutterSaveDialog(Activity activity, Bitmap img, String imgPath) {
        this.activity = activity;
        this.img = img;
        this.imgPath = imgPath;

        dialog = new MaterialAlertDialogBuilder(activity)
                .setTitle(R.string.cutter_save_dialog_title)
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
    }

    /**
     * Replace image on path by this new image.
     */
    private void save() {
        String path = ImageAction.save(activity, img, imgPath);
        closeActivity(path);
    }

    /**
     * Save image as new.
     */
    private void saveAsNew() {
        String path = ImageAction.saveAsNew(activity, img);
        closeActivity(path);
    }

    /**
     * Close activity and store image path.
     *
     * @param path of image.
     */
    private void closeActivity(String path) {
        Intent intent = new Intent();
        intent.putExtra(ImageDetailActivity.IMG_RETURN_PATH, path);
        activity.setResult(ImageDetailActivity.RETURN_REQUEST_CODE, intent);
        dialog.dismiss();

        activity.finish();
    }
}

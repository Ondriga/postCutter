package postCutter.fe.postcutter.dialogs;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;

import postCutter.fe.postcutter.ImageDetailActivity;
import postCutter.fe.postcutter.R;
import postCutter.fe.postcutter.functions.ImageAction;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;


public class CutterSaveDialog {
    private final androidx.appcompat.app.AlertDialog dialog;
    private final Activity activity;
    private final Bitmap img;
    private final String imgPath;

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

    private void save() {
        String path = ImageAction.save(activity, img, imgPath);
        closeActivity(path);
    }

    private void saveAsNew() {
        String path = ImageAction.saveAsNew(activity, img);
        closeActivity(path);
    }


    private void closeActivity(String path) {
        Intent intent = new Intent();
        intent.putExtra(ImageDetailActivity.IMG_RETURN_PATH, path);
        activity.setResult(ImageDetailActivity.RETURN_REQUEST_CODE, intent);
        dialog.dismiss();

        activity.finish();
    }
}

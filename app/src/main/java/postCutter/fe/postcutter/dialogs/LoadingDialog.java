/*
 * Source code for the frontend of Bachelor thesis.
 * LoadingDialog class
 *
 * (C) Patrik Ondriga (xondri08)
 */

package postCutter.fe.postcutter.dialogs;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import postCutter.fe.postcutter.CutterActivity;
import postCutter.fe.postcutter.R;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import postCutter.MyProgress;

/**
 * Representing loading dialog during image computing.
 */
public class LoadingDialog {
    /// Activity in which is this dialog used.
    private final Activity activity;
    /// Dialog window.
    private AlertDialog dialog;
    /// Progress of computing image crop suggestion.
    private final Progress progress;
    /// Showing current percentages.
    private TextView textView;
    /// Progress bar.
    private ProgressBar progressBar;

    /**
     * Constructor.
     *
     * @param activity Activity in which is this dialog used.
     */
    public LoadingDialog(Activity activity) {
        this.activity = activity;
        this.progress = new Progress();
    }

    /**
     * Show loading dialog and start progress percentages.
     *
     * @param thread where image crop suggestion computing.
     */
    public void startLoadingDialog(CutterActivity.ImageProcess thread) {

        LayoutInflater inflater = activity.getLayoutInflater();
        dialog = new MaterialAlertDialogBuilder(activity)
                .setView(inflater.inflate(R.layout.custom_dialog, null))
                .setCancelable(false)
                .setTitle(R.string.loading_text)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        thread.stopThread();
                    }
                })
                .show();

        textView = dialog.findViewById(R.id.dialog_textView);
        progressBar = dialog.findViewById(R.id.dialog_progressBar);
    }

    /**
     * Close dialog window.
     */
    public void stopLoadingDialog() {
        dialog.dismiss();
    }

    /**
     * Getter for progress.
     *
     * @return progress object.
     */
    public Progress getProgress() {
        return this.progress;
    }

    /**
     * Representing progress where the percentages are updated.
     */
    public class Progress extends MyProgress {

        @Override
        public void update(int i, int i1) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setMax(i1);
                    progressBar.setProgress(i);
                    textView.setText((i * 100) / i1 + "%");
                }
            });
        }
    }
}

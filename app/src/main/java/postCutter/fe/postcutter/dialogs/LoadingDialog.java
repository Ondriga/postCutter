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

public class LoadingDialog {

    private final Activity activity;
    private AlertDialog dialog;
    private final Progress progress;
    private TextView textView;
    private ProgressBar progressBar;

    public LoadingDialog(Activity activity) {
        this.activity = activity;
        this.progress = new Progress();
    }

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

    public void stopLoadingDialog() {
        dialog.dismiss();
    }

    public Progress getProgress() {
        return this.progress;
    }

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

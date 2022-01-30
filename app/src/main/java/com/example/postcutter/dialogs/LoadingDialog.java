package com.example.postcutter.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.postcutter.CutterActivity;
import com.example.postcutter.R;

import postCutter.MyProgress;

public class LoadingDialog {

    private final Activity activity;
    private AlertDialog dialog;
    private Progress progress;
    private TextView textView;
    private ProgressBar progressBar;

    public LoadingDialog(Activity activity) {
        this.activity = activity;
        this.progress = new Progress();
    }

    public void startLoadingDialog(CutterActivity.ImageProcess thread) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custom_dialog, null));
        builder.setCancelable(false);
        builder.setTitle(R.string.loading_text);

        dialog = builder.create();
        dialog.show();

        Button cancelButton = dialog.findViewById(R.id.dialog_button);
        cancelButton.setOnClickListener(e -> cancelProcessing(thread));
        textView = dialog.findViewById(R.id.dialog_textView);
        progressBar = dialog.findViewById(R.id.dialog_progressBar);
    }

    public void stopLoadingDialog() {
        dialog.dismiss();
    }

    private void cancelProcessing(CutterActivity.ImageProcess thread) {
        thread.stopThread();
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

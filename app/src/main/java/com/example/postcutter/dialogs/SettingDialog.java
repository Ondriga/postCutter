package com.example.postcutter.dialogs;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.widget.SeekBar;

import com.example.postcutter.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class SettingDialog {
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String SUGGESTION_SWITCH = "suggestionSwitch";
    public static final String SUGGESTION_ACCURACY = "suggestionAccuracy";
    public static final Boolean SWITCH_DEFAULT = true;
    public static final int ACCURACY_DEFAULT = 2;

    private final Activity activity;

    private final androidx.appcompat.app.AlertDialog dialog;
    private SharedPreferences sharedPreferences;

    private SwitchMaterial suggestionSwitch;
    private SeekBar suggestionAccuracy;

    private boolean allowSuggestion = false;

    private boolean reload = false;
    private int oldAccuracySetting = ACCURACY_DEFAULT;

    public SettingDialog(Activity activity) {
        this.activity = activity;

        LayoutInflater inflater = activity.getLayoutInflater();
        dialog = new MaterialAlertDialogBuilder(activity)
                .setView(inflater.inflate(R.layout.custom_settings_dialog, null))
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        reload = false;
                    }
                })
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        editor.putBoolean(SUGGESTION_SWITCH, suggestionSwitch.isChecked());
                        editor.putInt(SUGGESTION_ACCURACY, suggestionAccuracy.getProgress());

                        editor.apply();
                        reload = oldAccuracySetting != suggestionAccuracy.getProgress();
                    }
                })
                .setNeutralButton(R.string.reload, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        reload = true;
                    }
                }).create();
    }

    public void startDialog(boolean allowSuggestion) {
        dialog.show();
        reload = false;

        sharedPreferences = activity.getSharedPreferences(SHARED_PREFS, activity.MODE_PRIVATE);
        suggestionSwitch = dialog.findViewById(R.id.dialog_settings_switch);
        suggestionAccuracy = dialog.findViewById(R.id.dialog_settings_seekBar);

        if (allowSuggestion) {//if is one allowed, then it will be allow forever
            this.allowSuggestion = true;
        }
        suggestionSwitch.setEnabled(this.allowSuggestion);

        suggestionSwitch.setChecked(sharedPreferences.getBoolean(SUGGESTION_SWITCH, SWITCH_DEFAULT));
        oldAccuracySetting = sharedPreferences.getInt(SUGGESTION_ACCURACY, ACCURACY_DEFAULT);
        suggestionAccuracy.setProgress(oldAccuracySetting);
    }

    public boolean doReload() {
        return reload;
    }

    public androidx.appcompat.app.AlertDialog getDialog() {
        return dialog;
    }
}

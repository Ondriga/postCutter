package com.example.postcutter.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.widget.SeekBar;

import com.example.postcutter.R;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class SettingDialog {
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String SUGGESTION_SWITCH = "suggestionSwitch";
    public static final String SUGGESTION_ACCURACY = "suggestionAccuracy";
    public static final Boolean SWITCH_DEFAULT = true;
    public static final int ACCURACY_DEFAULT = 2;

    private AlertDialog dialog;
    private SharedPreferences sharedPreferences;

    private SwitchMaterial suggestionSwitch;
    private SeekBar suggestionAccuracy;

    public SettingDialog(Activity activity) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custom_settings_dialog, null));

        builder.setTitle(R.string.settings_dialog_title);
        builder.setNegativeButton(R.string.cancel, null);
        builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putBoolean(SUGGESTION_SWITCH, suggestionSwitch.isChecked());
                editor.putInt(SUGGESTION_ACCURACY, suggestionAccuracy.getProgress());

                editor.apply();
            }
        });

        dialog = builder.create();
        dialog.show();

        sharedPreferences = activity.getSharedPreferences(SHARED_PREFS, activity.MODE_PRIVATE);
        suggestionSwitch = dialog.findViewById(R.id.dialog_settings_switch);
        suggestionAccuracy = dialog.findViewById(R.id.dialog_settings_seekBar);

        suggestionSwitch.setChecked(sharedPreferences.getBoolean(SUGGESTION_SWITCH, SWITCH_DEFAULT));
        suggestionAccuracy.setProgress(sharedPreferences.getInt(SUGGESTION_ACCURACY, ACCURACY_DEFAULT));
    }

}

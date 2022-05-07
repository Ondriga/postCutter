/*
 * Source code for the frontend of Bachelor thesis.
 * LoadingDialog class
 *
 * (C) Patrik Ondriga (xondri08)
 */

package postCutter.fe.postcutter.dialogs;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.widget.SeekBar;
import android.widget.TextView;

import postCutter.fe.postcutter.R;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.switchmaterial.SwitchMaterial;

/**
 * Representing loading dialog during image computing.
 */
public class SettingDialog {
    /// Constant of key for access to store values.
    public static final String SHARED_PREFS = "sharedPrefs";
    /// Constant of key for "move helper" switch state.
    public static final String SUGGESTION_SWITCH = "suggestionSwitch";
    /// Constant of key for "accuracy" value.
    public static final String SUGGESTION_ACCURACY = "suggestionAccuracy";
    /// Constant of key for default state of "move helper" switch.
    public static final Boolean SWITCH_DEFAULT = true;
    /// Constant of key for default "accuracy" value.
    public static final int ACCURACY_DEFAULT = 4;

    /// Activity in which is this dialog used.
    private final Activity activity;
    /// Dialog window.
    private final androidx.appcompat.app.AlertDialog dialog;
    /// Store object.
    private SharedPreferences sharedPreferences;
    /// "move helper" switch.
    private SwitchMaterial suggestionSwitch;
    /// "move helper" slider.
    private SeekBar suggestionAccuracy;
    /// Allow "move helper" state.
    private boolean allowSuggestion = false;
    /// Suggest crop again after dialog close.
    private boolean reload = false;
    /// "accuracy" value.
    private int oldAccuracySetting = ACCURACY_DEFAULT;

    /**
     * Constructor.
     *
     * @param activity Activity in which is this dialog used.
     */
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

    /**
     * Show dialog window.
     *
     * @param allowSuggestion allow change
     */
    public void startDialog(boolean allowSuggestion) {
        dialog.show();
        reload = false;

        sharedPreferences = activity.getSharedPreferences(SHARED_PREFS, activity.MODE_PRIVATE);
        suggestionSwitch = dialog.findViewById(R.id.dialog_settings_switch);
        suggestionAccuracy = dialog.findViewById(R.id.dialog_settings_seekBar);

        TextView title = dialog.findViewById(R.id.dialog_settings_text);
        suggestionSwitch.setEnabled(true);
        title.setTextColor(suggestionSwitch.getCurrentTextColor());
        suggestionSwitch.setEnabled(false);

        if (allowSuggestion) {//if is one allowed, then it will be allow forever
            this.allowSuggestion = true;
        }
        suggestionSwitch.setEnabled(this.allowSuggestion);

        suggestionSwitch.setChecked(sharedPreferences.getBoolean(SUGGESTION_SWITCH, SWITCH_DEFAULT));
        oldAccuracySetting = sharedPreferences.getInt(SUGGESTION_ACCURACY, ACCURACY_DEFAULT);
        suggestionAccuracy.setProgress(oldAccuracySetting);
    }

    /**
     * Do suggestion again.
     *
     * @return true if do, otherwise false.
     */
    public boolean doReload() {
        return reload;
    }

    /**
     * Getter for dialog object.
     *
     * @return dialog object.
     */
    public androidx.appcompat.app.AlertDialog getDialog() {
        return dialog;
    }
}

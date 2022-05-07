/*
 * Source code for the frontend of Bachelor thesis.
 * HelpDialog class
 *
 * (C) Patrik Ondriga (xondri08)
 */

package postCutter.fe.postcutter.dialogs;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

import postCutter.fe.postcutter.R;
import postCutter.fe.postcutter.functions.Help;

/**
 * Representing dialog for help tutorial.
 */
public class HelpDialog {
    /// Activity in which is this dialog used.
    private final Activity activity;
    /// List of help objects.
    private final List<Help> helps;
    /// Dialog builder.
    private final MaterialAlertDialogBuilder dialogBuilder;
    /// Dialog window.
    private AlertDialog dialog;
    /// Index of help from list of helps.
    private int index = 0;

    /// Text of help.
    private TextView text;
    /// ImageView for help gif.
    private ImageView image;

    /**
     * Constructor.
     *
     * @param activity Activity in which is this dialog used.
     * @param helps    List of helps.
     */
    public HelpDialog(Activity activity, List<Help> helps) {
        this.activity = activity;
        this.helps = helps;

        LayoutInflater inflater = activity.getLayoutInflater();
        this.dialogBuilder = new MaterialAlertDialogBuilder(activity)
                .setCancelable(false)
                .setTitle(activity.getText(R.string.help_title) + " 0/" + helps.size())
                .setPositiveButton(activity.getResources().getString(R.string.next), null)
                .setNegativeButton(activity.getResources().getString(R.string.skip), null)
                .setView(inflater.inflate(R.layout.help_dialog, null));
    }

    /**
     * Show dialog with help text and gif.
     */
    public void show() {
        this.dialog = this.dialogBuilder.create();

        this.dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                next();
            }
        });

        dialog.show();

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(e -> next());
        this.text = dialog.findViewById(R.id.help_dialog__text);
        this.image = dialog.findViewById(R.id.help_dialog__image);
    }

    /**
     * Show next help message and gif. If all help messages was show, then close dialog window.
     */
    private void next() {
        if (helps.size() > index) {
            Help help = helps.get(index++);
            dialog.setTitle(activity.getText(R.string.help_title) + " " + index + "/" + helps.size());
            this.text.setText(help.getText());
            Glide.with(activity).asGif().load(help.getImage()).into(this.image);
            if (index == helps.size()) {
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setText(activity.getResources().getString(R.string.close));
                dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setVisibility(View.GONE);
            }
        } else {
            dialog.dismiss();
        }
    }
}

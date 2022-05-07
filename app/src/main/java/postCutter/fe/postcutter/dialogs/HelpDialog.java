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

public class HelpDialog {
    private final Activity activity;
    private final List<Help> helps;
    private final MaterialAlertDialogBuilder dialogBuilder;
    private AlertDialog dialog;
    private int index = 0;

    private TextView text;
    private ImageView image;

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

package postCutter.fe.postcutter.dialogs;

import android.app.Activity;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;

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
    private TextView counter;
    private AppCompatButton button;

    public HelpDialog(Activity activity, List<Help> helps) {
        this.activity = activity;
        this.helps = helps;

        LayoutInflater inflater = activity.getLayoutInflater();
        this.dialogBuilder = new MaterialAlertDialogBuilder(activity)
                .setCancelable(false)
                .setTitle(R.string.help_title)
                .setView(inflater.inflate(R.layout.help_dialog, null));
    }

    public void show() {
        this.dialog = this.dialogBuilder.show();

        this.text = dialog.findViewById(R.id.help_dialog__text);
        this.image = dialog.findViewById(R.id.help_dialog__image);
        this.counter = dialog.findViewById(R.id.help_dialog__counter);
        this.button = dialog.findViewById(R.id.help_dialog__button);
        this.button.setOnClickListener(e -> next());

        this.next();
    }

    private void next() {
        if (helps.size() > index) {
            Help help = helps.get(index++);
            this.counter.setText(index + "/" + helps.size());
            this.text.setText(help.getText());
            Glide.with(activity).asGif().load(help.getImage()).into(this.image);
            if (index == helps.size()){
                this.button.setText(activity.getResources().getString(R.string.close));
            }
        } else {
            dialog.dismiss();
        }
    }
}

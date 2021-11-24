package com.example.postcutter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.example.postcutter.customViews.EraseView;
import com.squareup.picasso.Picasso;

public class TextEraseActivity extends AppCompatActivity {
    private String imagePath;
    private EraseView eraseView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_erase);

        imagePath = getIntent().getStringExtra("imagePath");
        eraseView = findViewById(R.id.textErase_eraseView);

        Button button = findViewById(R.id.textErase_button);
        button.setOnClickListener(e -> doClick());

        Picasso.get().load("file:" + imagePath).into(eraseView.getImageView());
    }

    private void doClick() {
        System.out.println(eraseView.getRectangle());//TODO debug
    }

}
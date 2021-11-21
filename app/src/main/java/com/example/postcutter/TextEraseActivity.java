package com.example.postcutter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.ViewTreeObserver;

import com.example.postcutter.customViews.RectangleView;

import postCutter.geometricShapes.Coordinate;

public class TextEraseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_erase);

        RectangleView rectangle = findViewById(R.id.textErase_rectangle);

        ConstraintLayout mainLayout = findViewById(R.id.textErase_mainLayout);
        mainLayout.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        mainLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                        Coordinate cornerA = new Coordinate(rectangle.getLeft(), rectangle.getTop());
                        Coordinate cornerB = new Coordinate(rectangle.getRight(), rectangle.getBottom());
                        rectangle.setRectangle(cornerA, cornerB);
                        System.out.println("###########################");//TODO debug
                        System.out.println(rectangle.getRectangle());//TODO debug
                        System.out.println("###########################");//TODO debug
                    }
                }
        );
    }

}
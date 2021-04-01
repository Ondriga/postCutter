package com.example.postcutter.cutter;

import android.app.Activity;

import com.example.postcutter.cutter.lines.HorizontalLinesHandler;
import com.example.postcutter.cutter.lines.VerticalLinesHandler;

import postCutter.Cutter;
import postCutter.geometricShapes.rectangle.MyRectangle;


public class CutterGui{

    private final HorizontalLinesHandler horizontalLines;
    private final VerticalLinesHandler verticalLines;

    public CutterGui(Activity activity){
        horizontalLines = new HorizontalLinesHandler(activity);
        verticalLines = new VerticalLinesHandler(activity);
    }

    public void loadImage(Cutter cutter, MyRectangle imageRectangle, int originalWidth, int originalHeight){
        horizontalLines.loadImage(cutter.getRectangle(), imageRectangle, originalHeight, cutter.getHorizontalLines());
        verticalLines.loadImage(cutter.getRectangle(), imageRectangle, originalWidth, cutter.getVerticalLines());
    }

}

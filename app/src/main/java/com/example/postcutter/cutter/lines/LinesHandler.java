package com.example.postcutter.cutter.lines;

import android.app.Activity;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.postcutter.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import postCutter.geometricShapes.line.MyLine;
import postCutter.geometricShapes.rectangle.MyRectangle;

public class LinesHandler {
    protected static final int MIN_CUT_SIDE = 100;

    protected final int lineWidth;
    protected final int lineArea;

    protected final View topLine;
    protected final View bottomLine;
    protected final View leftLine;
    protected final View rightLine;

    protected MyRectangle cutRectangle;
    protected MyRectangle imageRectangle;

    public LinesHandler(Activity activity) {
        this.topLine = activity.findViewById(R.id.cutter_topView);
        this.bottomLine = activity.findViewById(R.id.cutter_bottomView);
        this.leftLine = activity.findViewById(R.id.cutter_leftView);
        this.rightLine = activity.findViewById(R.id.cutter_rightView);

        this.lineWidth = this.leftLine.getLayoutParams().width;
        this.lineArea = this.lineWidth * 2;
    }

    public int mapping(int from, int to, int value){
        double ration = (double) from / to;
        return (int) Math.round(value / ration);
    }

    protected void loadImage(MyRectangle cutRectangle, MyRectangle imageRectangle){
        this.cutRectangle = cutRectangle;
        this.imageRectangle = imageRectangle;
    }

    protected void setCutRectangleHeight(){
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) this.leftLine.getLayoutParams();
        int newHeight = Math.round(this.bottomLine.getY() - this.topLine.getY() + this.lineWidth);
        params.height = newHeight;
        this.leftLine.setLayoutParams(params);
        params = (RelativeLayout.LayoutParams) this.rightLine.getLayoutParams();
        params.height = newHeight;
        this.rightLine.setLayoutParams(params);
    }

    protected void setCutRectangleWidth(){
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) this.topLine.getLayoutParams();
        int newWidth = Math.round(this.rightLine.getX() - this.leftLine.getX() + this.lineWidth);
        params.width = newWidth;
        this.topLine.setLayoutParams(params);
        params = (RelativeLayout.LayoutParams) this.bottomLine.getLayoutParams();
        params.width = newWidth;
        this.bottomLine.setLayoutParams(params);
    }

    protected float nearBreakPoint(float value, List<Float> breakPoints){
        for(float breakPoint : breakPoints){
            if (Math.abs(breakPoint - value) < this.lineArea) {
                return breakPoint;
            }
        }
        return value;
    }
}

package com.example.postcutter.cutter.lines;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.postcutter.R;
import com.example.postcutter.SettingsActivity;

import java.util.List;

import postCutter.geometricShapes.rectangle.MyRectangle;

public class LinesHandler {
    protected static final int MIN_CUT_SIDE = 100;

    protected final int lineWidth;
    protected final int lineArea;

    protected final View topLine;
    protected final View bottomLine;
    protected final View leftLine;
    protected final View rightLine;

    protected final View topShadow;
    protected final View bottomShadow;
    protected final View leftShadow;
    protected final View rightShadow;

    protected MyRectangle cutRectangle;
    protected MyRectangle imageRectangle;

    protected boolean suggestLine;

    public LinesHandler(Activity activity) {
        this.topLine = activity.findViewById(R.id.cutter_topView);
        this.bottomLine = activity.findViewById(R.id.cutter_bottomView);
        this.leftLine = activity.findViewById(R.id.cutter_leftView);
        this.rightLine = activity.findViewById(R.id.cutter_rightView);

        this.topShadow = activity.findViewById(R.id.cutter_topViewShadow);
        this.bottomShadow = activity.findViewById(R.id.cutter_bottomViewShadow);
        this.leftShadow = activity.findViewById(R.id.cutter_leftViewShadow);
        this.rightShadow = activity.findViewById(R.id.cutter_rightViewShadow);

        this.lineWidth = this.leftLine.getLayoutParams().width;
        this.lineArea = this.lineWidth * 2;

        SharedPreferences sharedPreferences = activity.getSharedPreferences(SettingsActivity.SHARED_PREFS, activity.MODE_PRIVATE);
        suggestLine = sharedPreferences.getBoolean(SettingsActivity.SUGGESTION_SWITCH, SettingsActivity.SWITCH_DEFAULT);
    }

    public int mapping(int from, int to, int value){
        double ration = (double) from / to;
        return (int) Math.round(value / ration);
    }

    protected void loadImage(MyRectangle cutRectangle, MyRectangle imageRectangle){
        this.cutRectangle = cutRectangle;
        this.imageRectangle = imageRectangle;

        this.topLine.setVisibility(View.VISIBLE);
        this.bottomLine.setVisibility(View.VISIBLE);
        this.leftLine.setVisibility(View.VISIBLE);
        this.rightLine.setVisibility(View.VISIBLE);

        setUpShadow();
    }

    private void setUpShadow(){
        this.topShadow.setX(this.imageRectangle.getCornerA().getX());
        this.bottomShadow.setX(this.imageRectangle.getCornerA().getX());

        int shadowWidth = this.imageRectangle.getWidth();

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) this.topShadow.getLayoutParams();
        params.width = shadowWidth;
        this.topShadow.setLayoutParams(params);
        params = (RelativeLayout.LayoutParams) this.bottomShadow.getLayoutParams();
        params.width = shadowWidth;
        this.bottomShadow.setLayoutParams(params);
    }

    protected void setCutRectangleHeight(){
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) this.leftLine.getLayoutParams();
        int newHeight = Math.round(this.bottomLine.getY() - this.topLine.getY() + this.lineWidth);
        params.height = newHeight;
        this.leftLine.setLayoutParams(params);
        params = (RelativeLayout.LayoutParams) this.rightLine.getLayoutParams();
        params.height = newHeight;
        this.rightLine.setLayoutParams(params);

        params = (RelativeLayout.LayoutParams) this.leftShadow.getLayoutParams();
        params.height = newHeight - 2 * this.lineWidth;
        this.leftShadow.setLayoutParams(params);
        params = (RelativeLayout.LayoutParams) this.rightShadow.getLayoutParams();
        params.height = newHeight - 2 * this.lineWidth;
        this.rightShadow.setLayoutParams(params);
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

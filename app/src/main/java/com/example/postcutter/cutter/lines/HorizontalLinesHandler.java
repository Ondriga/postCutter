package com.example.postcutter.cutter.lines;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import postCutter.geometricShapes.line.MyLine;
import postCutter.geometricShapes.rectangle.MyRectangle;

public class HorizontalLinesHandler extends LinesHandler {
    private int originalHeight;
    private float downTopY;
    private float downBottomY;

    private final List<Float> breakPoints = new ArrayList<>();

    public HorizontalLinesHandler(Activity activity) {
        super(activity);

        this.topLine.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                moveTop(event);
                return true;
            }
        });
        this.bottomLine.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                moveBottom(event);
                return true;
            }
        });
    }

    public void loadImage(MyRectangle cutRectangle, MyRectangle imageRectangle, int originalHeight, List<MyLine> horizontalLines){
        super.loadImage(cutRectangle, imageRectangle);
        this.originalHeight = originalHeight;

        float tmp = -100000;
        for(MyLine line : horizontalLines){
            float breakPoint = (float) mapping(this.originalHeight,
                    this.imageRectangle.getHeight(),
                    line.getStartPoint().getY()) + this.imageRectangle.getCornerA().getY();
            if(Math.abs(tmp - breakPoint) > this.lineArea){
                breakPoints.add(breakPoint);
            }
            tmp = breakPoint;
        }

        this.downTopY = mapping(this.originalHeight, this.imageRectangle.getHeight(), this.cutRectangle.getCornerA().getY())
                + this.imageRectangle.getCornerA().getY() - this.lineWidth;
        this.downBottomY = mapping(this.originalHeight, this.imageRectangle.getHeight(), this.cutRectangle.getCornerB().getY())
                + this.imageRectangle.getCornerA().getY();

        setBottom(this.downBottomY);
        setTop(this.downTopY);
    }

    private void setTop(float newY){
        if(newY < this.imageRectangle.getCornerA().getY() - this.lineWidth + this.lineArea){
            newY = this.imageRectangle.getCornerA().getY() - this.lineWidth;
        }else if(this.bottomLine.getY() - newY < MIN_CUT_SIDE){
            newY = this.bottomLine.getY() - MIN_CUT_SIDE;
        }
        this.topLine.setY(newY);

        this.leftLine.setY(newY);
        this.rightLine.setY(newY);
        setCutRectangleHeight();
    }

    private void moveTop(MotionEvent event){
        switch (event.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
                this.downTopY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float movedY = event.getY();
                float distanceY = movedY - this.downTopY;
                float newY = this.topLine.getY() + distanceY;
                float tmp = this.nearBreakPoint(newY, this.breakPoints);
                if(tmp != newY){
                    newY = tmp - this.lineWidth;
                }
                setTop(newY);
                break;
            case MotionEvent.ACTION_UP:
                int cutPosition = (int) this.topLine.getY() - this.imageRectangle.getCornerA().getY() + this.lineWidth;
                this.cutRectangle.getCornerA().setY(
                        mapping(this.imageRectangle.getHeight(), this.originalHeight, cutPosition)
                );
        }
    }

    private void setBottom(float newY){
        if(newY > this.imageRectangle.getCornerB().getY() - this.lineArea){
            newY = this.imageRectangle.getCornerB().getY();
        }else if(newY - this.topLine.getY() < MIN_CUT_SIDE){
            newY = this.topLine.getY() + MIN_CUT_SIDE;
        }
        this.bottomLine.setY(newY);

        setCutRectangleHeight();
    }

    private void moveBottom(MotionEvent event){
        switch (event.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
                this.downBottomY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float movedY = event.getY();
                float distanceY = movedY - this.downBottomY;
                float newY = this.bottomLine.getY() + distanceY;
                newY = this.nearBreakPoint(newY, this.breakPoints);
                setBottom(newY);
                break;
            case MotionEvent.ACTION_UP:
                int cutPosition = (int) this.bottomLine.getY() - this.imageRectangle.getCornerA().getY();
                this.cutRectangle.getCornerB().setY(
                        mapping(this.imageRectangle.getHeight(), this.originalHeight, cutPosition)
                );
        }
    }

}

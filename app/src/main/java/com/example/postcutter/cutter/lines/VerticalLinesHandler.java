package com.example.postcutter.cutter.lines;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import postCutter.geometricShapes.line.MyLine;
import postCutter.geometricShapes.rectangle.MyRectangle;

public class VerticalLinesHandler extends LinesHandler {
    private int originalWidth;
    private float downLeftX;
    private float downRightX;

    private final List<Float> breakPoints = new ArrayList<>();

    public VerticalLinesHandler(Activity activity) {
        super(activity);

        this.leftLine.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                moveLeft(event);
                return true;
            }
        });
        this.rightLine.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                moveRight(event);
                return true;
            }
        });
    }

    public void loadImage(MyRectangle cutRectangle, MyRectangle imageRectangle, int originalWidth, List<MyLine> verticalLines){
        super.loadImage(cutRectangle, imageRectangle);
        this.originalWidth = originalWidth;

        if(suggestLine) {
            for (MyLine line : verticalLines) {
                float breakPoint = (float) mapping(this.originalWidth,
                        this.imageRectangle.getWidth(),
                        line.getStartPoint().getX()) + this.imageRectangle.getCornerA().getX();
                breakPoints.add(breakPoint);
            }
        }

        this.downLeftX = mapping(this.originalWidth, this.imageRectangle.getWidth(), this.cutRectangle.getCornerA().getX())
                + this.imageRectangle.getCornerA().getX() - this.lineWidth;
        this.downRightX = mapping(this.originalWidth, this.imageRectangle.getWidth(), this.cutRectangle.getCornerB().getX())
                + this.imageRectangle.getCornerA().getX();

        setRight(this.downRightX);
        setLeft(this.downLeftX);
    }

    private void setLeft(float newX){
        if(newX < this.imageRectangle.getCornerA().getX() - this.lineWidth) {
            newX = this.imageRectangle.getCornerA().getX() - this.lineWidth;
        }else if(suggestLine && newX < this.imageRectangle.getCornerA().getX() - this.lineWidth + this.lineArea){
            newX = this.imageRectangle.getCornerA().getX() - this.lineWidth;
        }else if(this.rightLine.getX() - newX < MIN_CUT_SIDE) {
            newX = this.rightLine.getX() - MIN_CUT_SIDE;
        }
        this.leftLine.setX(newX);
        setShadow(this.imageRectangle.getCornerA().getX(), (int) newX + this.lineWidth, this.leftShadow);

        this.topLine.setX(newX);
        this.bottomLine.setX(newX);
        setCutRectangleWidth();
    }

    private void moveLeft(MotionEvent event){
        switch (event.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
                this.downLeftX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                float movedX = event.getX();
                float distanceX = movedX - this.downLeftX;
                float newX = this.leftLine.getX() + distanceX;
                if(suggestLine) {
                    float tmp = this.nearBreakPoint(newX, this.breakPoints);
                    if (tmp != newX) {
                        newX = tmp - this.lineWidth;
                    }
                }
                setLeft(newX);
                break;
            case MotionEvent.ACTION_UP:
                int cutPosition = (int) this.leftLine.getX() - this.imageRectangle.getCornerA().getX() + this.lineWidth;
                this.cutRectangle.getCornerA().setX(
                        mapping(this.imageRectangle.getWidth(), this.originalWidth, cutPosition)
                );
        }
    }

    private void setRight(float newX){
        if(newX > this.imageRectangle.getCornerB().getX()) {
            newX = this.imageRectangle.getCornerB().getX();
        }else if(suggestLine && newX > this.imageRectangle.getCornerB().getX() - this.lineArea){
            newX = this.imageRectangle.getCornerB().getX();
        }else if(newX - this.leftLine.getX() < MIN_CUT_SIDE) {
            newX = this.leftLine.getX() + MIN_CUT_SIDE;
        }
        this.rightLine.setX(newX);
        setShadow((int) newX, this.imageRectangle.getCornerB().getX(), this.rightShadow);

        setCutRectangleWidth();
    }

    private void moveRight(MotionEvent event){
        switch (event.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
                this.downRightX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                float movedX = event.getX();
                float distanceX = movedX - this.downRightX;
                float newX = this.rightLine.getX() + distanceX;
                if(suggestLine) {
                    newX = this.nearBreakPoint(newX, this.breakPoints);
                }
                setRight(newX);
                break;
            case MotionEvent.ACTION_UP:
                int cutPosition = (int) this.rightLine.getX() - this.imageRectangle.getCornerA().getX();
                this.cutRectangle.getCornerB().setX(
                        mapping(this.imageRectangle.getWidth(), this.originalWidth, cutPosition)
                );
        }
    }

    private void setShadow(int start, int end, View shadow){
        if(start >= end){
            shadow.setVisibility(View.INVISIBLE);
            return;
        }
        shadow.setX(start);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) shadow.getLayoutParams();
        params.width = end - start;
        shadow.setLayoutParams(params);
        shadow.setVisibility(View.VISIBLE);
    }
}

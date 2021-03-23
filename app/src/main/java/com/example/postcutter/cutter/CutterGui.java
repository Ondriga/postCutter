package com.example.postcutter.cutter;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.postcutter.R;

import postCutter.geometricShapes.rectangle.MyRectangle;


public class CutterGui {
    private static final int MIN_CUT_SIDE = 100;

    private final int lineWidth;

    private MyRectangle cutRectangle;
    private MyRectangle imageRectangle;
    private int originalWidth;
    private int originalHeight;

    private final View topLine;
    private final View bottomLine;
    private final View leftLine;
    private final View rightLine;

    private float downTopY;
    private float downBottomY;
    private float downLeftX;
    private float downRightX;

    public CutterGui(Activity activity){
        this.topLine = activity.findViewById(R.id.cutter_topView);
        this.bottomLine = activity.findViewById(R.id.cutter_bottomView);
        this.leftLine = activity.findViewById(R.id.cutter_leftView);
        this.rightLine = activity.findViewById(R.id.cutter_rightView);

        this.lineWidth = this.topLine.getLayoutParams().height;
    }

    public void prepare(MyRectangle cutRectangle, MyRectangle imageRectangle, int originalWidth, int originalHeight){
        this.cutRectangle = cutRectangle;
        this.imageRectangle = imageRectangle;
        this.originalWidth = originalWidth;
        this.originalHeight = originalHeight;

        this.downTopY = cutRectangle.getCornerA().getY();
        this.downBottomY = cutRectangle.getCornerB().getY();
        this.downLeftX = cutRectangle.getCornerA().getX();
        this.downRightX = cutRectangle.getCornerB().getX();

        setBottom(
                mapping(this.originalHeight, this.imageRectangle.getHeight(), this.cutRectangle.getCornerB().getY())
                        + this.imageRectangle.getCornerA().getY()
        );
        this.bottomLine.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                moveBottom(event);
                return true;
            }
        });
        setTop(
                mapping(this.originalHeight, this.imageRectangle.getHeight(), this.cutRectangle.getCornerA().getY())
                        + this.imageRectangle.getCornerA().getY() - this.lineWidth
        );
        this.topLine.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                moveTop(event);
                return true;
            }
        });
        setRight(
                mapping(this.originalWidth, this.imageRectangle.getWidth(), this.cutRectangle.getCornerB().getX())
                        + this.imageRectangle.getCornerA().getX()
        );
        this.rightLine.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                moveRight(event);
                return true;
            }
        });
        setLeft(
                mapping(this.originalWidth, this.imageRectangle.getWidth(), this.cutRectangle.getCornerA().getX())
                        + this.imageRectangle.getCornerA().getX() - this.lineWidth
        );
        this.leftLine.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                moveLeft(event);
                return true;
            }
        });

    }

    private int mapping(int from, int to, int value){
        double ration = (double) from / to;
        return (int) Math.round(value / ration);
    }

    private void setTop(float newY){
        if(newY < this.imageRectangle.getCornerA().getY() - this.lineWidth){
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
        if(newY > this.imageRectangle.getCornerB().getY()){
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
                setBottom(newY);
                break;
            case MotionEvent.ACTION_UP:
                int cutPosition = (int) this.bottomLine.getY() - this.imageRectangle.getCornerA().getY();
                this.cutRectangle.getCornerB().setY(
                        mapping(this.imageRectangle.getHeight(), this.originalHeight, cutPosition)
                );
        }
    }

    private void setLeft(float newX){
        if(newX < this.imageRectangle.getCornerA().getX() - this.lineWidth){
            newX = this.imageRectangle.getCornerA().getX() - this.lineWidth;
        }else if(this.rightLine.getX() - newX < MIN_CUT_SIDE) {
            newX = this.rightLine.getX() - MIN_CUT_SIDE;
        }
        this.leftLine.setX(newX);

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
        if(newX > this.imageRectangle.getCornerB().getX()){
            newX = this.imageRectangle.getCornerB().getX();
        }else if(newX - this.leftLine.getX() < MIN_CUT_SIDE) {
            newX = this.leftLine.getX() + MIN_CUT_SIDE;
        }
        this.rightLine.setX(newX);

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
                setRight(newX);
                break;
            case MotionEvent.ACTION_UP:
                int cutPosition = (int) this.rightLine.getX() - this.imageRectangle.getCornerA().getX();
                this.cutRectangle.getCornerB().setX(
                        mapping(this.imageRectangle.getWidth(), this.originalWidth, cutPosition)
                );
        }
    }

    private void setCutRectangleHeight(){
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) this.leftLine.getLayoutParams();
        int newHeight = Math.round(this.bottomLine.getY() - this.topLine.getY() + this.lineWidth);
        params.height = newHeight;
        this.leftLine.setLayoutParams(params);
        params = (RelativeLayout.LayoutParams) this.rightLine.getLayoutParams();
        params.height = newHeight;
        this.rightLine.setLayoutParams(params);
    }

    private void setCutRectangleWidth(){
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) this.topLine.getLayoutParams();
        int newWidth = Math.round(this.rightLine.getX() - this.leftLine.getX() + this.lineWidth);
        params.width = newWidth;
        this.topLine.setLayoutParams(params);
        params = (RelativeLayout.LayoutParams) this.bottomLine.getLayoutParams();
        params.width = newWidth;
        this.bottomLine.setLayoutParams(params);
    }
}

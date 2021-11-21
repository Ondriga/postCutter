package com.example.postcutter.customViews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.example.postcutter.R;

import postCutter.geometricShapes.Coordinate;
import postCutter.geometricShapes.rectangle.MyRectangle;

public class RectangleView extends FrameLayout {

    private MyRectangle rectangle = null;

    private View innerRectangle;
    private View topLeftCorner;
    private View topRightCorner;
    private View bottomLeftCorner;
    private View bottomRightCorner;

    public RectangleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(getContext(), R.layout.rectangle_view, this);
    }

    //TODO debug
    private void moveTop(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                System.out.println("Action_down position==========================");
                System.out.println(event.getX() + ", " + event.getY());
                break;
//            case MotionEvent.ACTION_MOVE:
//                float movedY = event.getY();
//                float distanceY = movedY - this.downTopY;
//                float newY = this.topLine.getY() + distanceY;
//                if(suggestLine) {
//                    float tmp = this.nearBreakPoint(newY, this.breakPoints);
//                    float tmp = this.nearBreakPoint(newY, this.breakPoints);
//                    if (tmp != newY) {
//                        newY = tmp - this.lineWidth;
//                    }
//                }
//                setTop(newY);
//                break;
//            case MotionEvent.ACTION_UP:
//                int cutPosition = (int) this.topLine.getY() - this.imageRectangle.getCornerA().getY() + this.lineWidth;
//                this.cutRectangle.getCornerA().setY(
//                        mapping(this.imageRectangle.getHeight(), this.originalHeight, cutPosition)
//                );
        }
    }

    public void setRectangle(Coordinate cornerA, Coordinate cornerB) {
        int shift = (int) getResources().getDimension(R.dimen.rectangle_innerRectangleMargin);
        shift += (int) getResources().getDimension(R.dimen.rectangle_border_width);
        cornerA.setX(cornerA.getX() + shift);
        cornerA.setY(cornerA.getY() + shift);
        cornerB.setX(cornerB.getX() - shift);
        cornerB.setY(cornerB.getY() - shift);
        this.rectangle = MyRectangle.createRectangle(cornerA, cornerB);

        prepairViews();
    }

    private void prepairViews() {
        innerRectangle = findViewById(R.id.rectangle_innerRectangle);
        topLeftCorner = findViewById(R.id.rectangle_topLeftCorner);
        topRightCorner = findViewById(R.id.rectangle_topRightCorner);
        bottomLeftCorner = findViewById(R.id.rectangle_bottomLeftCorner);
        bottomRightCorner = findViewById(R.id.rectangle_bottomRightCorner);

        //TODO debug
        this.bottomRightCorner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                moveTop(event);
                return true;
            }
        });
    }

    public MyRectangle getRectangle() {
        return rectangle;
    }

    public View getInnerRectangle() {
        return innerRectangle;
    }

    public View getTopLeftCorner() {
        return topLeftCorner;
    }

    public View getTopRightCorner() {
        return topRightCorner;
    }

    public View getBottomLeftCorner() {
        return bottomLeftCorner;
    }

    public View getBottomRightCorner() {
        return bottomRightCorner;
    }
}

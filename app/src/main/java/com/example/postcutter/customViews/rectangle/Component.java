package com.example.postcutter.customViews.rectangle;

import android.view.MotionEvent;
import android.view.View;

public class Component {
    private final View view;
    protected CoordinateFloat coordinate = new CoordinateFloat(0, 0);

    public Component(View view) {
        this.view = view;
    }

    public CoordinateFloat moveAction(MotionEvent event, CoordinateFloat oldCoordinate) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                this.coordinate.setX(event.getX());
                this.coordinate.setY(event.getY());
                return null;
            case MotionEvent.ACTION_MOVE:
                float newY = oldCoordinate.getY() + this.getYDistance(event.getY());
                float newX = oldCoordinate.getX() + this.getXDistance(event.getX());
                return new CoordinateFloat(newX, newY);
        }
        return null;
    }

    protected float getXDistance(float movedX) {
        return movedX - this.coordinate.getX();
    }

    protected float getYDistance(float movedY) {
        return movedY - this.coordinate.getY();
    }

    public View getView() {
        return view;
    }
}

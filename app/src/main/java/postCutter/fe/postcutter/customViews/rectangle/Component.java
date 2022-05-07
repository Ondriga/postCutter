/*
 * Source code for the frontend of Bachelor thesis.
 * Component class
 *
 * (C) Patrik Ondriga (xondri08)
 */

package postCutter.fe.postcutter.customViews.rectangle;

import android.view.MotionEvent;
import android.view.View;

/**
 * This class representing component for crop/replace frame.
 */
public class Component {
    /// UI View of this component.
    private final View view;
    /// When user move with this component, this contain the pressed position after click.
    protected CoordinateFloat coordinate = new CoordinateFloat(0, 0);

    /**
     * Constructor.
     *
     * @param view UI View of this component.
     */
    public Component(View view) {
        this.view = view;
    }

    /**
     * Representing move action. Cache press event and store the coordinates of pressed place. On move event
     * calculating new coordinates for the View.
     *
     * @param event         trigger move action.
     * @param oldCoordinate old coordinates of View before it was pressed.
     * @return new coordinates for View if the event was move, otherwise null.
     */
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

    /**
     * Calculate the move distance in X direction.
     *
     * @param movedX new X value.
     * @return distance between old X and new X.
     */
    protected float getXDistance(float movedX) {
        return movedX - this.coordinate.getX();
    }

    /**
     * Calculate the move distance in Y direction.
     *
     * @param movedY new Y value.
     * @return distance between old Y and new Y.
     */
    protected float getYDistance(float movedY) {
        return movedY - this.coordinate.getY();
    }

    /**
     * Getter for View.
     *
     * @return View of component.
     */
    public View getView() {
        return view;
    }
}

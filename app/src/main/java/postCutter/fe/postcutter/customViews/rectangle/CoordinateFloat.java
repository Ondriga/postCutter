/*
 * Source code for the frontend of Bachelor thesis.
 * CoordinateFloat class
 *
 * (C) Patrik Ondriga (xondri08)
 */

package postCutter.fe.postcutter.customViews.rectangle;

/**
 * Representing x and y position on display.
 */
public class CoordinateFloat {
    /// Float X value of coordinate.
    private float x;
    /// Float Y value of coordinate.
    private float y;

    /**
     * Constructor.
     *
     * @param x value of coordinate.
     * @param y value of coordinate.
     */
    public CoordinateFloat(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Getter for X.
     *
     * @return X value of coordinate.
     */
    public float getX() {
        return x;
    }

    /**
     * Setter for X value of coordinate.
     *
     * @param x new X value.
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * Getter for Y.
     *
     * @return Y value of coordinate.
     */
    public float getY() {
        return y;
    }

    /**
     * Setter for Y value of coordinate.
     *
     * @param y new Y value.
     */
    public void setY(float y) {
        this.y = y;
    }
}

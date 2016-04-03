package nl.food4bees.beedroid;

import java.io.Serializable;

public class Point implements Serializable {
    private double mX;
    private double mY;

    public Point(double x, double y) {
        mX = x;
        mY = y;
    }

    public void setX(double x) {
        mX = x;
    }

    public void setY(double y) {
        mY = y;
    }

    public double getX() {
        return mX;
    }

    public double getY() {
        return mY;
    }

    public String toString() {
        return mX + new String(", ") + mY;
    }
}

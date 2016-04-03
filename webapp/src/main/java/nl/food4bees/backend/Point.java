package nl.food4bees.backend;

import java.util.Locale;

public class Point
{
    private double x_;
    private double y_;

    public Point(double x, double y)
    {
        x_ = x;
        y_ = y;
    }

    public void setX(double x)
    {
        x_ = x;
    }

    public void setY(double y)
    {
        y_ = y;
    }

    public double getX()
    {
        return x_;
    }

    public double getY()
    {
        return y_;
    }

    public String toString()
    {
        return String.format(Locale.US, "(%f,%f)", x_, y_);
    }
}

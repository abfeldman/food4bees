package nl.food4bees.beedroid;

import java.io.Serializable;

import java.util.Stack;

public class Polygon implements Serializable {
    private Stack<Point> mPoints;

    public Polygon() {
        this.mPoints = new Stack<Point>();
    }

    public Polygon(Stack<Point> points) {
        this.mPoints = points;
    }

    public int size() {
        return mPoints.size();
    }

    public void addPoint(Point point) {
        mPoints.push(point);
    }

    public Point getPoint(int index) {
        return mPoints.get(index);
    }

    public String toString() {
        int j = mPoints.size();

        String result = null;

        for (int i = 0; i < j; i++) {
            if (result == null) {
                result = mPoints.get(i).toString();

                continue;
            }
            result += "; ";
            result += mPoints.get(i);
        }

        return result;
    }
}

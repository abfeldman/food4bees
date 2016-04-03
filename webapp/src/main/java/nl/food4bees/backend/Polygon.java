package nl.food4bees.backend;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Stack;

public class Polygon
{
    public class ParsingException extends Exception
    {
    }

    private Stack<Point> points_;

    public Polygon()
    {
        points_ = new Stack<Point>();
    }

    public Polygon(Stack<Point> points)
    {
        points_ = points;
    }

    public int size()
    {
        return points_.size();
    }

    public void addPoint(Point point)
    {
        points_.push(point);
    }

    public Point getPoint(int index)
    {
        return points_.get(index);
    }

    public String toString()
    {
        StringBuilder builder = new StringBuilder("(");
        int j = points_.size();
        for (int i = 0; i < j; i++) {
            Point p = points_.get(i);
            if (i != 0) {
                builder.append(",");
            }
            builder.append(p.toString());
        }
        builder.append(")");
        return builder.toString();
    }
}

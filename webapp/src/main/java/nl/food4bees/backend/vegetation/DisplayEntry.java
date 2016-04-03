package nl.food4bees.backend.vegetation;

import nl.food4bees.backend.Polygon;

import java.util.Date;

public class DisplayEntry
{
    private Date time;
    private String user;
    private String plant;
    private Polygon area;

    public DisplayEntry(Date time, String user, String plant, Polygon area) throws Polygon.ParsingException
    {
        this.time = time;
        this.user = user;
        this.plant = plant;
        this.area = area;
    }

    public void setTime(Date time)
    {
        this.time = time;
    }

    public Date getTime()
    {
        return this.time;
    }

    public void setUser(String user)
    {
        this.user = user;
    }

    public String getUser()
    {
        return this.user;
    }

    public void setPlant(String plant)
    {
        this.plant = plant;
    }

    public String getPlant()
    {
        return this.plant;
    }

    public void setArea(Polygon area)
    {
        this.area = area;
    }

    public Polygon getArea()
    {
        return this.area;
    }
}

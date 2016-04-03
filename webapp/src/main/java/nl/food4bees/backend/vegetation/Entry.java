package nl.food4bees.backend.vegetation;

import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.food4bees.backend.Point;
import nl.food4bees.backend.Polygon;

public class Entry
{
    private final Logger logger = LoggerFactory.getLogger(Entry.class);
    
    private Integer id;
    private Integer userId;
    private Integer plantId;
    private Double amount;
    private Date time;
    private Polygon area;

    public Entry()
    {
    }

    public Entry(Integer id, Integer userId, Integer plantId, Polygon area, Double amount, Date time)
    {
        this.id = id;
        this.userId = userId;
        this.plantId = plantId;
        this.area = area;
        this.amount = amount;
        this.time = time;
    }

    public void setTime(Date time)
    {
        this.time = time;
    }

    public Date getTime()
    {
        return this.time;
    }

    public void setArea(Polygon area)
    {
        this.area = area;
    }

    public Polygon getArea()
    {
        return this.area;
    }

    public Integer getUserId()
    {
        return userId;
    }

    public void setUserId(Integer userId)
    {
        this.userId = userId;
    }

    public Integer getPlantId()
    {
        return plantId;
    }

    public void setPlantId(Integer plantId)
    {
        this.plantId = plantId;
    }

    public Double getAmount()
    {
        return amount;
    }

    public void setAmount(Double amount)
    {
        this.amount = amount;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }
}

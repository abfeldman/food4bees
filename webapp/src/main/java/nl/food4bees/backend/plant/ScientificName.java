package nl.food4bees.backend.plant;

import java.util.Date;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import nl.food4bees.backend.Util;

public class ScientificName
{
    private Integer id;
    private String scientificName;

    public ScientificName(Integer id, String scientificName)
    {
        this.id = id;
        this.scientificName = scientificName;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getScientificName()
    {
        return scientificName;
    }

    public void setScientificName(String latin)
    {
        this.scientificName = scientificName;
    }
}

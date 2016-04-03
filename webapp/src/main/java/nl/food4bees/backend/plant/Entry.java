package nl.food4bees.backend.plant;

import java.text.ParseException;
import java.util.Date;

import nl.food4bees.backend.Util;

public class Entry
{
    private Integer id_;
    private int userId_;
    private String commonName_;
    private String scientificName_;
    private String description_;
    private String url_;
    private Integer color_;
    private Double height_;
    private Double nectar_;
    private Double pollen_;
    private Date start_;
    private Date end_;
    private int version_;
    
    public Entry(Integer id,
                 int userId,
                 String commonName,
                 String scientificName,
                 String description,
                 String url,
                 String color,
                 String height,
                 String nectar,
                 String pollen,
                 String start,
                 String end,
                 int version)
        throws ParseException
    {
        id_ = id;
        userId_ = userId;
        commonName_ = Util.trim(commonName);
        scientificName_ = scientificName;
        description_ = Util.trim(description);
        url_ = Util.trim(url);
        color_ = Util.parseInteger(color, 16);
        height_ = Util.parseDouble(height);
        nectar_ = Util.parseDouble(nectar);
        pollen_ = Util.parseDouble(pollen);
        start_ = Util.parseDate(start);
        end_ = Util.parseDate(end);
        version_ = version;
    }

    public Entry(int id,
                 int userId,
                 String commonName,
                 String scientificName,
                 String description,
                 String url,
                 Integer color,
                 Double height,
                 Double nectar,
                 Double pollen,
                 Date start,
                 Date end,
                 int version)
    {
        id_ = id;
        userId_ = userId;
        commonName_ = commonName;
        scientificName_ = scientificName;
        description_ = description;
        url_ = url;
        color_ = color;
        height_ = height;
        nectar_ = nectar;
        pollen_ = pollen;
        start_ = start;
        end_ = end;
        version_ = version;
    }

    public Integer getId()
    {
        return id_;
    }

    public void setId(Integer id)
    {
        id_ = id;
    }

    public int getUserId()
    {
        return userId_;
    }

    public void setUserId(int userId)
    {
        userId_ = userId;
    }

    public String getCommonName()
    {
        return commonName_;
    }

    public void setCommonName(String commonName)
    {
        commonName_ = commonName;
    }

    public String getScientificName()
    {
        return scientificName_;
    }

    public void setScientificName(String scientificName)
    {
        scientificName_ = scientificName;
    }

    public String getDescription()
    {
        return description_;
    }

    public void setDescription(String description)
    {
        description_ = description;
    }

    public String getUrl()
    {
        return url_;
    }

    public void setUrl(String url)
    {
        url_ = url;
    }

    public Integer getColor()
    {
        return color_;
    }

    public void setColor(Integer color)
    {
        color_ = color;
    }

    public Double getHeight()
    {
        return height_;
    }

    public void setHeight(Double height)
    {
        height_ = height;
    }

    public Double getNectar()
    {
        return nectar_;
    }

    public void setNectar(Double nectar)
    {
        nectar_ = nectar;
    }

    public Double getPollen()
    {
        return pollen_;
    }

    public void setPollen(Double pollen)
    {
        pollen_ = pollen;
    }

    public Date getStart()
    {
        return start_;
    }

    public void setStart(Date start)
    {
        start_ = start;
    }

    public Date getEnd()
    {
        return end_;
    }

    public void setEnd(Date end)
    {
        end_ = end;
    }

    public int getVersion()
    {
        return version_;
    }

    public void setVersion(int version)
    {
        version_ = version;
    }
}

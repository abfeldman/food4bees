package nl.food4bees.backend.plant_image;

public class Entry
{
    private Integer id;
    private Integer plantId;
    private String caption;
    private byte[] image;
    private Integer size;
    
    public Entry(Integer id, Integer plantId, String caption, byte[] image)
    {
        this.id = id;
        this.plantId = plantId;
        this.caption = caption;
        this.image = image;

        if (image != null && image.length != 0) {
            size = image.length;
        } else {
            size = 0;
        }
    }

    public Entry(Integer id, Integer plantId, String caption, Integer size)
    {
        this.id = id;
        this.plantId = plantId;
        this.caption = caption;
        this.image = null;
        this.size = size;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getPlantId()
    {
        return plantId;
    }

    public void setPlantId(Integer plantId)
    {
        this.plantId = plantId;
    }

    public String getCaption()
    {
        return caption;
    }

    public void setCaption(String caption)
    {
        this.caption = caption;
    }

    public byte[] getImage()
    {
        return image;
    }

    public void setImage(byte[] image)
    {
        this.image = image;

        if (image != null && image.length != 0) {
            size = image.length;
        }
    }

    public Integer getSize()
    {
        return size;
    }
}

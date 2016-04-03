package nl.food4bees.backend;

public class Statistics
{
    private Integer users;
    private Integer plants;
    private Integer plantImages;
    private Integer vegetationRecords;

    public Statistics(Integer users,
                      Integer plants,
                      Integer plantImages,
                      Integer vegetationRecords)
    {
        this.users = users;
        this.plants = plants;
        this.plantImages = plantImages;
        this.vegetationRecords = vegetationRecords;
    }

    public void setUsers(Integer users)
    {
        this.users = users;
    }

    public Integer getUsers()
    {
        return users;
    }

    public void setPlants(Integer plants)
    {
        this.plants = plants;
    }

    public Integer getPlants()
    {
        return plants;
    }

    public void setPlantImages(Integer plantImages)
    {
        this.plantImages = plantImages;
    }

    public Integer getPlantImages()
    {
        return plantImages;
    }

    public void setVegetationRecords(Integer vegetationRecords)
    {
        this.vegetationRecords = vegetationRecords;
    }

    public Integer getVegetationRecords()
    {
        return vegetationRecords;
    }
}

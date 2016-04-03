package nl.food4bees.backend.group;

public class Entry
{
    private Integer id_;
    private String name_;

    public Entry(Integer id, String name)
    {
        id_ = id;
        name_ = name;
    }

    public Integer getId()
    {
        return id_;
    }

    public void setId(Integer id)
    {
        id_ = id;
    }

    public String getName()
    {
        return name_;
    }

    public void setName(String name)
    {
        name_ = name;
    }
}

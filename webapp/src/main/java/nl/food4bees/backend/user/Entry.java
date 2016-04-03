package nl.food4bees.backend.user;

public class Entry
{
    private Integer id_;
    private String name_;
    private String email_;
    private String password_;
    private int group_;

    public Entry(Integer id,
                 String name,
                 String email,
                 String password,
                 int group)
    {
        id_ = id;
        name_ = name;
        email_ = email;
        password_ = password;
        group_ = group;
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

    public String getEmail()
    {
        return email_;
    }

    public void setEmail(String email)
    {
        email_ = email;
    }

    public String getPassword()
    {
        return password_;
    }

    public void setPassword(String password)
    {
        password_ = password;
    }

    public int getGroup()
    {
        return group_;
    }

    public void setGroup(int group)
    {
        group_ = group;
    }
}

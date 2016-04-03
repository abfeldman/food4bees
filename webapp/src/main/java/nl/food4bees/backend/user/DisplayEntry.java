package nl.food4bees.backend.user;

public class DisplayEntry extends Entry
{
    private String groupName_;    

    public DisplayEntry(Integer id,
                        String name,
                        String email,
                        String password,
                        int group,
                        String groupName)
    {
        super(id, name, email, password, group);

        groupName_ = groupName;
    }

    public String getGroupName()
    {
        return groupName_;
    }

    public void setGroupName(String groupName)
    {
        groupName_ = groupName;
    }
}

package nl.food4bees.backend.user;

public class LoginCredentials
{
    private Integer userId_;
    private String groupName_;

    public LoginCredentials(Integer userId,
                            String groupName)
    {
        userId_ = userId;
        groupName_ = groupName;
    }

    public Integer getUserId()
    {
        return userId_;
    }

    public void setUserId(Integer userId)
    {
        userId_ = userId;
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

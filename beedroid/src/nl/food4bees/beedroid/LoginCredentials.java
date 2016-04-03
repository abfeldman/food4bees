package nl.food4bees.beedroid;

public class LoginCredentials
{
    static private String mEmail;
    static private String mPassword;

    LoginCredentials(String email, String password) {
        mEmail = email;
        mPassword = password;
    }

    void setEmail(String email) {
        mEmail = email;
    }

    String getEmail() {
        return mEmail;
    }

    void setPassword(String password) {
        mPassword = password;
    }

    String getPassword() {
        return mPassword;
    }
}

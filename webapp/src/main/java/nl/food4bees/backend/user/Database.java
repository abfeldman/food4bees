package nl.food4bees.backend.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import org.apache.commons.codec.binary.Base64;

public class Database extends nl.food4bees.backend.Database
{
    Database() throws SQLException
    {
    }

    public Integer add(Entry user)
        throws SQLException, NoSuchAlgorithmException, InvalidKeySpecException
    {
        String password = user.getPassword();

        PasswordEncryptionService enc = new PasswordEncryptionService();

        byte[] salt = enc.generateSalt();
        byte[] encryptedPassword = enc.getEncryptedPassword(password, salt);

        password = Base64.encodeBase64String(salt) + "$" + Base64.encodeBase64String(encryptedPassword);

        String query = "INSERT INTO \"user\" (name, email, password, group_id) VALUES (?, ?, ?, ?)";

        PreparedStatement preparedStatement = connection_.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

        int index = 1;

        preparedStatement.setString(index++, user.getName());
        preparedStatement.setString(index++, user.getEmail());
        preparedStatement.setString(index++, password);
        preparedStatement.setInt(index, user.getGroup());

        preparedStatement.executeUpdate();

        ResultSet primKeySet = preparedStatement.getGeneratedKeys();
        if (primKeySet.next()) {
            return primKeySet.getInt(1);
        }
            
        assert(false);

        return null;
    }

    public Entry get(Integer id) throws SQLException
    {
        Entry user = null;

        Statement statement = connection_.createStatement();

        String query = String.format("SELECT id, name, email, password, group_id FROM \"user\" WHERE id = %d", id);

        ResultSet resultSet = statement.executeQuery(query);
        if (resultSet.next()) {
            user = new Entry(resultSet.getInt("id"),
                             resultSet.getString("name"),
                             resultSet.getString("email"),
                             resultSet.getString("password"),
                             resultSet.getInt("group_id"));
        }

        return user;
    }

    public void edit(Entry user) throws SQLException
    {
        String query = "UPDATE \"user\" SET name = ?, email = ?, password = ?, group_id = ? WHERE id = ?";

        PreparedStatement preparedStatement = connection_.prepareStatement(query);

        int index = 1;

        preparedStatement.setString(index++, user.getName());
        preparedStatement.setString(index++, user.getEmail());
        preparedStatement.setString(index++, user.getPassword());
        preparedStatement.setInt(index++, user.getGroup());
        preparedStatement.setInt(index, user.getId());

        preparedStatement.executeUpdate();
    }

    public void delete(Integer id) throws SQLException
    {
        Statement statement = connection_.createStatement();

        startTransaction();
        try {
            statement.executeUpdate(String.format("DELETE FROM vegetation WHERE user_id = %d", id));
            statement.executeUpdate(String.format("DELETE FROM plant_image WHERE plant_id IN (SELECT id FROM plant WHERE user_id = %d)", id));
            statement.executeUpdate(String.format("DELETE FROM plant WHERE user_id = %d", id));
            statement.executeUpdate(String.format("DELETE FROM email_verification_token WHERE user_id = %d", id));
            statement.executeUpdate(String.format("DELETE FROM \"user\" WHERE id = %d", id));

            commit();
        } catch (Exception e) {
            rollback();

            throw e;
        } finally {
            endTransaction();
        }
    }

    public List<DisplayEntry> list() throws SQLException
    {
        List<DisplayEntry> list = new ArrayList<DisplayEntry>();

        Statement statement = connection_.createStatement();

        ResultSet resultSet = statement.executeQuery("SELECT \"user\".id, \"user\".name, email, password, group_id, \"group\".name AS group_name FROM \"user\", \"group\" WHERE \"user\".group_id = \"group\".id ORDER BY \"user\".name, email");
        while (resultSet.next()) {
            DisplayEntry user = new DisplayEntry(resultSet.getInt("id"),
                                                 resultSet.getString("name"),
                                                 resultSet.getString("email"),
                                                 resultSet.getString("password"),
                                                 resultSet.getInt("group_id"),
                                                 resultSet.getString("group_name"));
            list.add(user);
        }

        return list;
    }

    public List<Email> getEmailList() throws SQLException
    {
        List<Email> list = new ArrayList<Email>();

        Statement statement = connection_.createStatement();

        ResultSet resultSet = statement.executeQuery("SELECT id, email FROM \"user\"");
        while (resultSet.next()) {
            Email email = new Email(resultSet.getInt("id"),
                                    resultSet.getString("email"));

            list.add(email);
        }

        return list;
    }

    public Integer getId(String email) throws SQLException
    {
        Statement statement = connection_.createStatement();

        String query = String.format("SELECT id FROM \"user\" WHERE email = '%s'", email);

        ResultSet resultSet = statement.executeQuery(query);
        if (resultSet.next()) {
            return resultSet.getInt("id");
        }

        return null;
    }

    public LoginCredentials getLoginCredentials(String email, String password)
        throws SQLException, NoSuchAlgorithmException, InvalidKeySpecException
    {
        Statement statement = connection_.createStatement();

        byte[] salt = getSalt(email);

        PasswordEncryptionService enc = new PasswordEncryptionService();

        byte[] encryptedPassword = enc.getEncryptedPassword(password, salt);

        password = Base64.encodeBase64String(salt) + "$" + Base64.encodeBase64String(encryptedPassword);

        String query = String.format("SELECT \"user\".id AS user_id, \"group\".name AS group_name FROM \"user\", \"group\" WHERE group_id = \"group\".id AND email = '%s' AND password = '%s' AND verified = TRUE",
                                     email,
                                     password);

        ResultSet resultSet = statement.executeQuery(query);
        if (resultSet.next()) {
            return new LoginCredentials(resultSet.getInt("user_id"),
                                        resultSet.getString("group_name"));
        }

        return null;
    }

    public boolean hasUser(String email) throws SQLException
    {
        Statement statement = connection_.createStatement();

        String query = String.format("SELECT id FROM \"user\" WHERE email = '%s'", email);

        ResultSet resultSet = statement.executeQuery(query);
        if (resultSet.next()) {
            return true;
        }

        return false;
    }

    public void addVerificationToken(Integer id, String token) throws SQLException
    {
        assert(id != null);

        Statement statement = connection_.createStatement();

        String query = String.format("INSERT INTO email_verification_token (user_id, token) VALUES (%d, '%s');", id, token);

        statement.executeUpdate(query);
    }

    public Integer findToken(String token) throws SQLException
    {
        if (token == null) {
            return null;
        }

        Statement statement = connection_.createStatement();

        String query = String.format("SELECT user_id FROM email_verification_token WHERE token = '%s'", token);

        ResultSet resultSet = statement.executeQuery(query);
        if (resultSet.next()) {
            return resultSet.getInt("user_id");
        }

        return null;
    }

    public void deleteToken(String token) throws SQLException
    {
        Statement statement = connection_.createStatement();

        String query = String.format("DELETE FROM email_verification_token WHERE token = '%s'", token);

        statement.executeUpdate(query);
    }

    public void enableUser(Integer id) throws SQLException
    {
        Statement statement = connection_.createStatement();

        String query = String.format("UPDATE \"user\" SET verified = TRUE WHERE id = %d", id);

        statement.executeUpdate(query);
    }

    public void updatePassword(Integer id, String password)
        throws SQLException, NoSuchAlgorithmException, InvalidKeySpecException
    {
        Statement statement = connection_.createStatement();

        PasswordEncryptionService enc = new PasswordEncryptionService();

        byte[] salt = enc.generateSalt();
        byte[] encryptedPassword = enc.getEncryptedPassword(password, salt);

        password = Base64.encodeBase64String(salt) + "$" + Base64.encodeBase64String(encryptedPassword);

        String query = String.format("UPDATE \"user\" SET password = '%s' WHERE id = %d", password, id);

        statement.executeUpdate(query);
    }

    public byte[] getSalt(String email) throws SQLException
    {
        Statement statement = connection_.createStatement();

        String query = String.format("SELECT password FROM \"user\" WHERE email = '%s'", email);

        ResultSet resultSet = statement.executeQuery(query);
        if (!resultSet.next()) {
            return null;
        }

        String password = resultSet.getString("password");
        String[] parts = password.split("\\$");
        if (parts.length != 2) {
            return null;
        }

        return Base64.decodeBase64(parts[0]);
    }
}

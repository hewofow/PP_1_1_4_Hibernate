package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private Connection connection;

    public UserDaoJDBCImpl() {
    }

    private void getConnection() {
        connection = Util.getConnection();
    }

    private void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createUsersTable() {
        getConnection();

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS users (" +
                    " id INT AUTO_INCREMENT," +
                    " name VARCHAR(30)," +
                    " lastName VARCHAR(30)," +
                    " age TINYINT," +
                    " PRIMARY KEY(id)" +
                    ");");
            connection.commit();
            System.out.println("Таблица создана в базе данных (или уже присутствовала в ней)");
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.addSuppressed(e);
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    public void dropUsersTable() {
        getConnection();

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("DROP TABLE IF EXISTS users");
            connection.commit();
            System.out.println("Таблица удалена из базы данных (либо уже отсутствовала в ней)");
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.addSuppressed(e);
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        getConnection();

        try (PreparedStatement ps = connection.prepareStatement("INSERT INTO users (name, lastName, age) VALUES (?, ?, ?)")) {
            ps.setString(1, name);
            ps.setString(2, lastName);
            ps.setByte(3, age);
            ps.executeUpdate();
            connection.commit();
            System.out.println("User с именем " + name + " добавлен в базу данных.");
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.addSuppressed(e);
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    public void removeUserById(long id) {
        getConnection();

        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM users WHERE id = ?")) {
            ps.setLong(1, id);
            ps.executeUpdate();
            connection.commit();
            System.out.println("User с id = " + id + " удалён");
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.addSuppressed(e);
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    public List<User> getAllUsers() {
        getConnection();
        List<User> list = new ArrayList<>();

        try (Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery("SELECT * FROM users");

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setName(rs.getString("name"));
                user.setLastName(rs.getString("lastName"));
                user.setAge(rs.getByte("age"));
                list.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }

        return list;
    }

    public void cleanUsersTable() {
        getConnection();

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("TRUNCATE TABLE users");
            connection.commit();
            System.out.println("Таблица очищена");
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.addSuppressed(e);
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }
}

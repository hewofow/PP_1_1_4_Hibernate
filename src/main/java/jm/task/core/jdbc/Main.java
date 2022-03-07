package jm.task.core.jdbc;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        UserService us = new UserServiceImpl();
        us.createUsersTable();
        us.saveUser("Vasiliy", "Vasilenko", (byte) 10);
        us.saveUser("Ivan", "Ivanov", (byte) 15);
        us.saveUser("Petya", "Petrov", (byte) 20);
        us.saveUser("Boba", "Biba", (byte) 25);

        List<User> list = us.getAllUsers();
        for (User user : list) {
            System.out.println(user);
        }

        us.cleanUsersTable();
        us.dropUsersTable();
    }
}

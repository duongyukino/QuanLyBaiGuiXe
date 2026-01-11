package OOP.Parking.controller;

import OOP.Parking.DAO.UserDAO;
import OOP.Parking.model.User;

import java.io.IOException;
import java.util.List;

public class UserController {
    private final UserDAO userDAO;

    public UserController() throws IOException {
        this.userDAO = UserDAO.getInstance();
    }

    public List<User> getAllUsers() {
        // UserDAO.getAll() không ném IOException vì nó đọc từ Map trong bộ nhớ
        return userDAO.getAll();
    }

    public boolean addUser(User user) {
        try {
            return userDAO.add(user);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateUser(User user) {
        try {
            return userDAO.update(user);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteUser(String username) {
        try {
            return userDAO.delete(username);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
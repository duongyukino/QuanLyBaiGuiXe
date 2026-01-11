package OOP.Parking.service.impl;

import OOP.Parking.DAO.UserDAO;
import OOP.Parking.model.User;
import OOP.Parking.service.AuthenticationService;

import java.io.IOException;

public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserDAO userDAO;
    private User currentUser;

    public AuthenticationServiceImpl() throws IOException {
        this.userDAO = UserDAO.getInstance();
    }

    @Override
    public User login(String username, String password) {
        // authenticate() không ném IOException
        User user = userDAO.authenticate(username, password);
        if (user != null) {
            this.currentUser = user;
            return user;
        }
        return null;
    }

    @Override
    public void logout() {
        this.currentUser = null;
    }

    @Override
    public User getCurrentUser() {
        return currentUser;
    }

    @Override
    public boolean changePassword(String username, String oldPassword, String newPassword) {
        // getById() không ném IOException
        User user = userDAO.getById(username);
        
        if (user != null && user.authenticate(oldPassword)) {
            user.setPassword(newPassword);
            try {
                // update() CÓ ném IOException vì nó ghi file
                return userDAO.update(user);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }
}
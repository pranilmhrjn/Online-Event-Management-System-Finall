package controller;

import model.UserModel;

public class LoginController {

    private UserModel userModel = new UserModel();

    public String handleLogin(String username,
                              String password, String role) {
        if (username.isEmpty() || password.isEmpty()) return null;
        return userModel.login(username, password, role);
    }
}

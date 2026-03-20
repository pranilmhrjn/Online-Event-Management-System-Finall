package controller;

import model.UserModel;

public class SignupController {

    private UserModel userModel = new UserModel();

    public String handleSignup(String username, String email,
                               String password, String confirm,
                               String role) {
        if (role.equals("admin")) return "blocked_admin";
        if (role.equals("organizer")) return "blocked_organizer";
        if (username.isEmpty() || email.isEmpty() || password.isEmpty())
            return "empty";
        if (!password.equals(confirm)) return "mismatch";
        return userModel.register(username, email, password, role)
               ? "success" : "exists";
    }
}

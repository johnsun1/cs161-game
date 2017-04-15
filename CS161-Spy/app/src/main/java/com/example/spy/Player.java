package com.example.spy;

/**
 * Created by johns on 4/14/2017.
 */

public class Player {
    private String email;
    private String role;

    public Player(String email, String role) {
        this.email = email;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }


}

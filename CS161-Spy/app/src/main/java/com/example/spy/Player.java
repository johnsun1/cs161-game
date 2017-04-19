package com.example.spy;

/**
 * Created by johns on 4/14/2017.
 */

public class Player {
    private String email;
    private String role;
    private String ID;

    public Player() {

    }

    public Player(String email, String role, String ID) {
        this.email = email;
        this.role = role;
        this.ID = ID;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String s) { email = s; }

    public String getRole() {
        return role;
    }
    public void setRole(String s) { role = s; }

    public String getID() { return ID; }

}

package com.example.spy;

/**
 * Created by johns on 4/14/2017.
 */

public class Player {
    private String email;
    private String ID;

    public Player() {

    }

    public Player(String email, String ID) {
        this.email = email;
        this.ID = ID;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String s) { email = s; }

    public String getID() { return ID; }

}

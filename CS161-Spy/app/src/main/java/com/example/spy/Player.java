package com.example.spy;

/**
 * Created by johns on 4/14/2017.
 */

public class Player {
    private String email;
    private String game_code;

    public Player() {

    }

    public Player(String email, String game_code) {
        this.email = email;
        this.game_code = game_code;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String s) {
        email = s;
    }

    public void setCode(String c) {
        game_code = c;
    }

    public String getCode() {
        return game_code;
    }

}

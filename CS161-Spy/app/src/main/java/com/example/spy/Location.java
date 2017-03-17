package com.example.spy;

/**
 * Created by johnsun on 3/16/17.
 */

public class Location {
    private String name;
    private String role_1;
    private String role_2;
    private String role_3;
    private String role_4;

    public Location(String name, String role_1, String role_2, String role_3, String role_4) {
        this.name = name;
        this.role_1 = role_1;
        this.role_2 = role_2;
        this.role_3 = role_3;
        this.role_4 = role_4;
    }

    public String getLocation() {
        return name;
    }

    public String getRole_1() {
        return role_1;
    }

    public String getRole_2() {
        return role_2;
    }

    public String getRole_3() {
        return role_3;
    }

    public String getRole_4() {
        return role_4;
    }
}

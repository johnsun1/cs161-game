package com.example.spy;

import java.util.ArrayList;

/**
 * Created by johnsun on 3/16/17.
 */

public class Location {
    private String name;
    private String role_1;
    private String role_2;
    private String role_3;
    private String role_4;
    private ArrayList<String> roles;

    public Location(String name, String role_1, String role_2, String role_3, String role_4) {
        this.name = name;
        this.role_1 = role_1;
        this.role_2 = role_2;
        this.role_3 = role_3;
        this.role_4 = role_4;

        roles = new ArrayList<String>();
        roles.add(role_1);
        roles.add(role_2);
        roles.add(role_3);
        roles.add(role_4);
    }

    public String getLocationName() {
        return name;
    }

    public String getRole(Integer i) {
        return roles.get(i);
    }
}

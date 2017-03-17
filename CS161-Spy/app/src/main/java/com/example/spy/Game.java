package com.example.spy;

import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.Random;

import android.content.Intent;

public class Game extends AppCompatActivity {
    private ArrayList<Location> locations;
    private Intent extra;
    private String game_code;
    private ArrayList<String> players;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //Get game code
        extra = getIntent();
        game_code = extra.getStringExtra("game_code");

        //Instantiate locations
        locations.add(new Location("Artic Base", "Scientist", "Engineer", "Eskimo", "Soldier"));
        locations.add(new Location("Submarine", "Captain", "First Officer", "Cook", "Diver"));
        locations.add(new Location("Radio Shack", "IT Guy", "Radio Enthusiast", "Soccer Mom", "Leet Hacker"));
    }

    public Location randomLocation() {
        Random rand = new Random();
        return locations.get(rand.nextInt(3));
    }

    public Integer randomSpy() {
        return 0;
    }
}

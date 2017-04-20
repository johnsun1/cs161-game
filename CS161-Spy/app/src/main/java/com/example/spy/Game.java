package com.example.spy;

import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.Random;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Game extends AppCompatActivity implements View.OnClickListener {
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseUser user;

    private TextView location;
    private TextView role;
    private TextView desc;

    private Button back;

    private Intent extra;
    private Bundle data;

    private String spy;
    private String locale;

    private Location localeObj;
    private Integer currentPlayerNumber;
    private ArrayList<String> playerOrder;

    private String game_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //Firebase stuff
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        //Views
        role = (TextView) findViewById(R.id.label_role);
        location = (TextView) findViewById(R.id.label_location);
        desc = (TextView) findViewById(R.id.descriptionText);

        //Buttons
        back = (Button) findViewById(R.id.button3);

        //Attach a listener to the button
        findViewById(R.id.button3).setOnClickListener(this);

        //Retrieve data from previous activity
        extra = getIntent();
        data = extra.getBundleExtra("game_data");
        spy = data.getString("spy");
        locale = data.getString("location");
        playerOrder = data.getStringArrayList("player_order");
        game_code = data.getString("game_code");

        //Establish what the current player's number is
        for (int i = 0; i < playerOrder.size(); i++) {
            if (playerOrder.get(i).equals(user.getEmail())) {
                //Player number is i
                currentPlayerNumber = i;
            }
        }

        //Establish what location players are at
        ArrayList<Location> loc = new ArrayList<Location>();

        //Add random locations to locale
        loc.add(new Location ("Arctic Base", "Captain", "Chief Scientist", "Intern", "Hunter"));
        loc.add(new Location ("Moon Base", "Scientist", "Geologist", "Chemist", "Engineer"));
        loc.add(new Location ("Submarine", "Captain", "First Officer", "Cook", "Janitor"));
        loc.add(new Location ("Silicon Valley Startup", "Hacker", "CEO", "Programmer", "Venture Capitalist"));
        loc.add(new Location ("European Castle", "Knight", "King", "Concubine", "Peasant"));
        loc.add(new Location ("London Subway Train", "Commuter", "Janitor", "Ticket Agent", "Security Guard"));
        loc.add(new Location ("Cafe in Athens", "Barista", "Actor", "Screenwriter", "College Student"));
        loc.add(new Location ("Cruise Ship", "Retired School Teacher", "Rich Girl", "Poor Hustler", "Crew"));
        loc.add(new Location ("Gas Station", "Manager", "Mechanic", "Truck Driver", "Cashier"));
        loc.add(new Location ("Library", "Librarian", "College Student", "Homeless Person", "Security Guard"));

        for (Location l : loc){
            if (l.getLocationName().equals(locale)) {
                localeObj = l;
            }
        }

        //Display a special UI if the user is a spy
        if (user.getEmail().equals(spy)) {
            role.setText("You are the spy. Don't get caught!");
            location.setText("Mystery Location");
            desc.setText("Welcome to Spy Fall. Keep your identity hidden for the length of the game, or figure out what the location is to win the game.");
        } else {
            role.setText(localeObj.getRole(currentPlayerNumber));
            location.setText(localeObj.getLocationName());
            desc.setText("Welcome to Spy Fall. Infer who is the spy before time runs out in order to win the game.");
        }
    }


    @Override
    public void onClick(View v) {
        //Stuff happens when buttons are clicked
        int id = v.getId();
        if (id == R.id.button3) {
            //Clean up old game lobby information
            myRef = database.getReference("lobby");
            myRef.child(game_code).removeValue(); //Remove all data associated with the current game

            //Return to the main lobby
            Intent main_lobby = new Intent(Game.this, MainActivity.class);
            startActivity(main_lobby);
        }
    }
}

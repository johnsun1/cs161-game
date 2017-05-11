package com.example.spy;

import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.Random;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

    private Location localeObj;

    private String game_code;

    private Random rand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        rand = new Random();

        //Firebase stuff
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        //load gif files
        ImageView imageView = (ImageView) findViewById(R.id.change_spy);
        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(imageView);
       // Glide.with(this).load(R.raw.change_spy).into(imageViewTarget);


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
        game_code = data.getString("game_code");
        spy = data.getString("spy_email");

        //Establish what location players are at
        ArrayList<Location> loc = new ArrayList<Location>();

        //Add random locations to locale
        loc.add(new Location ("Arctic Base", "Captain", "Chief Scientist", "Intern", "Hunter", spy));
        loc.add(new Location ("Moon Base", "Scientist", "Geologist", "Chemist", "Engineer", spy));
        loc.add(new Location ("Submarine", "Captain", "First Officer", "Cook", "Janitor", spy));
        loc.add(new Location ("Silicon Valley Startup", "Hacker", "CEO", "Programmer", "Venture Capitalist", spy));
        loc.add(new Location ("European Castle", "Knight", "King", "Concubine", "Peasant", spy));
        loc.add(new Location ("London Subway Train", "Commuter", "Janitor", "Ticket Agent", "Security Guard", spy));
        loc.add(new Location ("Cafe in Athens", "Barista", "Actor", "Screenwriter", "College Student", spy));
        loc.add(new Location ("Cruise Ship", "Retired School Teacher", "Rich Girl", "Poor Hustler", "Crew", spy));
        loc.add(new Location ("Gas Station", "Manager", "Mechanic", "Truck Driver", "Cashier", spy));
        loc.add(new Location ("Library", "Librarian", "College Student", "Homeless Person", "Security Guard", spy));


        localeObj = loc.get(rand.nextInt(9));

        myRef = database.getReference(game_code);
        myRef.setValue(localeObj);

        //Display a special UI if the user is a spy
        if (user.getEmail().equals(spy)) {
            role.setText("You are the spy. Don't get caught!");
            location.setText("Mystery Location");
            desc.setText("Welcome to Spy Fall. Keep your identity hidden for the length of the game, or figure out what the location is to win the game.");
        } else {
            role.setText(localeObj.getRole(rand.nextInt(3)));
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
            myRef = database.getReference().child(game_code).child("players").child(user.getUid());
            myRef.removeValue(); //Remove all data associated with the current game

            //Return to the main lobby
            Intent main_lobby = new Intent(Game.this, MainActivity.class);
            startActivity(main_lobby);
        }
    }
}

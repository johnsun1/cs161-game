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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GameJoin extends AppCompatActivity implements View.OnClickListener {
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

        //TODO: Pull location object from Firebase
        myRef.child("lobby").child(game_code).child("location").addValueEventListener( new ValueEventListener() {

            @Override
            public void onCancelled(DatabaseError error) {

            }

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    localeObj = dataSnapshot.getValue(Location.class);
                }
            }
        });

        myRef.child("lobby").child(game_code).child("spy").addValueEventListener( new ValueEventListener() {

            @Override
            public void onCancelled(DatabaseError error) {

            }

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    spy = (String) dataSnapshot.getValue(String.class);
                }
            }
        });

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
            myRef = database.getReference("lobby");
            myRef.child(game_code).child("players").child(user.getUid()).removeValue(); //Remove all data associated with the current game

            //Return to the main lobby
            Intent main_lobby = new Intent(GameJoin.this, MainActivity.class);
            startActivity(main_lobby);
        }
    }
}

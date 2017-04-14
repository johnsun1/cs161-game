package com.example.spy;

import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;

import android.widget.TextView;

import android.view.View;

public class GameLobby extends AppCompatActivity implements View.OnClickListener {
    private Intent extra;

    private String game_code;
    private String game_name;

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseUser user;

    ArrayList<FirebaseUser> players;

    private Bundle data;

    private TextView p1;
    private TextView p2;
    private TextView p3;
    private TextView p4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_lobby);

        //Retrieve extra information from game creation menu
        extra = getIntent();
        data = extra.getBundleExtra("game_data");
        game_code = data.getString("game_code");
        game_name = data.getString("game_name");

        //Views
        p1 = (TextView) findViewById(R.id.label_p1);
        p2 = (TextView) findViewById(R.id.label_p2);
        p3 = (TextView) findViewById(R.id.label_p3);
        p4 = (TextView) findViewById(R.id.label_p4);

        //Add all players into a data structure for easy handling
        players = new ArrayList<FirebaseUser>();

        //Buttons
        findViewById(R.id.button_back).setOnClickListener(this);

        //Get database reference
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();

        //Store game information
        myRef = database.getReference();
        myRef.child("lobby").child(game_code).setValue(game_name);

        //TODO: Add player 1 to lobby/game_code/players/ as a node (all players should be stored as children nodes to players/)
        myRef.child("lobby").child(game_code).child("players").child(user.getUid()).setValue(user.getEmail());
        players.add(user); //Add first player to array list so that we can count the number of players

        //Set player 1 to the person who made the game
        p1.setText(user.getEmail());
    }

    public void findPlayers() {

        // Attach a listener to read the data at our game lobby reference
        myRef.child("lobby").child(game_code).child("players").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Need to pull new data from dataSnapshot and display it on the activity
                if (players.size() == 4) {
                    //Start the game
                    Intent mainGameActivity = new Intent(GameLobby.this, Game.class);
                    startActivity(mainGameActivity);
                } else {
                    //Do nothing
                }
            }

            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }

        });
    }

    @Override
    public void onClick(View v) {
        //Stuff happens when buttons are clicked
        int id = v.getId();
        if (id == R.id.button_back) {
            //Clean up old game lobby information
            myRef = database.getReference("lobby");
            myRef.child(game_code).removeValue(); //Remove all data associated with the current game

            //Return to the main lobby
            Intent main_lobby = new Intent(GameLobby.this, MainActivity.class);
            startActivity(main_lobby);
        } else if (id == R.id.button_check_players) {
            findPlayers();
        }
    }
}
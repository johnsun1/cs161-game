package com.example.spy;

import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import java.util.ArrayList;

import android.widget.TextView;

import android.view.View;

public class GameLobbyJoin extends AppCompatActivity implements View.OnClickListener {
    private Intent extra;

    private String game_code;
    private String game_name;

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseUser user;
    private FirebaseUser newUser;

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
        myRef = database.getReference();


        //Add player to lobby/game_code/players/ as a node (all players should be stored as children nodes to players/)
        myRef.child("lobby").child(game_code).child("players").child(user.getUid()).setValue(user); //Store the user in the tree
        players.add(user); //Add first player to array list so that we can count the number of players

        //List the current user's email on the list of players
    }

    public void findPlayers() {

        // Attach a listener to read the data at our game lobby reference
        myRef.child("lobby").child(game_code).child("players").addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {

                //Add new player to the game lobby list and array list
                for (DataSnapshot data: dataSnapshot.getChildren()) {
                    //Look for an empty spot, then put the new player's
                    //TODO: Add the new player to the arraylist
                    FirebaseUser snapshotUser = data.getValue(FirebaseUser.class);

                    Boolean found = false; //Reset found flag to false
                    for (FirebaseUser u: players) {
                        if (u.getUid().equals(snapshotUser.getUid())) {
                            found = true; //snapshotUser is already in the list
                        }
                    }

                    //If the snapshotUser is not already in the list, add them to the list
                    if (!found) {
                        players.add(snapshotUser);
                        newUser = snapshotUser;
                    }
                }

                String defaultText = "Waiting for player to join game";
                if (!p2.getText().equals(defaultText)) {
                    p2.setText(newUser.getEmail());
                } else if (!p3.getText().equals(defaultText)) {
                    p3.setText(newUser.getEmail());
                } else if (!p4.getText().equals(defaultText)) {
                    p4.setText(newUser.getEmail());
                }

                //TODO: Check for full lobby
                if (players.size() == 4) {
                    Intent startGame = new Intent(GameLobbyJoin.this, Game.class);
                    startActivity(startGame);
                }
            }

            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {

            }

            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {

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
            myRef = database.getReference();
            myRef.child("lobby").child(game_code).child("players").child(user.getUid()).removeValue(); //Remove all data associated with the current player, who is leaving the game

            //Return to the main lobby
            Intent main_lobby = new Intent(GameLobbyJoin.this, MainActivity.class);
            startActivity(main_lobby);
        } else if (id == R.id.button_check_players) {
            findPlayers();
        }
    }
}
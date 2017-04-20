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
import java.util.Random;

import android.widget.TextView;

import android.view.View;

public class GameLobbyJoin extends AppCompatActivity implements View.OnClickListener {
    private Intent extra;

    private String game_code;
    private String game_name;

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseUser user;

    ArrayList<Player> players;

    private Bundle data;

    private TextView p1;
    private TextView p2;
    private TextView p3;
    private TextView p4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_lobby_join);

        //Retrieve extra information from game creation menu
        extra = getIntent();
        data = extra.getBundleExtra("game_data");
        game_code = data.getString("game_code");
        game_name = data.getString("game_name");

        players = new ArrayList<Player>();

        //Buttons
        findViewById(R.id.button_back).setOnClickListener(this);
        findViewById(R.id.button_check_players_one).setOnClickListener(this);

        //Get database reference
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();

        //Add player to lobby/game_code/players/ as a node (all players should be stored as children nodes to players/)
        Player player = new Player(user.getEmail(), user.getUid()); //Player doesn't currently have a role

        myRef = database.getReference();
        myRef.child("lobby").child(game_code).child("players").child("player").setValue(player); //Store the user in the tree
        players.add(player);
    }

    public void findPlayers() {

        // Attach a listener to read the data at our game lobby reference
        myRef.child("lobby").child(game_code).child("players").addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    //Add new player to the game lobby list and array list
                    //TODO: Add the new player to the arraylist
                    Player snapshotUser = dataSnapshot.getValue(Player.class);

                    Boolean found = false; //Reset found flag to false
                    for (Player p : players) {
                        if (p.getEmail().equals(snapshotUser.getEmail())) {
                            found = true; //snapshotUser is already in the list
                        }
                    }

                    //If the snapshotUser is not already in the list, add them to the list
                    if (!found) {
                        players.add(snapshotUser);
                    }


                    //TODO: Check for full lobby
                    if (players.size() == 2) {

                        Intent startGame = new Intent(GameLobbyJoin.this, Game.class);

                        startActivity(startGame);
                    }

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
            myRef = database.getReference("lobby");
            myRef.child(game_code).removeValue(); //Remove all data associated with the current game

            //Return to the main lobby
            Intent main_lobby = new Intent(GameLobbyJoin.this, MainActivity.class);
            startActivity(main_lobby);
        } else if (id == R.id.button_check_players_one) {
            findPlayers();
        }
    }
}
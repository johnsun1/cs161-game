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
import java.util.List;
import java.util.Random;

import android.widget.TextView;

import android.view.View;

public class GameLobby extends AppCompatActivity implements View.OnClickListener {
    private Intent extra;

    private Random rand;

    private String game_code;
    private String game_name;

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseUser user;

    ArrayList<Player> players;
    ArrayList<String> playerOrder;

    private Bundle data;
    private Bundle gameData;

    private TextView p1;
    private TextView p2;
    private TextView p3;
    private TextView p4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_lobby);

        rand = new Random();

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
        players = new ArrayList<Player>();
        playerOrder = new ArrayList<String>();

        //Buttons
        findViewById(R.id.button_back).setOnClickListener(this);
        findViewById(R.id.button_check_players_one).setOnClickListener(this);

        //Get database reference
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();

        //Store game information
        myRef = database.getReference().child(game_code);
        //myRef.setValue(game_name);

        //Add player 1 to lobby/game_code/players/ as a node (all players should be stored as children nodes to players/)
        Player player = new Player(user.getEmail(), game_code); //Player doesn't currently have a role

        myRef = database.getReference().child("players").child(user.getUid()); //Store the user in the tree
        players.add(player); //Add first player to array list so that we can count the number of players
        myRef.setValue(player);

        p1.setText(user.getEmail());
    }

    public void findPlayers() {

        // Attach a listener to read the data at our game lobby reference
        myRef = database.getReference("players");
        myRef.addChildEventListener(new ChildEventListener() {

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
                        //Update lobby view
                        if (players.size() > 1) {
                            p2.setText(snapshotUser.getEmail());
                        } else if (players.size() > 2) {
                            p3.setText(snapshotUser.getEmail());
                        } else if (players.size() > 3) {
                            p4.setText(snapshotUser.getEmail());
                        }
                    }

                    if (players.size() == 2) {
                        //Record order of players
                        for (Player p : players) {
                            playerOrder.add(p.getEmail());
                        }

                        //Determine who is the spy
                        int pickSpy = rand.nextInt(2); //Generate a random number from 0 to 1 for testing

                        Intent startGame = new Intent(GameLobby.this, Game.class);

                        //Pick and send over who the spy is, what the the location is
                        gameData = new Bundle();

                        //Store Spy identity and other information into bundle
                        gameData.putString("spy_email", players.get(pickSpy).getEmail());

                        gameData.putString("game_code", game_code);

                        startGame.putExtra("game_data", gameData);

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
            myRef = database.getReference();
            myRef.child(game_code).removeValue(); //Remove all data associated with the current game

            //Return to the main lobby
            Intent main_lobby = new Intent(GameLobby.this, MainActivity.class);
            startActivity(main_lobby);
        } else if (id == R.id.button_check_players_one) {
            findPlayers();
        }
    }
}
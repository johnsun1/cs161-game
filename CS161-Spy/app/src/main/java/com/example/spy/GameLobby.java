package com.example.spy;

import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;

import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;

import android.widget.TextView;

public class GameLobby extends AppCompatActivity {
    private Intent extra;
    private String game_code;
    private ArrayList<FirebaseUser> players;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    TextView p1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_lobby);

        //Retrieve extra information from game creation menu
        extra = getIntent();
        game_code = extra.getStringExtra("game_code");

        //Views
        p1 = (TextView) findViewById(R.id.label_p1);

        findPlayers();
    }

    public void findPlayers() {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("players/" + game_code);
        // Attach a listener to read the data at our posts reference
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String email = dataSnapshot.getValue(String.class);
                p1.setText(p1.getText().toString());
            }
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }

        });
    }
}
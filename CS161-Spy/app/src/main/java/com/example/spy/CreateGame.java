package com.example.spy;

import android.support.v7.app.AppCompatActivity;

import android.content.Intent;

import android.os.Bundle;

import android.widget.EditText;
import android.view.View;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;

import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.auth.FirebaseAuth;

public class CreateGame extends AppCompatActivity implements View.OnClickListener {
    EditText game_name;
    EditText game_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);

        //Views
        game_name = (EditText) findViewById(R.id.field_game_name);
        game_code = (EditText) findViewById(R.id.field_game_code);

        //Buttons
        findViewById(R.id.button_begin_game).setOnClickListener(this);
        findViewById(R.id.button_return_lobby).setOnClickListener(this);
    }

    //Get database reference
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("lobby");

    //Do stuff when buttons are clicked
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.button_begin_game) {

            //Store game information
            myRef.child(game_code.getText().toString()).setValue(game_code.getText().toString());
            myRef.child(game_code.getText().toString()).child("game_name").setValue(game_name.getText().toString());

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            //Store current user into a new game
            myRef = database.getReference("users");
            myRef.child(game_code.getText().toString()).setValue(user.getEmail());

            //Attach the game code to an Intent, and send it to the game waiting lobby
            Intent start_game_lobby = new Intent(CreateGame.this, GameLobby.class);
            start_game_lobby.putExtra("game_code", game_code.getText().toString());
            startActivity(start_game_lobby);
        } else if (i == R.id.button_return_lobby) {
            Intent return_to_lobby = new Intent(CreateGame.this, Lobby.class);
            startActivity(return_to_lobby);
        }
    }
}

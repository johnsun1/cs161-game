package com.example.spy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.widget.TextView;
import android.widget.EditText;

import android.view.View;

import android.content.Intent;

import com.google.firebase.auth.FirebaseUser;

public class JoinGame extends AppCompatActivity implements View.OnClickListener {
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseUser user;

    private EditText join_game_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game);

        //Views
        join_game_code = (EditText) findViewById(R.id.field_join_game_code);

        //Buttons
        findViewById(R.id.button_join_game).setOnClickListener(this);
        findViewById(R.id.button_return_lobby).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //Stuff happens when the user clicks a button
        int id = v.getId();
        if (id == R.id.button_join_game) {
            //Add player to the game lobby
            //Get database reference
            user = FirebaseAuth.getInstance().getCurrentUser();
            database = FirebaseDatabase.getInstance();

            //Store game and player information
            myRef = database.getReference("lobby/" + join_game_code + "/" + user.getEmail());
            myRef.setValue("active");

            Intent existing_game = new Intent(JoinGame.this, Game.class);
            startActivity(existing_game);

        } else if (id == R.id.button_return_lobby) {
            Intent back = new Intent(JoinGame.this, MainActivity.class);
            startActivity(back);
        }
    }
}

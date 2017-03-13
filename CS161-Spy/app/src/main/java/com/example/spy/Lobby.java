package com.example.spy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;
import android.view.View;

public class Lobby extends AppCompatActivity {
    Button createGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        createGame = (Button) findViewById(R.id.button3);
        createGame.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent nextActivity = new Intent(Lobby.this, CreateGame.class);
                startActivity(nextActivity);
            }
        });
    }
}
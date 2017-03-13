package com.example.spy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;
import android.view.View;

public class CreateGame extends AppCompatActivity {
    Button returnLobby;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);

        returnLobby = (Button) findViewById(R.id.button6);
        returnLobby.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent nextActivity = new Intent(CreateGame.this, Lobby.class);
                startActivity(nextActivity);
            }
        });
    }
}

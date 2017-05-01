package com.example.spy;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.EditText;

import android.view.View;

import android.content.Intent;

import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.io.InputStream;

public class JoinGame extends AppCompatActivity implements View.OnClickListener {
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseUser user;

    private EditText join_game_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game);
        //loadImage from Assets
        ImageView imageView3 = (ImageView)findViewById(R.id.join_game_spy);

        try {
            InputStream istream = getResources().getAssets().open("join_game.jpg");
            Bitmap bitmap = BitmapFactory.decodeStream(istream);
            imageView3.setImageBitmap(bitmap);
        } catch (IOException e) {
            Log.d("Assets","Error");
        }

        //Views
        join_game_code = (EditText) findViewById(R.id.field_join_game_code);

        //Buttons
        findViewById(R.id.button_join_game).setOnClickListener(this);
        findViewById(R.id.button_return_lobby).setOnClickListener(this);

        //Initialize stuff
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
    }

    @Override
    public void onClick(View v) {


        //Stuff happens when the user clicks a button
        int id = v.getId();

        //Add player to the game lobby
        if (id == R.id.button_join_game) {

            //Start the existing game lobby
            Intent existing_game = new Intent(JoinGame.this, GameLobbyJoin.class);

            Bundle data = new Bundle();
            data.putString("game_code", join_game_code.getText().toString());
            existing_game.putExtra("game_data", data);

            startActivity(existing_game);

        } else if (id == R.id.button_return_lobby) {
            Intent back = new Intent(JoinGame.this, MainActivity.class);
            startActivity(back);
        }
    }
}

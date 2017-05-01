package com.example.spy;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;

import android.util.Log;
import android.view.View;

import android.content.Intent;

import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.io.InputStream;

public class Lobby extends AppCompatActivity implements View.OnClickListener {
    TextView welcome_message;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        //load image for login screen from asset folder
        ImageView imageView3 = (ImageView)findViewById(R.id.spy3);

        try {
            InputStream istream = getResources().getAssets().open("spy3.jpg");
            Bitmap bitmap = BitmapFactory.decodeStream(istream);
            imageView3.setImageBitmap(bitmap);
        } catch (IOException e) {
            Log.d("Assets","Error");
        }

        //Fields
        welcome_message = (TextView) findViewById(R.id.label_welcome_message);
        welcome_message.setText("Hello " + user.getEmail() + " welcome to Spy, the mobile adaptation of the card game Spy Fall.");

        //Buttons
        findViewById(R.id.button_join_game).setOnClickListener(this);
        findViewById(R.id.button_create_game).setOnClickListener(this);
        findViewById(R.id.button_store).setOnClickListener(this);
        findViewById(R.id.button_logout).setOnClickListener(this);
    }

    public void signOut() {
        FirebaseAuth.getInstance().signOut();
    }

    public void onClick(View v) {
        int i = v.getId();

        if (i == R.id.button_create_game) {
            Intent new_game = new Intent(Lobby.this, CreateGame.class);
            startActivity(new_game);
        } else if (i == R.id.button_join_game) {
            Intent join_game = new Intent(Lobby.this, JoinGame.class);
            startActivity(join_game);
        } else if (i == R.id.button_store) {
            //TO BE IMPLEMENTED Intent store = new Intent(Lobby.this, Store.class);
        } else if (i == R.id.button_logout) {
            signOut();
            Intent title = new Intent(Lobby.this, MainActivity.class);
            startActivity(title);
        }
    }
}
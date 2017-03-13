package com.example.spy;



import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {
    Button userLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userLogin = (Button) findViewById(R.id.button);

        userLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent nextActivity = new Intent(MainActivity.this, Lobby.class);
                startActivity(nextActivity);
            }
        });

    }
}
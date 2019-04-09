package com.example.amsher.guesstimation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.firebase.client.Firebase;

public class GamePage extends AppCompatActivity {
    public Firebase mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_page);

        Firebase.setAndroidContext(this);
        mRef = new Firebase("https://guesstimation-445f5.firebaseio.com/Count");

    }
}

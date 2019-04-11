package com.example.amsher.guesstimation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class IntroPage extends AppCompatActivity {

    private EditText userID;
    private EditText gameID;

    public Firebase mRef;
    public Firebase mRefInstance;
    public Firebase mRefInstanceUser;
    public Firebase mRefInstanceReady;
    public Firebase mRefInstanceScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_page);

        userID = (EditText) findViewById(R.id.editText1);
        gameID = (EditText) findViewById(R.id.editText2);
    }

    protected void onHostClick (View v) {
        Firebase.setAndroidContext(this);
        mRef = new Firebase("https://guesstimation-445f5.firebaseio.com/Game");

        String uValue = userID.getText().toString();
        String gValue = gameID.getText().toString();

        mRefInstance = mRef.child(gValue);
        mRefInstanceUser = mRefInstance.child("Host");
        mRefInstanceUser.setValue(uValue);

        Intent intent1 = new Intent(getApplicationContext(), AdminPage.class);
        startActivity(intent1);
    }

    protected void onJoinClick (View v) {
        Firebase.setAndroidContext(this);
        mRef = new Firebase("https://guesstimation-445f5.firebaseio.com/Game");

        String uValue = userID.getText().toString();
        String gValue = gameID.getText().toString();
        int score = 0;
        int ready = 0;

        mRefInstance = mRef.child(gValue).child(uValue);
        mRefInstanceReady = mRefInstance.child("Ready");
        mRefInstanceReady.setValue(ready);

        mRefInstanceScore = mRefInstance.child("Score");
        mRefInstanceScore.setValue(score);

        Intent intent1 = new Intent(getApplicationContext(), ResultsPage.class);
        startActivity(intent1);
    }
}

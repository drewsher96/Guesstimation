package com.example.amsher.guesstimation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ResultsPage extends AppCompatActivity {

    public Firebase mRef;
    public Firebase mRefInstance;
    public Firebase mRefInstanceUser1;
    public Firebase mRefInstanceUser2;
    public Firebase mRefInstanceUser3;
    public Firebase mRefInstanceScore1;
    public Firebase mRefInstanceScore2;
    public Firebase mRefInstanceScore3;
    private String rgameID;
    private String extra_User;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_page);

        rgameID = getIntent().getStringExtra(IntroPage.Extra_String);
        extra_User = getIntent().getStringExtra(GamePage.ExtraStringU);

        Firebase.setAndroidContext(this);
        mRef = new Firebase("https://guesstimation-445f5.firebaseio.com/Game");

        DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = dataRef.child("Game").child(rgameID);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> names = new ArrayList<>();

                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    String userName = ds.child(extra_User).toString();
                    names.add(userName);

                    String score = ds.child("Score").getValue(String.class);
                    Log.d("User", userName + ":" + score);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        };

    }

    protected void onHomeClick (View v) {
        Intent intent = new Intent(getApplicationContext(), IntroPage.class);
        startActivity(intent);
    }
}

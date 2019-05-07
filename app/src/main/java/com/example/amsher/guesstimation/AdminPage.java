package com.example.amsher.guesstimation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

public class AdminPage extends AppCompatActivity {
    public static String Extra_Stringh;
    private String hgameID;

    public DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    public DatabaseReference mGameRef = mRootRef.child("Game");
    public Firebase mUserRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);

        hgameID = getIntent().getStringExtra(IntroPage.Extra_String);
        TextView tw = (TextView) findViewById(R.id.textView);
        tw.setText(hgameID);
    }

    protected void onStopGame(View v){
        mGameRef.child(hgameID).setValue(null);

        Intent intent = new Intent(getApplicationContext(), IntroPage.class);
        startActivity(intent);
    }

    public void checkQuestion(){
        mUserRef = new Firebase("https://guesstimation-445f5.firebaseio.com/Game/" + hgameID);

        mUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    Map<String, Map<String, String>> map = dataSnapshot.getValue(Map.class);
                    System.out.println(map);

                    Map<String, String> userMap = map.get("1");
                    System.out.println("matchMap: " + userMap);

                    //If player is on last question redirect admin to the results page along with the players
                    if (userMap.get("QuestionID") == "10"){

                        Intent intent = new Intent(getApplicationContext(), ResultsPage.class);
                        startActivity(intent);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    /*Toast.makeText(getApplicationContext(), "Game Session has Ended", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getApplicationContext(), IntroPage.class);
                    startActivity(intent);*/

                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


    }
}



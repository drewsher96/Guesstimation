package com.example.amsher.guesstimation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class ResultsPage extends AppCompatActivity {

    public Firebase mRef;
    private Button homeBtn;

    public DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    public DatabaseReference mGameRef = mRootRef.child("Game");
    public DatabaseReference mSessionRef;

    private String rgameID;
    private String userID;
    public int NumOfPlayers;
    public int display =0;

    public TextView playerResultsTV;
    public TextView playerResultsTV2;

    ArrayList<String> finalName = new ArrayList<>();
    ArrayList<String> finalScore = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_page);

        Intent intentGame = getIntent();
        Bundle extras = intentGame.getExtras();
        rgameID = extras.getString("GameID");
        userID = extras.getString("UserID");

        playerResultsTV = (TextView) findViewById(R.id.playerResultsTV);
        playerResultsTV2 = (TextView) findViewById(R.id.playerResultsTV2);

        System.out.println("This is the game ID: " + rgameID);
        System.out.println("This is the user ID: " + userID);

        homeBtn = (findViewById(R.id.homeBtn));

        Firebase.setAndroidContext(this);
        mRef = new Firebase("https://guesstimation-445f5.firebaseio.com/Game");
        DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = dataRef.child("Game").child(rgameID);

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onHomeClick();
            }
        });

        getPlayerStatus();
    }

    protected void onHomeClick () {
        mGameRef.child(rgameID).setValue(null);

        Intent intent = new Intent(getApplicationContext(), IntroPage.class);
        startActivity(intent);
    }

    public void getPlayerStatus() {
        mRef = new Firebase("https://guesstimation-445f5.firebaseio.com/Game/" + rgameID);
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Map<String, String>> map = dataSnapshot.getValue(Map.class);
                System.out.println(map);
                Set players = map.entrySet();

                if (!players.isEmpty()) {
                    NumOfPlayers = players.size() - 1;
                    System.out.println("### PLAYER COUNT ###");
                    System.out.println("         " + NumOfPlayers);
                    System.out.println("####################");

                } else {
                    System.out.println("No Players Found");
                }

                NumOfPlayers = players.size() - 1;
                System.out.println("Number of Players: " + NumOfPlayers);


                for (int i = 1; i < NumOfPlayers + 1; i++) {
                    Map<String, String> statusMap = map.get(Integer.toString(i));
                    System.out.println("matchMap: " + statusMap);
                    String name = null;
                    if (statusMap.containsKey("Name")) {
                        name = statusMap.get("Name");
                        System.out.println("Value from Map: " + name);
                        finalName.add(name);
                    }


                }

                for (int i = 1; i < NumOfPlayers + 1; i++) {
                    Map<String, String> statusMap = map.get(Integer.toString(i));
                    System.out.println("matchMap: " + statusMap);
                    String score = null;
                    if (statusMap.containsKey("Score")) {
                        score = statusMap.get("Score");
                        System.out.println("Value from Map: " + score);
                        finalScore.add(score);
                    }
                    //Gets name and score from a arrays added above and concats to the two textViews
                    String n = finalName.get(i - 1);
                    String s = finalScore.get(i - 1);
                    playerResultsTV.setText(playerResultsTV.getText().toString() + n + ": " + "\r\n");
                    playerResultsTV2.setText(playerResultsTV2.getText().toString() + s + "\r\n");
                }

            }


            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}


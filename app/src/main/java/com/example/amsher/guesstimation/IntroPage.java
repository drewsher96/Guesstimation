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
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.SECONDS;

public class IntroPage extends AppCompatActivity {

    private EditText userID;
    public EditText gameID;
    public Button JoinBtn;
    public Button HostBtn;
    public String uValue;
    public String gValue;
    public String userName;
    private Intent intentGame;
    public Bundle extras;
    public static String Extra_String;
    public static String Extra_StringU;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mGameRef = mRootRef.child("Game");

    public Firebase mRef;
    public Firebase mRefInstance;
    public Firebase mRefInstanceUser;

    //Declaring variables
    int NumOfPlayers = 0;
    int AllReady;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_page);

        userID = (EditText) findViewById(R.id.editText1);
        gameID = (EditText) findViewById(R.id.editText2);
        JoinBtn = findViewById(R.id.button4);
        HostBtn = findViewById(R.id.button);

        intentGame = new Intent(getApplicationContext(), GamePage.class);
        extras = new Bundle();

        JoinBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onJoinClick();
            }
        });

        HostBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onHostClick();
            }
        });

        Firebase.setAndroidContext(this);

        System.out.println(gameID.getText().toString());

    }

    protected void onHostClick () {
        Firebase.setAndroidContext(this);
        //mRef = new Firebase("https://guesstimation-445f5.firebaseio.com/Game");

        userName = userID.getText().toString();
        gValue = gameID.getText().toString();

        /*mRefInstance = mRef.child(gValue);
        mRefInstanceUser = mRefInstance.child("Host");
        mRefInstanceUser.setValue(userName);*/

        mGameRef.child(gValue).child("Host").setValue(userName);

        Intent intent1 = new Intent(getApplicationContext(), AdminPage.class);

        intent1.putExtra(Extra_String, gValue);
        startActivity(intent1);
    }

    protected void onJoinClick () {
        NumOfPlayers = 0;
        userName = userID.getText().toString();
        gValue = gameID.getText().toString();

        getPlayerCount();
    }

    // Add a player to the game. This is where the user is added to the game. You need to store this
    public void AddPlayerToGame() {
        mRef = new Firebase("https://guesstimation-445f5.firebaseio.com/Game/" + gValue);

        System.out.println("Number of Players Add: " + NumOfPlayers);
        Player player = new Player(Integer.toString(NumOfPlayers + 1), userName);

        System.out.println("This is the playerID: " + player.playerID);
        mRef.child(player.playerID).child("Name").setValue(userName);
        mRef.child(player.playerID).child("Score").setValue(player.score);
        mRef.child(player.playerID).child("Ready").setValue(player.ready);

        // Store the player.playerID in a variable.
        // This is the user's unique ID. You'll refer to this every time you need to update their status
        // (Hint: Write a method for updating the status).

        uValue = player.playerID;
        extras.putString("UserID", uValue);
        extras.putString("GameID", gValue);

        intentGame.putExtras(extras);
        startActivity(intentGame);
    }

    public void getPlayerCount() {
        String url = "https://guesstimation-445f5.firebaseio.com/Game/" + gValue;
        System.out.println("Firebase URL: " + url);
        mRef = new Firebase(url);

        mRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Map<String, Map<String, String>> map = dataSnapshot.getValue(Map.class);
                System.out.println(map);

                // Pull all children in the game object and place them in a Set variable
                Set players = map.entrySet();

                if(!players.isEmpty()){
                    NumOfPlayers = players.size() - 1;
                    System.out.println("### PLAYER COUNT ###");
                    System.out.println("         " + NumOfPlayers);
                    System.out.println("####################");
                    AddPlayerToGame();
                }
                else {
                    System.out.println("No Players Found");
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }

        });
    }
}

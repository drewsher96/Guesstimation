/*
package com.example.amsher.guesstimation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

public class AlexCode {

    /* TODO LIST

        1.  You need to set it up so that when the user hits "Ready Up", it updates their status only.
            To do this, you'll need to keep track of their unique userID.
        2.  You need to set it up so that you're keeping track of points/questions/etc.
        3.  You need to store the game session. This is as simple as passing the value in an intent
            and storing it in the "gameSessionID" variable.
        4.  You need to setup some sort of waiting screen after the user submits an answer.
            This is so user's can interact with the components until all players have submitted their answers.
        5.  For this app, I was updating the users' statuses manually in Firebase,
            but you'll need to create a function that updates them when a user submits an answer.

            I'm sure there's more you'll need to do.
    */
/*
    // Initialize Variables
    Button  readyButton;
    Button  addPlayerButton;
    Button  removePlayerButton;
    TextView statusTV;
    TextView sessionTV;
    TextView playerCountTV;

    // Initialize Firebase Variables
    Firebase mRef;
    DatabaseReference mDatabase;
    String gameSessionID = "1"; // TODO: Pass the real session ID from your Join Game activity
    int NumOfPlayers;
    int AllReady;
    String playerID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Assign variables to their components
        readyButton         = findViewById(R.id.readyButton);
        addPlayerButton     = findViewById(R.id.addPlayerButton);
        removePlayerButton  = findViewById(R.id.removePlayerButton);
        statusTV            = findViewById(R.id.statusTextView);
        sessionTV           = findViewById(R.id.sessionIdTV);
        playerCountTV       = findViewById(R.id.playerCountTV);

        getPlayerStatus();

        readyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: This is where you'll call the method that updates the user's status after they submit an answer
            }
        });

        // TODO: This is for testing
        addPlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddPlayerToGame();
            }
        });

        // TODO: This is for testing
        removePlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RemovePlayerFromGame();
            }
        });
    }

    // Tell Firebase to check if everyone is ready for the next round.
    public void getPlayerStatus() {
        mRef = new Firebase("https://alex-4363.firebaseio.com/Game");
        mRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Map<String, String>> map = dataSnapshot.getValue(Map.class);
                if (gameSessionID.equals(map.get("SessionID"))) {

                    // Pull all children in the game object and place them in a Set variable
                    Set players = map.entrySet();
                    // Count the number of children in the object, subtract one for the SessionID, the remainder are the players
                    // Put this value in the NumOfPlayers variable to keep track of how many players we have in our game
                    NumOfPlayers = players.size() - 1;
                    System.out.println("Number of Players: " + NumOfPlayers);
                    playerCountTV.setText("Players: " + Integer.toString(NumOfPlayers));

                    /*
                        # Get all the player statuses and multiply them.
                        # Creating a variable to keep track of all of the statuses
                        # Naming it "MotherStatus" since we'll just multiply the most recent status by this value
                        # If 0, then someone isn't ready, if 1, everyone is ready.
                    */

                    int MotherStatus = 1;
                    // Run a loop that counts to the number of players
                    for (int i = 1; i < NumOfPlayers+1; i++) {

                        // Create a map of each individual player's status
                        Map<String, String> statusMap = map.get("Player"+i);
                        System.out.println("matchMap: " + statusMap);

                        /*
                            # Create a 'status' variable to store the value for each player.
                            # Multiply each status by the MotherStatus
                        */
                        String status = null;
                        if(statusMap.containsKey("Status")){
                            status = statusMap.get("Status");
                            System.out.println("Value from Map: " + status);
                            // Multiply each status by the "MotherStatus"
                            MotherStatus = MotherStatus * Integer.parseInt(status);
                        }

                        /*
                            # Only if all players have been accounted for AND the MotherStatus equals 1,
                              then all players have submitted answers and are ready to go.
                        */
                        System.out.println("MotherStatus: " + MotherStatus);
                        if (i == NumOfPlayers && MotherStatus == 1) {
                            statusTV.setText("Everyone is Ready!");
                            AllReady = MotherStatus;
                            break;
                        }
                        else if (i == NumOfPlayers && MotherStatus == 0) {
                            System.out.println("Waiting on Someone...");
                            statusTV.setText("Waiting on Someone...");
                        }
                    }
                }
                else {
                    statusTV.setText("Wrong Session...");
                    // Pull all children in the game object and place them in a Set variable
                    Set players = map.entrySet();
                    // Count the number of children in the object, subtract one for the SessionID, the remainder are the players
                    // Put this value in the NumOfPlayers variable to keep track of how many players we have in our game
                    NumOfPlayers = players.size() - 1;
                    System.out.println("Number of Players: " + NumOfPlayers);
                    playerCountTV.setText("Players: " + Integer.toString(NumOfPlayers));
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }

        });
    }

    // TODO: Start a new round. This is where you'll reset the user's status and fetch the next question.
    public void startNextRound() {


    }

    // TODO: Remove a player to the game
    public void RemovePlayerFromGame() {
        mRef = new Firebase("https://alex-4363.firebaseio.com/Game");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // TODO: Get the userID of the current player and store that value in a variable so the player can be removed from the game
        Player player = new Player(Integer.toString(3));
        mDatabase.child("Game").child("Player" + player.playerID).removeValue();
    }

    // Add a player to the game. This is where the user is added to the game. You need to store this
    public void AddPlayerToGame() {
        mRef = new Firebase("https://alex-4363.firebaseio.com/Game");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Player player = new Player(Integer.toString(NumOfPlayers+1));
        mDatabase.child("Game").child("Player" + player.playerID).child("UserID").setValue(player.playerID);
        mDatabase.child("Game").child("Player" + player.playerID).child("Status").setValue(player.status);

        // Store the player.playerID in a variable.
        // This is the user's unique ID. You'll refer to this every time you need to update their status
        // (Hint: Write a method for updating the status).
        playerID = player.playerID;
    }

    public void getPlayerCount() {

        mRef = new Firebase("https://alex-4363.firebaseio.com/Game");
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Map<String, String>> map = dataSnapshot.getValue(Map.class);
                System.out.println(map);

                // Check and make sure that the user is in the right game
                if (gameSessionID.equals(map.get("SessionID"))) {

                    // Pull all children in the game object and place them in a Set variable
                    Set players = map.entrySet();

                    // Count the number of children in the object, subtract one for the SessionID, the remainder are the players
                    // Put this value in the NumOfPlayers variable to keep track of how many players we have in our game
                    NumOfPlayers = players.size() - 1;
                    System.out.println("Number of Players: " + NumOfPlayers);
                    playerCountTV.setText("Players: " + Integer.toString(NumOfPlayers));
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    // This is for testing - this can be removed from the final version of the app
    public void updateGameSession() {
        sessionTV.setText("Session ID: " + gameSessionID);
    }

    public void setStatusTo(String newStatus) {
        if (newStatus == "1") {
            // TODO: Set user status to 1
            // The code from the AddPlayer method should come in handy here.
        }
        else {
            // TODO: Set user status to 0
            // The code from the AddPlayer method should come in handy here.
        }
    }
}
*/
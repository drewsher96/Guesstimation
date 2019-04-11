package com.example.amsher.guesstimation;

import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Map;
import java.util.Random;
import java.util.Set;

public class GamePage extends AppCompatActivity {
    public Firebase mRef;
    public Firebase mRefInstance;
    public Firebase mRefInstanceUser;
    public Firebase mRefInstanceReady;
    String gameSessionID = "1"; //Need to pull real Session ID
    int NumOfPlayers;
    int AllReady;
    String playerID;
    TextView playerCountTV;
    TextView statusTV;

    private TextView gameQuestion;
    private RadioButton questionAnswer1;
    private RadioButton questionAnswer2;
    private RadioButton questionAnswer3;
    private RadioButton questionAnswer4;
    int count = 1;

    String[] qArray = {"How tall is the statue of liberty in feet?","What is the population of Oklahoma in Millions?","How many days are in the average school year?",
            "What is the average airspeed velocity of an unladen African swallow in MPH?","How many Super Bowls have the New England Patriots won?",
            "How many different varieties of apples are there?","How much wood could a woodchuck chuck? (in pounds) ","How much does a Tesla Roadster cost in USD?",
            "What is Bill Gates net worth in Billions? ","What Year did the first moon landing take place? ","Game Over" };

    //An Array of Arrays of answers to questions for the above array. The placement in the initial array
    //corresponds to their question above
    String[][] aArray = {{"305","300","260","412"},{"2.5","3.3","3.9","2.7"},{"200","170","160","180"},{"12","24","16","17"},{"6","1","8","Too Many"},
            {"5000","5500","6000","7500"},{"700","520","650","900"},{"300K","400K","180K","200K"},{"82.2","99.7","90.6","75.4"},{"1970","1969","1971","1962"},{"Game Over","Game Over","Game Over","Game Over"}};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_page);



        gameQuestion = (TextView)findViewById(R.id.gameQuestion);
        questionAnswer1 = (RadioButton)findViewById(R.id.questionAnswer1);
        questionAnswer2 = (RadioButton)findViewById(R.id.questionAnswer2);
        questionAnswer3 = (RadioButton)findViewById(R.id.questionAnswer3);
        questionAnswer4 = (RadioButton)findViewById(R.id.questionAnswer4);
        playerCountTV = findViewById(R.id.playerCountTV);
        statusTV = findViewById(R.id.statusTV);


    }

    public void getPlayerStatus(){
        mRef = new Firebase("https://guesstimation-445f5.firebaseio.com/Game");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Map<String, String>> map = dataSnapshot.getValue(Map.class);
                if(gameSessionID.equals(map.get("SessionID"))){
                    Set players = map.entrySet();

                    NumOfPlayers = players.size() - 1;
                    System.out.println("Number of Players: " + NumOfPlayers);
                    playerCountTV.setText("Players: " + Integer.toString(NumOfPlayers));


                    int MotherStatus = 1;
                    for(int i = 1; i < NumOfPlayers+1; i++){
                        Map<String, String> statusMap = map.get("Player"+i);
                        System.out.println("matchMap: " + statusMap);
                        String status = null;
                        if(statusMap.containsKey("Status")){
                            status = statusMap.get("Status");
                            System.out.println("Value from Map: " + status);
                            MotherStatus = MotherStatus * Integer.parseInt(status);
                        }

                        System.out.println("MotherStatus: " + MotherStatus);
                        if (i == NumOfPlayers && MotherStatus == 1){
                            statusTV.setText("All Players Are Ready");
                            AllReady = MotherStatus;
                            break;
                        }
                        else if(i == NumOfPlayers && MotherStatus == 0){
                            System.out.println("Waiting on Players...");
                            statusTV.setText("Waiting on Players...");
                        }
                    }
                }

                else{
                    statusTV.setText("Wrong Session");
                    Set players = map.entrySet();
                    NumOfPlayers = players.size() -1;
                    System.out.println("Number of Players: " + NumOfPlayers);
                    playerCountTV.setText("Players: " + Integer.toString(NumOfPlayers));
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }


    protected void onReadyClick (View v){


        String question = qArray[count];
        String answer1 = aArray[count][0];
        String answer2 = aArray[count][1];
        String answer3 = aArray[count][2];
        String answer4 = aArray[count][3];
        gameQuestion.setText(question);
        questionAnswer1.setText(answer1);
        questionAnswer2.setText(answer2);
        questionAnswer3.setText(answer3);
        questionAnswer4.setText(answer4);
        count++;
        if(count == qArray.length){
            Intent intent1 = new Intent(getApplicationContext(), ResultsPage.class);
            startActivity(intent1);
        }


    }
}

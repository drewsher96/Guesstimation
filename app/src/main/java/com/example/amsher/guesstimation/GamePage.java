package com.example.amsher.guesstimation;

import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;
import java.util.Random;
import java.util.Set;

public class GamePage extends AppCompatActivity {
    public Firebase mRef;
    public DatabaseReference mDatabase;
    public Firebase mRefInstance;
    public Firebase mRefInstanceUser;
    public Firebase mRefInstanceReady;
    public String gameSessionID; //Need to pull real Session ID
    public static String ExtraStringU;
    String userName;
    int NumOfPlayers;
    int AllReady;
    String playerID;
    TextView playerCountTV;
    TextView statusTV;

    private TextView gameQuestion;
    private Button lockInBtn;
    private RadioButton questionAnswer1;
    private RadioButton questionAnswer2;
    private RadioButton questionAnswer3;
    private RadioButton questionAnswer4;
    int count = 1;
    String newStatus = "0";


    //String gameID = intent1.getStringExtra("gameID");

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
        lockInBtn = findViewById(R.id.lockInBtn);

        //this is the new way to get the extra strings when there is more than one string to be passed, the way its set up is on the intro page.
        Bundle extras = getIntent().getExtras();
        gameSessionID = extras.getString("GameID");
        userName = extras.getString("UserID");

        getPlayerCount();
        //getPlayerStatus();

        lockInBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                setStatusTo(newStatus);
            }
        });
    }

    protected void onLockinClick(View v){
        newStatus = "1";
        startNextRound();
    }

    public void getPlayerStatus(){
        mRef = new Firebase("https://guesstimation-445f5.firebaseio.com/Game/" + gameSessionID);
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Map<String, String>> map = dataSnapshot.getValue(Map.class);
                System.out.println(map);
                //GET RID OF IF ELSE STATEMENT
                //ADD ARRAY OF MAP VALUES TO INTRO PAGE
                //CHECK GAME ID AGAINST THAT BEFORE MOVING ON
                Set players = map.entrySet();

                NumOfPlayers = players.size() - 1;
                System.out.println("Number of Players: " + NumOfPlayers);
                playerCountTV.setText(Integer.toString(NumOfPlayers));


                int MotherStatus = 1;
                for(int i = 1; i < NumOfPlayers+1; i++){
                    Map<String, String> statusMap = map.get(userName);
                    System.out.println("matchMap: " + statusMap);
                    String status = null;
                    if(statusMap.containsKey("Ready")){
                        status = statusMap.get("Ready");
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

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void startNextRound(){
        if(AllReady == 1) {
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
                intent1.putExtra(ExtraStringU, userName);
                startActivity(intent1);
            }
        }
    }

    public void getPlayerCount() {
        String url = "https://guesstimation-445f5.firebaseio.com/Game/" + gameSessionID;
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
                    NumOfPlayers = players.size();
                    System.out.println("### PLAYER COUNT ###");
                    System.out.println("         " + NumOfPlayers);
                    System.out.println("####################");

                    playerCountTV.setText(Integer.toString(NumOfPlayers));
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

    public void setStatusTo(String newStatus){
        if(newStatus == "1") {

        }
        else {

        }
    }
}

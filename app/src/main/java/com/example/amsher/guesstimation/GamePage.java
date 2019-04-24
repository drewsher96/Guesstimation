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
import java.util.Set;

public class GamePage extends AppCompatActivity {
    //Firebase variables
    public Firebase mRef;
    public DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    public DatabaseReference mGameRef = mRootRef.child("Game");
    public Firebase mQuestionRef;
    public DatabaseReference mUserRef;

    public String gameSessionID; //Need to pull real Session ID
    public static String ExtraStringU;
    private Intent intentGame;
    public Bundle extras;
    public String userID;
    public String questionID = "1";
    public int pointCounter = 0;
    public int NumOfPlayers;
    public int AllReady;
    public int counter;
    TextView playerCountTV;
    TextView statusTV;

    private TextView gameQuestion;
    private Button lockInBtn;
    private RadioButton questionAnswer1;
    private RadioButton questionAnswer2;
    private RadioButton questionAnswer3;
    private RadioButton questionAnswer4;


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
        gameQuestion = (TextView) findViewById(R.id.gameQuestion);
        questionAnswer1 = (RadioButton) findViewById(R.id.questionAnswer1);
        questionAnswer2 = (RadioButton) findViewById(R.id.questionAnswer2);
        questionAnswer3 = (RadioButton) findViewById(R.id.questionAnswer3);
        questionAnswer4 = (RadioButton) findViewById(R.id.questionAnswer4);
        playerCountTV = findViewById(R.id.playerCountTV);
        statusTV = findViewById(R.id.statusTV);
        lockInBtn = findViewById(R.id.lockInBtn);

        //this is the new way to get the extra strings when there is more than one string to be passed, the way its set up is on the intro page
        Intent intentGame = getIntent();
        Bundle extras = intentGame.getExtras();
        gameSessionID = extras.getString("GameID");
        userID = extras.getString("UserID");
        mUserRef = mGameRef.child(gameSessionID).child(userID);

        getPlayerStatus();

        lockInBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onLockInClick();
            }
        });
    }

    protected void onLockInClick(){
        mUserRef.child("Ready").setValue("1");

    }

    public void getPlayerStatus(){
        mRef = new Firebase("https://guesstimation-445f5.firebaseio.com/Game/" + gameSessionID);
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Map<String, String>> map = dataSnapshot.getValue(Map.class);
                System.out.println(map);

                Set players = map.entrySet();

                if(!players.isEmpty()){
                    NumOfPlayers = players.size() -1;
                    System.out.println("### PLAYER COUNT ###");
                    System.out.println("         " + NumOfPlayers);
                    System.out.println("####################");

                    playerCountTV.setText(Integer.toString(NumOfPlayers));
                }
                else {
                    System.out.println("No Players Found");
                }

                NumOfPlayers = players.size() - 1;
                System.out.println("Number of Players: " + NumOfPlayers);
                playerCountTV.setText(Integer.toString(NumOfPlayers));


                int MotherStatus = 1;
                for(int i = 1; i < NumOfPlayers+1; i++) {
                    Map<String, String> statusMap = map.get(Integer.toString(i));
                    System.out.println("matchMap: " + statusMap);
                    String status = null;
                    if (statusMap.containsKey("Ready")) {
                        status = statusMap.get("Ready");
                        System.out.println("Value from Map: " + status);
                        MotherStatus = MotherStatus * Integer.parseInt(status);
                    }

                    System.out.println("MotherStatus: " + MotherStatus);
                    if (i == NumOfPlayers && MotherStatus == 1) {
                        statusTV.setText("All Players Are Ready");
                        AllReady = MotherStatus;
                        startNextRound();
                        break;
                    } else if (i == NumOfPlayers && MotherStatus == 0) {
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
        mQuestionRef = new Firebase("https://guesstimation-445f5.firebaseio.com/Questions/");

            /*String question = qArray[count];
            String answer1 = aArray[count][0];
            String answer2 = aArray[count][1];
            String answer3 = aArray[count][2];
            String answer4 = aArray[count][3];
            gameQuestion.setText(question);
            questionAnswer1.setText(answer1);
            questionAnswer2.setText(answer2);
            questionAnswer3.setText(answer3);
            questionAnswer4.setText(answer4);
            count++;*/

            mQuestionRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // need to check to make sure an answer was selected somehow
                        if(AllReady == 1) {

                        if (questionAnswer1.isChecked()){
                            pointCounter++;
                        }

                        Map<String, Map<String, String>> map = dataSnapshot.getValue(Map.class);
                        System.out.println(map);

                        System.out.println(questionID);

                        Map<String, String> questionMap = map.get(questionID);
                        System.out.println("matchMap: " + questionMap);

                        String question = questionMap.get("Question");
                        String answer1 = questionMap.get("CAnswer");
                        String answer2 = questionMap.get("I1Answer");
                        String answer3 = questionMap.get("I2Answer");
                        String answer4 = questionMap.get("I3Answer");

                        gameQuestion.setText(question);
                        questionAnswer1.setText(answer1);
                        questionAnswer2.setText(answer2);
                        questionAnswer3.setText(answer3);
                        questionAnswer4.setText(answer4);

                        mUserRef.child("Ready").setValue("0");
                        counter = Integer.parseInt(questionID);
                        counter++;
                        questionID = Integer.toString(counter);

                            if(counter == 9) {
                                //adding points to user's firebase node
                                mUserRef.child("Score").setValue(Integer.toString(pointCounter));

                                Intent intent2 = new Intent(getApplicationContext(), ResultsPage.class);
                                intent2.putExtra(ExtraStringU, userID);
                                startActivity(intent2);
                            }
                        }

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });

            //Resetting user to not ready


            // hard coding this for current amount of questions in database
            /*else {
                intentGame = new Intent(getApplicationContext(), GamePage.class);
                extras = new Bundle();
                extras.putInt("Count", count);
                extras.putString("UserID", userID);
                extras.putString("GameID", gameSessionID);

                intentGame.putExtras(extras);
                startActivity(intentGame);
            }*/

    }
}

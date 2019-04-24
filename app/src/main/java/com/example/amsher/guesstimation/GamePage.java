package com.example.amsher.guesstimation;

import android.content.Intent;
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
                        if(counter == 2) {
                            //adding points to user's firebase node
                            mUserRef.child("Score").setValue(Integer.toString(pointCounter));

                            mRef.removeEventListener(this);
                            Intent intent2 = new Intent(getApplicationContext(), ResultsPage.class);
                            intent2.putExtra(ExtraStringU, gameSessionID);
                            startActivity(intent2);
                        }
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

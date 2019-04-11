package com.example.amsher.guesstimation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.firebase.client.Firebase;

import java.util.Random;

public class GamePage extends AppCompatActivity {
    public Firebase mRef;

    private TextView gameQuestion;
    private RadioButton questionAnswer1;
    private RadioButton questionAnswer2;
    private RadioButton questionAnswer3;
    private RadioButton questionAnswer4;

    String[] qArray = {"How tall is the statue of liberty in feet?","What is the population of Oklahoma in Millions?","How many days are in the average school year?",
            "What is the average airspeed velocity of an unladen African swallow in MPH?","How many Super Bowls have the New England Patriots won?",
            "How many different varieties of apples are there?","How much wood could a woodchuck chuck? (in pounds) ","How much does a Tesla Roadster cost in USD?",
            "What is Bill Gates net worth in Billions? ","What Year did the first moon landing take place? " };

    //An Array of Arrays of answers to questions for the above array. The placement in the initial array
    //corresponds to their question above
    String[][] aArray = {{"305","300","260","412"},{"2.5","3.3","3.9","2.7"},{"200","170","160","180"},{"12","24","16","17"},{"6","1","8","Too Many"},
            {"5000","5500","6000","7500"},{"700","520","650","900"},{"300K","400K","180K","200K"},{"82.2","99.7","90.6","75.4"},{"1970","1969","1971","1962"}};

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_page);

        Firebase.setAndroidContext(this);
        mRef = new Firebase("https://guesstimation-445f5.firebaseio.com/Count");

        gameQuestion = (TextView)findViewById(R.id.gameQuestion);
        questionAnswer1 = (RadioButton)findViewById(R.id.questionAnswer1);
        questionAnswer2 = (RadioButton)findViewById(R.id.questionAnswer2);
        questionAnswer3 = (RadioButton)findViewById(R.id.questionAnswer3);
        questionAnswer4 = (RadioButton)findViewById(R.id.questionAnswer4);


    }

    protected void onReadyClick (View v){
        int randomIndex = new Random().nextInt(qArray.length);
        String question = qArray[randomIndex];
        String answer1 = aArray[randomIndex][0];
        String answer2 = aArray[randomIndex][1];
        String answer3 = aArray[randomIndex][2];
        String answer4 = aArray[randomIndex][3];
        gameQuestion.setText(question);
        questionAnswer1.setText(answer1);
        questionAnswer2.setText(answer2);
        questionAnswer3.setText(answer3);
        questionAnswer4.setText(answer4);

    }
}

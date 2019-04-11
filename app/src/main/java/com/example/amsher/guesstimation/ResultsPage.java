package com.example.amsher.guesstimation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.firebase.client.Firebase;

public class ResultsPage extends AppCompatActivity {

    public Firebase mRef;
    public Firebase mRefInstance;
    public Firebase mRefInstanceUser1;
    public Firebase mRefInstanceUser2;
    public Firebase mRefInstanceUser3;
    public Firebase mRefInstanceScore1;
    public Firebase mRefInstanceScore2;
    public Firebase mRefInstanceScore3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_page);
    }

    protected void onHomeClick (View v) {
        Intent intent = new Intent(getApplicationContext(), IntroPage.class);
        startActivity(intent);
    }
}

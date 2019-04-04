package com.example.amsher.guesstimation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AdminPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);
    }

    protected void onStopGame(Bundle savedInstanceState){
        Intent intent = new Intent(getApplicationContext(),IntroPage.class);
        startActivity(intent);
    }
}

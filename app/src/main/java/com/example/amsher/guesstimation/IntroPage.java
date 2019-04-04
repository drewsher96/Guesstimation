package com.example.amsher.guesstimation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class IntroPage extends AppCompatActivity {

    public EditText input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_page);
    }

    protected void onHostClick (View v) {
        input = (EditText) findViewById(R.id.editText1);
        String host = input.getText().toString();

        Intent intent = new Intent(getApplicationContext(),AdminPage.class);
        startActivity(intent);


    }

    protected void onJoinClick (View v) {
        input = (EditText) findViewById(R.id.editText2);
        String join = input.getText().toString();

        Intent intent = new Intent(getApplicationContext(),GamePage.class);
        startActivity(intent);


    }
}

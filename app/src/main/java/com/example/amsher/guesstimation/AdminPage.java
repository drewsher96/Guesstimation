package com.example.amsher.guesstimation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminPage extends AppCompatActivity {
    public static String Extra_Stringh;
    private String hgameID;

    public DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    public DatabaseReference mGameRef = mRootRef.child("Game");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);

        hgameID = getIntent().getStringExtra(IntroPage.Extra_String);
        TextView tw = (TextView) findViewById(R.id.textView);
        tw.setText(hgameID);
    }

    protected void onStopGame(View v){
        mGameRef.child(hgameID).setValue(null);

        Intent intent = new Intent(getApplicationContext(), IntroPage.class);
        startActivity(intent);
    }
}

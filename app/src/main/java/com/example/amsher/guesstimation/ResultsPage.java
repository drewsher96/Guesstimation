package com.example.amsher.guesstimation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ResultsPage extends AppCompatActivity {
    public Firebase mRef;
    private Button homeBtn;
    public DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    public DatabaseReference mGameRef = mRootRef.child("Game");
    private String rgameID;
    private String extra_User;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_page);

        Intent intentGame = getIntent();
        Bundle extras = intentGame.getExtras();
        rgameID = extras.getString("GameID");
        extra_User = extras.getString("UserID");

        System.out.println("This is the game ID: " + rgameID);
        System.out.println("This is the user ID: " + extra_User);

        homeBtn = (findViewById(R.id.homeBtn));

        Firebase.setAndroidContext(this);
        mRef = new Firebase("https://guesstimation-445f5.firebaseio.com/Game");
        DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = dataRef.child("Game").child(rgameID);

        homeBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onHomeClick();
            }
        });


        /*ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> names = new ArrayList<>();

                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    String userName = ds.child(extra_User).toString();
                    names.add(userName);

                    String score = ds.child("Score").getValue(String.class);
                    Log.d("User", userName + ":" + score);
                }

                for(String userName : names) {
                    TextView stringTextView = (TextView) findViewById(R.id.textView4);
                    stringTextView.setText(stringTextView.getText().toString() + userName);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        };
*/
    }

    protected void onHomeClick () {
        mGameRef.child(rgameID).setValue(null);

        Intent intent = new Intent(getApplicationContext(), IntroPage.class);
        startActivity(intent);
    }
}

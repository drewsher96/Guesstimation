package com.example.amsher.guesstimation;

public class Player {

    public String playerID;
    public String status;

//    public Player() {
//        // Default constructor required for calls to DataSnapshot.getValue(User.class)
//    }

    public Player(String id) {
        this.playerID = id;
        this.status = "0";
    }
}

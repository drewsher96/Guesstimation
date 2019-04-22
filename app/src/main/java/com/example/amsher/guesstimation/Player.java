package com.example.amsher.guesstimation;

public class Player {

    public String playerID;
    public String ready;
    public int score;
    public String name;

//    public Player() {
//        // Default constructor required for calls to DataSnapshot.getValue(User.class)
//    }

    public Player(String id, String name) {
        this.playerID = id;
        this.ready = "0";
        this.score = 0;
        this.name = name;
    }
}

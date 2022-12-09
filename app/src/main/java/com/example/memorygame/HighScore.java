package com.example.memorygame;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class HighScore extends AppCompatActivity {

    private TextView mUser1;
    private TextView mScore1;
    private TextView mUser2;
    private TextView mScore2;
    private TextView mUser3;
    private TextView mScore3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        mUser1 = findViewById(R.id.user1);
        mScore1 = findViewById(R.id.score1);
        mUser2 = findViewById(R.id.user2);
        mScore2 = findViewById(R.id.score2);
        mUser3 = findViewById(R.id.user3);
        mScore3 = findViewById(R.id.score3);

        SharedPreferences sharedPref = getSharedPreferences("myScores", Context.MODE_PRIVATE);
        String username = sharedPref.getString("name", "Player1");
        int score = sharedPref.getInt("highScore", 0);
        String scoreText = Integer.toString(score);
        if(score == 0) {
            mUser1.setVisibility(View.GONE);
        }
        else {

            mUser1.setText(username);
            mScore1.setText(scoreText);
        }
        username = sharedPref.getString("name2", "Player2");
        score = sharedPref.getInt("highScore2", 0);
        scoreText = Integer.toString(score);
        if(score == 0) {
            mUser2.setVisibility(View.GONE);
        }
        else {
            mUser2.setText(username);
            mScore2.setText(scoreText);
        }



        username = sharedPref.getString("name3", "Player3");
        score = sharedPref.getInt("highScore3", 0);
        scoreText = Integer.toString(score);

        if(score == 0) {
            mUser3.setVisibility(View.GONE);
        }
        else {

            mUser3.setText(username);
            mScore3.setText(scoreText);
        }

    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), HomeScreen.class);
        startActivityForResult(myIntent, 0);
        return true;
    }
}
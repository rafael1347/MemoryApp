package com.example.memorygame;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ButtonBarLayout;
import androidx.core.content.ContextCompat;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private MemoryGame mGame;
    private GridLayout mTileGrid;
    private TextView mRoundLabel;
    private TextView mScoreLabel;
    private int mLightOnColor;
    private int mLightOffColor;
    private int mWrongTileColor;
    private int mButtonClicks;
    private int mRound;
    private int mCounter;
    private int userScore;
    private int[] mRoundOrder;
    private CountDownTimer mTimer;
    private boolean run;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        mTileGrid = findViewById(R.id.tile_grid);
        mRoundLabel = findViewById(R.id.round);
        mScoreLabel = findViewById(R.id.score);
        Button newGameButton = findViewById(R.id.new_game_button);
        Button customButton = findViewById(R.id.change_color_button);

        // Add the same click handler to all grid buttons
        for(int buttonIndex = 0; buttonIndex < mTileGrid.getChildCount(); buttonIndex++) {
            Button tileButton = (Button) mTileGrid.getChildAt(buttonIndex);
            tileButton.setOnClickListener(this::onTileButtonClick);
        }

        newGameButton.setOnClickListener(this::onNewGameClick);
        customButton.setOnClickListener(this::onCustomizeClick);

        
        sharedPref = getSharedPreferences("myScores", Context.MODE_PRIVATE);
        String name = sharedPref.getString("name", "Player1");
        String name2 = sharedPref.getString("name2", "Player2");
        String name3 = sharedPref.getString("name3", "Player3");

        int score = sharedPref.getInt("highScore", 0);
        int score2 = sharedPref.getInt("highScore2", 0);
        int score3 = sharedPref.getInt("highScore3", 0);


        editor = sharedPref.edit();
        editor.putString("name", name);
        editor.putInt("highScore", score);
        editor.putString("name2", name2);
        editor.putInt("highScore2", score2);
        editor.putString("name3", name3);
        editor.putInt("highScore3", score3);
        editor.apply();


        mLightOnColor = R.color.yellow;
        mLightOffColor = ContextCompat.getColor(this, R.color.black);
        mWrongTileColor = ContextCompat.getColor(this, R.color.red);
        mButtonClicks = 0;
        mRound = 1;
        mCounter = 0;
        userScore = 0;
        run = false;

        mGame = new MemoryGame();
        setTilesOff();
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), HomeScreen.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

    private void runGame() {
        
        if(run) {
            mRoundOrder = mGame.newRound(mRound);
            String roundText = "Round " + mRound;
            mRoundLabel.setText(roundText);
            mCounter = 0;

            long time = mRound  * 1000;

            if (mTimer != null) {
                mTimer.cancel();
            }

            mTimer = new CountDownTimer(1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    run = false;

                    mTimer = new CountDownTimer(time, 1000) {

                        public void onTick(long millisUntilFinished) {

                            tileOn(mRoundOrder, mCounter);

                            if (mCounter > 0) {
                                tileOff(mRoundOrder, mCounter-1);
                            }

                            mCounter++;
                        }

                        public void onFinish() {
                            setTilesOff();
                            run = true;
                        }
                    }.start();
                }
            }.start();
        }
        else{
            Toast.makeText(this, R.string.try_again, Toast.LENGTH_LONG).show();
        }

    }

    public void tileOn(int[] order, int counter) {
        Button tileButton = (Button) mTileGrid.getChildAt(order[counter]);
        tileButton.setBackgroundColor(mLightOnColor);
    }

    public void tileOff(int[] order, int counter) {
        Button tileButton = (Button) mTileGrid.getChildAt(order[counter]);
        tileButton.setBackgroundColor(mLightOffColor);
    }

    public void setTilesOff(){

        int gridSize = mGame.GRID_SIZE;

        for(int i = 0; i < gridSize * gridSize; i++){
            Button tileButton = (Button) mTileGrid.getChildAt(i);
            tileButton.setBackgroundColor(mLightOffColor);
        }
    }

    private void onTileButtonClick(View view) {
        
        int buttonIndex = mTileGrid.indexOfChild(view);
        Button tileButton = (Button) mTileGrid.getChildAt(buttonIndex);
        
        if(run) {

            if (buttonIndex == mRoundOrder[mButtonClicks] && mButtonClicks == mRoundOrder.length - 1) {

                userScore++;
                String scoreText = "Score: " + userScore;
                mScoreLabel.setText(scoreText);

                long time = 500;

                if (mTimer != null) {
                    mTimer.cancel();
                }

                mTimer = new CountDownTimer(time, 500) {
                    public void onTick(long millisUntilFinished) {
                        tileButton.setBackgroundColor(mLightOnColor);
                    }

                    public void onFinish() {
                        setTilesOff();
                        mButtonClicks = 0;
                        mRound++;
                        runGame();
                    }
                }.start();


            }

            else if (buttonIndex == mRoundOrder[mButtonClicks] && mButtonClicks < mRoundOrder.length) {

                userScore++;
                String userText = "Score: " + userScore;
                mScoreLabel.setText(userText);

                long time = 400;

                if (mTimer != null) {
                    mTimer.cancel();
                }

                mTimer = new CountDownTimer(time, 400) {
                    public void onTick(long millisUntilFinished) {
                        tileButton.setBackgroundColor(mLightOnColor);
                    }

                    public void onFinish() {
                            setTilesOff();
                        }
                }.start();

                mButtonClicks++;

            }
            else {

                tileButton.setBackgroundColor(mWrongTileColor);
                run = false;
                mButtonClicks = 0;

                setHighScore();

                mRound = 1;
                runGame();

            }
        }
    }

    public void onNewGameClick(View view) {
        run = true;
        mRound = 1;
        userScore = 0;
        String userText = "Score: " + userScore;
        mScoreLabel.setText(userText);
        setTilesOff();
        runGame();
    }
    
    public void setHighScore(){
        int currentHigh = sharedPref.getInt("highScore3", 0);

        if(userScore > currentHigh){
            // Create a new AlertDialog builder
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            // Set the title of the dialog
            builder.setTitle("New high score!");

            // Set the message of the dialog
            builder.setMessage("Please enter your name:");

            // Create an EditText widget to accept the user's input
            final EditText input = new EditText(this);

            // Set the EditText widget as the input of the dialog
            builder.setView(input);

            // Set up the buttons for the dialog
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Get the user's input from the EditText widget
                    String playerName = input.getText().toString();

                    // Do something with the user's name (e.g. save it to a high score list)
                    int currentHigh = sharedPref.getInt("highScore2", 0);

                    if(userScore > currentHigh) {
                        currentHigh = sharedPref.getInt("highScore", 0);

                        if(userScore > currentHigh) {
                            sharedPref = getSharedPreferences("myScores", Context.MODE_PRIVATE);
                            editor = sharedPref.edit();

                            int score2 = sharedPref.getInt("highScore", 0);
                            String name2 = sharedPref.getString("name", null);

                            int score3 = sharedPref.getInt("highScore2", 0);
                            String name3 = sharedPref.getString("name2", null);


                            editor.putString("name", playerName);
                            editor.putInt("highScore", userScore);

                            editor.putString("name2", name2);
                            editor.putInt("highScore2", score2);

                            editor.putString("name3", name3);
                            editor.putInt("highScore3", score3);
                            editor.apply();
                        }
                        else {
                            sharedPref = getSharedPreferences("myScores", Context.MODE_PRIVATE);
                            editor = sharedPref.edit();

                            int score3 = sharedPref.getInt("highScore2", 0);
                            String name3 = sharedPref.getString("name2", null);

                            editor.putString("name2", playerName);
                            editor.putInt("highScore2", userScore);

                            editor.putString("name3", name3);
                            editor.putInt("highScore3", score3);
                            editor.apply();
                        }
                    }
                    else {
                        sharedPref = getSharedPreferences("myScores", Context.MODE_PRIVATE);
                        editor = sharedPref.edit();
                        editor.putString("name", playerName);
                        editor.putInt("highScore3", userScore);
                        editor.apply();
                    }
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Do nothing (the dialog will be dismissed automatically)
                }
            });

            // Show the dialog
            builder.show();

        }
    }
    
    public void onCustomizeClick(View view) {
        Intent intent = new Intent(this, ColorActivity.class);
        intent.putExtra(ColorActivity.EXTRA_COLOR, mLightOnColor);
        mColorResultLauncher.launch(intent);
    }

    private void setButtonColors() {

        for (int buttonIndex = 0; buttonIndex < mTileGrid.getChildCount(); buttonIndex++) {
            Button gridButton = (Button) mTileGrid.getChildAt(buttonIndex);

            // Find the button's row and col
            int row = buttonIndex / MemoryGame.GRID_SIZE;
            int col = buttonIndex % MemoryGame.GRID_SIZE;
        }
    }

    ActivityResultLauncher<Intent> mColorResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            // Create the "on" button color from the chosen color ID from ColorActivity
                            mLightOnColor = data.getIntExtra(ColorActivity.EXTRA_COLOR, R.color.yellow);
                            mLightOnColor = ContextCompat.getColor(MainActivity.this, mLightOnColor);
                            setButtonColors();
                        }
                    }
                }
            });

}

package com.example.memorygame;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ButtonBarLayout;
import androidx.core.content.ContextCompat;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private MemoryGame mGame;
    private GridLayout mTileGrid;
    private TextView mRoundLabel;
    private int mLightOnColor;
    private int mLightOffColor;
    private int mWrongTileColor;
    private int mButtonClicks;
    private int mRound;
    private int mCounter;
    private int[] mRoundOrder;
    private CountDownTimer mTimer;
    private boolean run;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mTileGrid = findViewById(R.id.tile_grid);
        mRoundLabel = findViewById(R.id.round);

        // Add the same click handler to all grid buttons
        for(int buttonIndex = 0; buttonIndex < mTileGrid.getChildCount(); buttonIndex++) {
            Button tileButton = (Button) mTileGrid.getChildAt(buttonIndex);
            tileButton.setOnClickListener(this::onTileButtonClick);
        }

        mLightOnColor = ContextCompat.getColor(this, R.color.yellow);
        mLightOffColor = ContextCompat.getColor(this, R.color.black);
        mWrongTileColor = ContextCompat.getColor(this, R.color.red);
        mButtonClicks = 0;
        mRound = 1;
        mCounter = 0;
        run = true;

        mGame = new MemoryGame();
        setTilesOff();
    }

    private void runGame() {
        if(run) {
            mRoundOrder = mGame.newRound(mRound);
            mCounter = 0;

            long time = mRound * 1000;

            if (mTimer != null) {
                mTimer.cancel();
            }

            mTimer = new CountDownTimer(time, 1000) {
                public void onTick(long millisUntilFinished) {
                    tileOn(mRoundOrder, mCounter);
                    if (mCounter > 0) {
                       tileOff(mRoundOrder, mCounter - 1);
                    }
                    mCounter++;
                }

                public void onFinish() {
                    setTilesOff();
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
        for(int i = 0; i < mGame.GRID_SIZE * mGame.GRID_SIZE; i++){
            Button tileButton = (Button) mTileGrid.getChildAt(i);
            tileButton.setBackgroundColor(mLightOffColor);
        }
    }

    private void onTileButtonClick(View view) {
        int buttonIndex = mTileGrid.indexOfChild(view);
        Button tileButton = (Button) mTileGrid.getChildAt(buttonIndex);
        if(run) {
            if (mButtonClicks == mRoundOrder.length - 1) {
                mButtonClicks = 0;
                mRound++;
                runGame();
            }
            else {
                if (buttonIndex == mRoundOrder[mButtonClicks]) {
                    if (mButtonClicks > 0) {
                        Button tileButton2 = (Button) mTileGrid.getChildAt(mRoundOrder[mButtonClicks - 1]);
                        tileButton2.setBackgroundColor(mLightOffColor);
                        tileButton.setBackgroundColor(mLightOnColor);
                    } else {
                        tileButton.setBackgroundColor(mLightOnColor);
                    }
                    mButtonClicks++;
                } else if (buttonIndex != mRoundOrder[mButtonClicks]) {
                    tileButton.setBackgroundColor(mWrongTileColor);
                    run = false;
                    mButtonClicks = 0;
                    mRound = 1;
                    runGame();
                }
            }
        }
    }

    public void onNewGameClick(View view) {
        run = true;
        setTilesOff();
        runGame();
    }

}

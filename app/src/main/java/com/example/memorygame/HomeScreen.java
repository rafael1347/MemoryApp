package com.example.memorygame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class HomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
    }

    public void onNewGameClick(View view) {
        Intent intent=new Intent(HomeScreen.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    
    public void onCustomizeClick(View view) {
        Intent intent=new Intent(HomeScreen.this, ColorActivity.class);
        startActivity(intent);
        finish();
    }
    
    public void onHowtoPlayClick(View view) {
        Intent intent=new Intent(HomeScreen.this, HowToActivity.class);
        startActivity(intent);
        finish();
    }
    
        public void onHighscoreButtonClick(View view) {
        Intent intent=new Intent(HomeScreen.this, HighScore.class);
        startActivity(intent);
        finish();
    }
}

package com.example.save_my_friend.view;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.save_my_friend.MainActivity;
import com.example.save_my_friend.R;
import com.example.save_my_friend.view.level2.GameView2;

public class ChangeLevel extends AppCompatActivity {
    TextView tvPoints;
    Button next_level;
    SharedPreferences sharedPreferences;
    Button quit_game;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uplevel);
        tvPoints = findViewById(R.id.your_point_view);
        next_level = findViewById(R.id.next_level_button);
        quit_game = findViewById(R.id.quit_game_button);
        int points = getIntent().getExtras().getInt("points");
        tvPoints.setText("" + points);
        sharedPreferences = getSharedPreferences("my_pref", 0);
        int highest = sharedPreferences.getInt("highest", 0);
        next_level.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GameView2 gameView2 = new GameView2(ChangeLevel.this, points);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                setContentView(gameView2);
//                finish();
            }
        });
        quit_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exit(view);
            }
        });
    }
    public void exit(View view) {
        finish();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

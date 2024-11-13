package com.example.save_my_friend;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

//import com.example.save_my_friend.view.Preview;
import com.example.save_my_friend.utils.ImageCache;
import com.example.save_my_friend.view.ScoreActivity;
import com.example.save_my_friend.view.level1.GameView;
import com.example.save_my_friend.view.level2.GameView2;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Button buttonShow = findViewById(R.id.startButton);
        Button score_button = findViewById(R.id.score_button);

        buttonShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameView gameview1 = new GameView(MainActivity.this);
                setContentView(gameview1);
            }
        });

        score_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ScoreActivity.class);
                startActivity(intent);
            }
        });

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImageCache.clearCache();  // Giải phóng bộ nhớ khi game bị dừng
    }
}

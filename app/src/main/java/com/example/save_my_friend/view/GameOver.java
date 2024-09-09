package com.example.save_my_friend.view;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.save_my_friend.MainActivity;
import com.example.save_my_friend.R;

public class GameOver extends AppCompatActivity {

    TextView tvPoints;
    TextView tvHighest;
    SharedPreferences sharedPreferences;
    ImageView ivNewHighest;
    Button try_again;
    Button exit_game;
    ImageView ivLight_winner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_over);
        tvPoints = findViewById(R.id.your_point_view);
        tvHighest = findViewById(R.id.highest_view);
        try_again = findViewById(R.id.try_again_button);
        exit_game = findViewById(R.id.exit_button);
        ivNewHighest = findViewById(R.id.No1NewHighest);
        ivLight_winner = findViewById(R.id.light_winner);

        int points = getIntent().getExtras().getInt("points");
        tvPoints.setText("" + points);
        sharedPreferences = getSharedPreferences("my_pref", 0);
        int highest = sharedPreferences.getInt("highest", 0);

        try_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restart(view);
            }
        });
        exit_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exit(view);
            }
        });

        if (points > highest) {
            ivLight_winner.setVisibility(View.VISIBLE);
            animateView(ivLight_winner, 2000);
            ivNewHighest.setVisibility(View.VISIBLE);
            highest = points;
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("highest", highest);
            editor.commit();
        }
        tvHighest.setText("" + highest);
    }

    public void restart(View view) {
        Intent intent = new Intent(GameOver.this, MainActivity.class);
        startActivity(intent);
        finish();


    }

    public void exit(View view) {
        finish();
    }

    private void animateView(ImageView view, long duration) {
        // Tạo ObjectAnimator cho xoay
        ObjectAnimator rotationAnimator = ObjectAnimator.ofFloat(view, "rotation", 0f, 360f);
        rotationAnimator.setDuration(duration);

        // Bắt đầu cả hai bộ animation
        rotationAnimator.start();

    }
}


package com.example.save_my_friend.view;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.save_my_friend.MainActivity;
import com.example.save_my_friend.R;
import com.example.save_my_friend.SQLiteHelper;

public class GameOver extends AppCompatActivity {

    TextView tvPoints;
    TextView tvHighest;
    SharedPreferences sharedPreferences;
    ImageView ivNewHighest;
    Button try_again;
    Button save_point;
    Button exit_game;
    ImageView ivLight_winner;
int points;

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
        save_point = findViewById(R.id.save_point);
        this.points = getIntent().getExtras().getInt("points");
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

        save_point.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNameInputDialog();
//                exit(view);
            }
        });
    }

    public void restart(View view) {
        Intent intent = new Intent(GameOver.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void showNameInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Game Over");
        builder.setMessage("Nhập tên của bạn để lưu điểm:");

        // Tạo input cho người chơi nhập tên
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Nút "Lưu" để lưu tên và điểm
        builder.setPositiveButton("Lưu", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String playerName = input.getText().toString();
                // Lưu tên và điểm vào SQLite
                SQLiteHelper SQLiteHelper = new SQLiteHelper(GameOver.this);
                SQLiteHelper.addScore(playerName, points);
                // Đóng Activity sau khi lưu
                Intent intent = new Intent(GameOver.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Nút "Hủy" để không lưu điểm
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
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


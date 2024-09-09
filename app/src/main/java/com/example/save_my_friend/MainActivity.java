package com.example.save_my_friend;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
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
import com.example.save_my_friend.view.level1.GameView;
import com.example.save_my_friend.view.level2.GameView2;


public class MainActivity extends AppCompatActivity {
    private ImageView smoke;
    private ImageView smoke1;
    private ImageView spike;
    private ImageView spike1;
//    private Preview preview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        // Khởi tạo Preview và thêm vào nội dung của Activity


        spike = findViewById(R.id.spike_menu);
        spike1 = findViewById(R.id.spike_menu1);
        smoke = findViewById(R.id.smoke);
        smoke1 = findViewById(R.id.smoke1);
        Button buttonShow = findViewById(R.id.startButton);
        Button lv2_button = findViewById(R.id.lv2_button);

        buttonShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSmokeAndSmoke1();
                startAnimation();
                GameView gameview1 = new GameView(MainActivity.this);
                setContentView(gameview1);
                // startGame();
            }
        });
        lv2_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GameView2 gameView2 = new GameView2(MainActivity.this);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                setContentView(gameView2);
            }
        });

    }


    private void showSmokeAndSmoke1() {
        // Hiện smoke
        smoke1.setVisibility(View.VISIBLE);

        // Tạo handler để delay 0.5 giây trước khi hiện smoke1
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                smoke1.setVisibility(View.GONE);
                smoke.setVisibility(View.VISIBLE);

            }
        }, 500); // Delay 500ms = 0.5 giây

    }

    private void startAnimation() {
        animateView(spike, 1000, 100f);
        animateView(spike1, 1000, -150f);
    }

    private void animateView(ImageView view, long duration, float translationYValue) {
        // Tạo ObjectAnimator cho xoay
        ObjectAnimator rotationAnimator = ObjectAnimator.ofFloat(view, "rotation", 0f, 360f);
        rotationAnimator.setDuration(duration);

        // Tạo ObjectAnimator cho di chuyển
        PropertyValuesHolder translationY = PropertyValuesHolder.ofFloat("translationY", 0f, translationYValue);
        ObjectAnimator translationAnimator = ObjectAnimator.ofPropertyValuesHolder(view, translationY);
        translationAnimator.setDuration(duration);

        // Bắt đầu cả hai bộ animation
        rotationAnimator.start();
        translationAnimator.start();
    }

//    public void startGame(View view) {
//        try {
//            if (view instanceof GameView) {
//                setContentView((GameView) view);
//            } else if (view instanceof GameView2) {
//                setContentView((GameView2) view);
//            } else {
//                throw new IllegalArgumentException("Invalid view type");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.d("GameViewError", "Error starting game view: " + e.getMessage());
//        }
//    }

}

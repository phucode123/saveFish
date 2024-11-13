package com.example.save_my_friend.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.save_my_friend.MainActivity;
import com.example.save_my_friend.R;
import com.example.save_my_friend.SQLiteHelper;
import android.widget.ArrayAdapter;
import android.widget.Button;
import java.util.List;

public class ScoreActivity extends AppCompatActivity {
    private ListView scoreListView;
    private SQLiteHelper dbHelper;
    private  Button back;
//    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        back = findViewById(R.id.button_back_main);
        scoreListView = findViewById(R.id.scoreListView);
        dbHelper = new SQLiteHelper(this);

        // Lấy danh sách điểm từ SQLite
        List<String> scores = dbHelper.getAllScores();
        // Sử dụng ArrayAdapter để hiển thị danh sách điểm trong ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, scores);
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, scores);
        scoreListView.setAdapter(adapter);
        back.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(ScoreActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    });
    }




}

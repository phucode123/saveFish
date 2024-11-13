package com.example.save_my_friend;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "game_scores.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_SCORES = "scores";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_SCORE = "score";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng scores nếu chưa tồn tại
        String CREATE_SCORES_TABLE = "CREATE TABLE " + TABLE_SCORES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NAME + " TEXT, "
                + COLUMN_SCORE + " INTEGER)";
        db.execSQL(CREATE_SCORES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Xóa bảng cũ nếu có và tạo bảng mới
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCORES);
        onCreate(db);
    }

    // Thêm điểm mới vào cơ sở dữ liệu
    public void addScore(String name, int score) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_SCORE, score);
        db.insert(TABLE_SCORES, null, values);
        db.close();
    }

    // Lấy tất cả điểm trong cơ sở dữ liệu
    public List<String> getAllScores() {
        List<String> scoreList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT name, score FROM " + TABLE_SCORES + " ORDER BY score DESC", null);

        int position = 1; // Bắt đầu đếm số thứ tự từ 1
        if (cursor.moveToFirst()) {
            do {
                String playerName = cursor.getString(0);
                int score = cursor.getInt(1);
                // Thêm số thứ tự vào chuỗi hiển thị
                scoreList.add(position + ". " + playerName + ": " + score);
                position++; // Tăng số thứ tự
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return scoreList;
    }

}

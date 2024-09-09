package com.example.save_my_friend.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.example.save_my_friend.utils.ImageUtils;
import com.example.save_my_friend.R;

public class Dizzy {
    private Bitmap dizzyBitmap;
    private float x, y;
    private boolean isVisible;

    public Dizzy(Context context) {
        dizzyBitmap = ImageUtils.getResizedBitmap(context, R.drawable.dizzy, 0.2f, 0.2f);;
        isVisible = false;
    }

    public void show(float fishX, float fishY) {
        x = fishX;
        y = fishY;
        isVisible = true;
    }

    public void hide() {
        isVisible = false;
    }

    public void draw(Canvas canvas) {
        if (isVisible) {
            canvas.drawBitmap(dizzyBitmap, x, y - 100, null);
        }
    }
}

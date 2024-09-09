package com.example.save_my_friend.model;

import android.content.Context;
import android.graphics.Bitmap;

import com.example.save_my_friend.utils.ImageUtils;
import com.example.save_my_friend.R;

public class Explosion {
    public Bitmap explosion[] = new Bitmap[4];
    public int explosionFrame = 0;
    public int explosionX, explosionY;

    public Explosion(Context context) {
        // Sử dụng ImageUtils để điều chỉnh kích thước hình ảnh
        explosion[0] = ImageUtils.getResizedBitmap(context, R.drawable.smoke, 0.2f, 0.2f); // Tỷ lệ kích thước hình ảnh
        explosion[1] = ImageUtils.getResizedBitmap(context, R.drawable.smoke1, 0.2f, 0.2f); // Tỷ lệ kích thước hình ảnh
        explosion[2] = ImageUtils.getResizedBitmap(context, R.drawable.smoke, 0.2f, 0.2f); // Tỷ lệ kích thước hình ảnh
        explosion[3] = ImageUtils.getResizedBitmap(context, R.drawable.smoke1, 0.2f, 0.2f); // Tỷ lệ kích thước hình ảnh
    }

    public Bitmap getExplosion(int explosionFrame) {
        return explosion[explosionFrame];
    }
}

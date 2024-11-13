package com.example.save_my_friend.utils;  // Thay đổi package nếu cần thiết

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.HashMap;

public class ImageCache {
    // Sử dụng HashMap để lưu trữ các Bitmap
    private static HashMap<Integer, Bitmap> cache = new HashMap<>();

    // Phương thức để lấy Bitmap từ cache hoặc tải nếu chưa có
    public static Bitmap getBitMap(Context context, int resId) {
        if (!cache.containsKey(resId)) {
            // Nếu chưa có trong cache thì tải ảnh từ tài nguyên
            cache.put(resId, ImageUtils.getResizedBitmap(context, resId, 0.5f, 0.5f));
        }
        // Trả về Bitmap đã được lưu trữ trong cache
        return cache.get(resId);
    }

    // Phương thức để dọn dẹp bộ nhớ, giải phóng các Bitmap khi không cần nữa
    public static void clearCache() {
        for (Bitmap bitmap : cache.values()) {
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();  // Giải phóng bộ nhớ
            }
        }
        cache.clear();  // Xóa hết cache
    }
}

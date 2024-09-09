package com.example.save_my_friend.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.util.Log;

public class ImageUtils {
    // căn chỉnh hình aảnh cho khớp với màn hình
    public static Bitmap getResizedBitmap(Context context, int resourceId, float widthRatio, float heightRatio) {
        // Lấy kích thước màn hình
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int screenWidth = metrics.widthPixels;
        int screenHeight = metrics.heightPixels;

        // Lấy kích thước của hình ảnh gốc
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), resourceId, options);
        int originalWidth = options.outWidth;
        int originalHeight = options.outHeight;

        if (originalWidth <= 0 || originalHeight <= 0) {
            Log.e("ImageUtils", "Invalid original dimensions: width=" + originalWidth + ", height=" + originalHeight);
            return null;
        }

        // Tính toán kích thước mới theo tỷ lệ của màn hình
        int newWidth = (int) (screenWidth * widthRatio);
        int newHeight = (int) (screenHeight * heightRatio);

        // Tính toán tỷ lệ giảm kích thước
        float widthScale = (float) newWidth / originalWidth;
        float heightScale = (float) newHeight / originalHeight;
        float scale = Math.min(widthScale, heightScale);

        // Tính toán kích thước mới để giữ tỷ lệ khung hình
        int finalWidth = (int) (originalWidth * scale);
        int finalHeight = (int) (originalHeight * scale);

        // Đọc lại hình ảnh với tỷ lệ giảm kích thước
        options.inSampleSize = calculateInSampleSize(options, finalWidth, finalHeight);
        options.inJustDecodeBounds = false;
        Bitmap resizedBitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);

        if (resizedBitmap == null) {
            Log.e("ImageUtils", "Failed to decode resource");
            return null;
        }

        // Tạo bitmap mới với kích thước chính xác
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(resizedBitmap, finalWidth, finalHeight, true);

        if (scaledBitmap == null) {
            Log.e("ImageUtils", "Failed to create scaled bitmap");
        }

        return scaledBitmap;
    }
    public static Bitmap getResizedBitmapByWidth(Context context, int resourceId, int targetWidth) {
        // Lấy kích thước của hình ảnh gốc
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), resourceId, options);
        int originalWidth = options.outWidth;
        int originalHeight = options.outHeight;

        if (originalWidth <= 0 || originalHeight <= 0) {
            Log.e("ImageUtils", "Invalid original dimensions: width=" + originalWidth + ", height=" + originalHeight);
            return null;
        }

        // Tính toán tỷ lệ giảm kích thước dựa trên chiều rộng
        float scale = (float) targetWidth / originalWidth;

        // Tính toán chiều cao mới để giữ tỷ lệ khung hình
        int finalWidth = targetWidth;
        int finalHeight = (int) (originalHeight * scale);

        // Đọc lại hình ảnh với tỷ lệ giảm kích thước
        options.inSampleSize = calculateInSampleSize(options, finalWidth, finalHeight);
        options.inJustDecodeBounds = false;
        Bitmap resizedBitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);

        if (resizedBitmap == null) {
            Log.e("ImageUtils", "Failed to decode resource");
            return null;
        }

        // Cắt hoặc làm đầy nếu cần thiết để đạt được kích thước cuối cùng
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(resizedBitmap, finalWidth, finalHeight, true);

        if (scaledBitmap == null) {
            Log.e("ImageUtils", "Failed to create scaled bitmap");
        }

        return scaledBitmap;
    }



    // Tính toán giá trị inSampleSize để giảm kích thước hình ảnh
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Chiều rộng và chiều cao của hình ảnh gốc
        final int width = options.outWidth;
        final int height = options.outHeight;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Tính toán tỷ lệ giảm kích thước
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}

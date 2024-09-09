package com.example.save_my_friend.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.example.save_my_friend.R;
import com.example.save_my_friend.model.weapons.Animation;
import com.example.save_my_friend.model.weapons.Animation;

public class AnimationUtils {
    public Animation currentAni;
  //  public double frame = 0.5;
    private long lastFrameChangeTime;
    private int currentFrameIndex= 0;
   // private long lastFrameTime;
    private boolean isAnimating = false;
    private long frameDuration = 100; // Thời gian mỗi frame (ms)
    private AnimationCallback callback;

    private int countFrame = 0;
    private boolean isSwordActive = true;
    public AnimationUtils(Context context, AnimationCallback callback) {
        // Khởi tạo các frame cho thanh kiếm và khẩu súng
        this.callback = callback;
//        currentAni = new Animation(new Bitmap[]{
//                getBitMap(context, R.drawable.spire_lv2_1),
//                getBitMap(context, R.drawable.spire_lv2_2),
//                getBitMap(context, R.drawable.spire_lv2_3),
//                getBitMap(context, R.drawable.spire_lv2_4),
//                getBitMap(context, R.drawable.spire_lv2_5),
//                getBitMap(context, R.drawable.spire_lv2_6),
//                getBitMap(context, R.drawable.spire_lv2_7),
//                getBitMap(context, R.drawable.spire_lv2_8),
//        });
//        this.currentAni = currentAni;
//        gun = new Animation(new Bitmap[]{
//                getBitMap(context, R.drawable.gun_lv2_1),
//                getBitMap(context, R.drawable.gun_lv2_2),
//                getBitMap(context, R.drawable.gun_lv2_3),
//                getBitMap(context, R.drawable.gun_lv2_4),
//                getBitMap(context, R.drawable.gun_lv2_5),
//                getBitMap(context, R.drawable.gun_lv2_6),
//                getBitMap(context, R.drawable.gun_lv2_7),
//                getBitMap(context, R.drawable.gun_lv2_8),
//                // Add other frames
//        });
    }

    public static Animation AnimationUnitys(String nameU){
        return null;
    }
    public static Bitmap getBitMap(Context context, int idPng) {
        return ImageUtils.getResizedBitmap(context, idPng, 0.5f, 0.5f);
    }

    public void update() {
        if (isAnimating) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastFrameChangeTime > frameDuration) {
//                Log.d("currentIndex", "update: "+currentFrameIndex+ " "+ currentAni.getFrameCount());
                currentFrameIndex++;
                this.currentAni.nextFrame();
                lastFrameChangeTime = currentTime;
                if (currentFrameIndex >= currentAni.getFrameCount()) {
                    Log.d("weapons", "update: lần thứ"+ currentFrameIndex);
                    currentFrameIndex = 0;
                    isAnimating = false;
                    if (callback != null) {
                        callback.onAnimationComplete(); // Gọi callback khi hoàn tất
                    }
                }
            }
        }
    }
    public void startAnimation() {
        currentFrameIndex = 0;
        lastFrameChangeTime = System.currentTimeMillis();
        isAnimating = true;
    }
    public Bitmap getCurrentAni() {
        return currentAni.getCurrentFrame();
    }

    public void setCurrentAni(Animation currentAni) {
        this.currentAni = currentAni;
    }

    public void resetAnimation() {
        currentFrameIndex = 0;
       // lastFrameTime = System.currentTimeMillis();
    }
    public boolean isAnimating() {
        return isAnimating;
    }
    public void setAnimating(boolean animating) {
        isAnimating = animating;
    }

}

package com.example.save_my_friend.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.example.save_my_friend.R;
import com.example.save_my_friend.model.weapons.Animation;
import com.example.save_my_friend.model.weapons.Animation;

public class AnimationUtils {
    private Animation weaponAnimation;
    private Animation bossAnimation;
    private long lastFrameChangeTimeWeapon;
    private long lastFrameChangeTimeBoss;
    private int currentFrameIndex= 0;
    private int weaponFrameIndex = 0;
    private int bossFrameIndex = 0;
    // private long lastFrameTime;
    private boolean isAnimating = false;
    private long frameDuration = 100; // Thời gian mỗi frame (ms)
    private AnimationCallback callback;
    public AnimationUtils(Context context, AnimationCallback callback) {
        this.callback = callback;
    }
    public static Animation AnimationUnitys(String nameU){
        return null;
    }
    public static Bitmap getBitMap(Context context, int idPng) {
        return ImageUtils.getResizedBitmap(context, idPng, 0.5f, 0.5f);
    }
    public void update() {
        if (isAnimating) {
            //Log.d("Vu khi", "drawContent: frame vu khi so: " );
            //
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastFrameChangeTimeWeapon > frameDuration) {
                //Log.d("currentIndex", "update: "+weaponFrameIndex+ " "+ weaponAnimation.getFrameCount());
                weaponFrameIndex++;
                this.weaponAnimation.nextFrame();
                lastFrameChangeTimeWeapon = currentTime;
                if (weaponFrameIndex >= weaponAnimation.getFrameCount()) {
                    //  Log.d("weapons", "update: lần thứ"+ currentFrameIndex);
                    weaponFrameIndex = 0;
                    isAnimating = false;
                    if (callback != null) {
                      //  Log.d("TAG", "update: weapon ket thuc");
                        callback.onAnimationComplete(); // Gọi callback khi hoàn tất
                    }
                }
            }
        }
    }
    public void updateBoss() {
        long currentTime = System.currentTimeMillis();
        //Log.d("TAG", "updateBoss: time: "+ lastFrameChangeTimeBoss);
        if (currentTime - lastFrameChangeTimeBoss > 500) {
               //Log.d("currentIndex", "update: "+bossFrameIndex+ " "+ bossAnimation.getFrameCount()+ "time: "+ lastFrameChangeTimeBoss);
            bossFrameIndex++;
            this.bossAnimation.nextFrame();
            lastFrameChangeTimeBoss = currentTime;
            if (bossFrameIndex >= bossAnimation.getFrameCount()) {
                //Log.d("weapons", "update: lần thứ"+ currentFrameIndex);
                bossFrameIndex = 0;
            }
        }
    }
    public void startAnimation() {
        weaponFrameIndex = 0;
        lastFrameChangeTimeWeapon = System.currentTimeMillis();
        isAnimating = true;
    }
    // Khởi tạo animation cho boss
    public void startBossAnimation() {
        bossFrameIndex = 0;
        lastFrameChangeTimeBoss = System.currentTimeMillis(); // Khởi tạo thời gian cho boss
        isAnimating = true;
    }

//


    public Bitmap getWeaponAnimation() {
        return weaponAnimation.getCurrentFrame();
    }

    public void setWeaponAnimation(Animation weaponAnimation) {
        this.weaponAnimation = weaponAnimation;
    }

    public Bitmap getBossAnimation() {
        return bossAnimation.getCurrentFrame();
    }

    public void setBossAnimation(Animation bossAnimation) {
        this.bossAnimation = bossAnimation;
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

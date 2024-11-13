package com.example.save_my_friend.model.weapons;

import android.content.Context;
import android.graphics.Bitmap;

import com.example.save_my_friend.R;
import com.example.save_my_friend.utils.ImageCache;

public class WeaponChoice {
    Animation gunleft, gunright;
    Animation swordleft, swordright;
    Context context;

    public WeaponChoice(Context context) {
        this.context = context;
        setAnimations(context);
    }

    public void setAnimations(Context context) {
        setGunleft(new Animation(new Bitmap[]{
                ImageCache.getBitMap(context, R.drawable.gun_lv2_1left),
                ImageCache.getBitMap(context, R.drawable.gun_lv2_2left),
                ImageCache.getBitMap(context, R.drawable.gun_lv2_3left),
                ImageCache.getBitMap(context, R.drawable.gun_lv2_4left),
                ImageCache.getBitMap(context, R.drawable.gun_lv2_5left),
                ImageCache.getBitMap(context, R.drawable.gun_lv2_6left),
                ImageCache.getBitMap(context, R.drawable.gun_lv2_7left),
                ImageCache.getBitMap(context, R.drawable.gun_lv2_8left),
        }));
        setGunright(new Animation(new Bitmap[]{
                ImageCache.getBitMap(context, R.drawable.gun_lv2_1),
                ImageCache.getBitMap(context, R.drawable.gun_lv2_2),
                ImageCache.getBitMap(context, R.drawable.gun_lv2_3),
                ImageCache.getBitMap(context, R.drawable.gun_lv2_4),
                ImageCache.getBitMap(context, R.drawable.gun_lv2_5),
                ImageCache.getBitMap(context, R.drawable.gun_lv2_6),
                ImageCache.getBitMap(context, R.drawable.gun_lv2_7),
                ImageCache.getBitMap(context, R.drawable.gun_lv2_8),
        }));
        setSwordleft(new Animation(new Bitmap[]{
                ImageCache.getBitMap(context, R.drawable.spire_lv2_1left),
                ImageCache.getBitMap(context, R.drawable.spire_lv2_2left),
                ImageCache.getBitMap(context, R.drawable.spire_lv2_3left),
                ImageCache.getBitMap(context, R.drawable.spire_lv2_4left),
                ImageCache.getBitMap(context, R.drawable.spire_lv2_5left),
                ImageCache.getBitMap(context, R.drawable.spire_lv2_6left),
                ImageCache.getBitMap(context, R.drawable.spire_lv2_7left),
                ImageCache.getBitMap(context, R.drawable.spire_lv2_8left),
        }));
        setSwordright(new Animation(new Bitmap[]{
                ImageCache.getBitMap(context, R.drawable.spire_lv2_1),
                ImageCache.getBitMap(context, R.drawable.spire_lv2_2),
                ImageCache.getBitMap(context, R.drawable.spire_lv2_3),
                ImageCache.getBitMap(context, R.drawable.spire_lv2_4),
                ImageCache.getBitMap(context, R.drawable.spire_lv2_5),
                ImageCache.getBitMap(context, R.drawable.spire_lv2_6),
                ImageCache.getBitMap(context, R.drawable.spire_lv2_7),
                ImageCache.getBitMap(context, R.drawable.spire_lv2_8),
        }));
    }

    public Animation getGunleft() {
        return gunleft;
    }
    public void setGunleft(Animation gunleft) {
        this.gunleft = gunleft;
    }
    public Animation getGunright() {
        return gunright;
    }
    public void setGunright(Animation gunright) {
        this.gunright = gunright;
    }
    public Animation getSwordleft() {
        return swordleft;
    }
    public void setSwordleft(Animation swordleft) {
        this.swordleft = swordleft;
    }
    public Animation getSwordright() {
        return swordright;
    }

    public void setSwordright(Animation swordright) {
        this.swordright = swordright;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}

package com.example.save_my_friend.model.weapons;

import android.content.Context;
import android.graphics.Bitmap;

import com.example.save_my_friend.R;
import com.example.save_my_friend.utils.ImageUtils;

public class Animation {

    private Bitmap[] frames;
    private int currentFrame;
    private int frameCount;

    public Animation(Bitmap[] frames) {
        this.frames = frames;
        this.currentFrame = 0;
        this.frameCount = frames.length;
    }

    public Bitmap getCurrentFrame() {
        return frames[currentFrame];
    }

    public int getFrameCount() {
        return frameCount;
    }

    public void nextFrame() {
        currentFrame = (currentFrame + 1) % frames.length;
    }

    public void reset() {
        currentFrame = 0;
    }
}

package com.example.save_my_friend.model;

import android.content.Context;
import android.graphics.Bitmap;

import com.example.save_my_friend.view.level1.GameView;
import com.example.save_my_friend.utils.ImageUtils;
import com.example.save_my_friend.R;

import java.util.Random;

public class Spike {
    Bitmap spike[] = new Bitmap[3];
    public int spikeFrame = 0;
    public int spikeX, spikeY, spikeVelocity;
    public  Random random;
    public int spikeWidth, spikeHeight;
    public Spike(Context context) {
        spike[0] = ImageUtils.getResizedBitmap(context, R.drawable.spike, 0.15f, 0.15f); // Tỷ lệ kích thước hình ảnh
        spike[1] = ImageUtils.getResizedBitmap(context, R.drawable.stone, 0.15f, 0.15f); // Tỷ lệ kích thước hình ảnh
        spike[2] = ImageUtils.getResizedBitmap(context, R.drawable.spike, 0.15f, 0.15f); // Tỷ lệ kích thước hình ảnh
        random = new Random();
//        randomizeSpike();
        resetPosition();

    }

    public Bitmap getSpike(int spikeFrame) {
        return spike[spikeFrame];
    }

    public int getSpikeWidth() {
        return spike[0].getWidth();
    }

    public int getSpikeHeight(){
        return spike[0].getHeight();
    }

    public void resetPosition() {
        spikeX = random.nextInt(GameView.dWidth - getSpikeWidth());

        spikeY = -200 + random.nextInt(600) * -1;
        spikeVelocity = 35 + random.nextInt(16);
    }
    public void randomizeSpike() {
        spikeFrame = random.nextInt(spike.length);
        // Cập nhật kích thước dựa trên hình ảnh hiện tại
        Bitmap currentSpike = spike[spikeFrame];
        spikeWidth = currentSpike.getWidth();
        spikeHeight = currentSpike.getHeight();
    }


}

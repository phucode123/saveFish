package com.example.save_my_friend.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import com.example.save_my_friend.utils.ImageUtils;
import com.example.save_my_friend.R;

public class Fish {
    Bitmap fish, fishLeft, fishRight;
    float fishX, fishY;
    float fishW, fishH;
    float oldX, oldFishX, oldY, oldFishY;
    Context context;
    int dWidth;
    boolean movingLeft, movingRight;
    float screenWidth, screenHeight;
    private boolean isMoving = false;

    public Fish(Context context, int dWidth, int dHeight, int groundHeight) {
        this.screenWidth = dWidth;
        this.screenHeight = dHeight;
        // Sử dụng hàm chung để xử lý và thay đổi kích thước các hình ảnh
        this.fish = ImageUtils.getResizedBitmap(context, R.drawable.fish_sad, 0.2f, 0.2f);
        this.fishLeft = ImageUtils.getResizedBitmap(context, R.drawable.fish_l, 0.21f, 0.25f);
        this.fishRight = ImageUtils.getResizedBitmap(context, R.drawable.fish_r, 0.21f, 0.25f);
        if (this.fish != null) {
            setFishH(this.fish.getHeight());
            setFishW(this.fish.getWidth());
            this.fishX = this.screenWidth / 2 - this.fish.getWidth() / 2;
            this.fishY = this.screenHeight - groundHeight - this.fish.getHeight();
            setFishX(this.fishX);
            setFishY(this.fishY);
        } else {
            Log.e("Fish", "Fish bitmap is null");
        }
    }

    public Bitmap getFishImage() {
        if (movingLeft) {
            return fishLeft;
        } else if (movingRight) {
            return fishRight;
        } else {
            return fish;
        }
    }

    public void draw(Canvas canvas, int status) {
        //1 là vẽ quay trái, -1 là quay phải, 0 là nhi
        if (status == 1) {
            setFish(fishRight);
        }
        if (fish != null && !fish.isRecycled()) {

            canvas.drawBitmap(fish, this.getFishX(), this.getFishY(), null);
            // canvas.drawBitmap(bitmap, x, y, paint);
        } else {
            // Log hoặc xử lý trường hợp bitmap bị null hoặc đã bị recycled
            Log.e("Fish", "Bitmap is null or recycled");
        }
    }

    public void setFishW(float fishW) {
        this.fishW = fishW;
    }

    public void setFishH(float fishH) {
        this.fishH = fishH;
    }

    public void setFish(Bitmap fish) {
        this.fish = fish;
    }

    public void setFishLeft(Bitmap fishLeft) {
        this.fishLeft = fishLeft;
    }

    public void setFishRight(Bitmap fishRight) {
        this.fishRight = fishRight;
    }

    public void setFishX(float fishX) {
        this.fishX = fishX;
    }

    public void setFishY(float fishY) {
        this.fishY = fishY;
    }

    public void setOldX(float oldX) {
        this.oldX = oldX;
    }

    public void setOldFishX(float oldFishX) {
        this.oldFishX = oldFishX;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setdWidth(int dWidth) {
        this.dWidth = dWidth;
    }

    public void setMovingLeft(boolean movingLeft) {
        this.movingLeft = movingLeft;
    }

    public void setMovingRight(boolean movingRight) {
        this.movingRight = movingRight;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }

    public Bitmap getFish() {
        return fish;
    }

    public Bitmap getFishLeft() {
        return fishLeft;
    }

    public Bitmap getFishRight() {
        return fishRight;
    }

    public float getOldX() {
        return oldX;
    }

    public float getOldFishX() {
        return oldFishX;
    }

    public Context getContext() {
        return context;
    }

    public int getdWidth() {
        return dWidth;
    }

    public boolean isMovingLeft() {
        return movingLeft;
    }

    public boolean isMovingRight() {
        return movingRight;
    }

    public float getScreenWidth() {
        return screenWidth;
    }

    public float getScreenHeight() {
        return screenHeight;
    }
    public float getFishX() {
        return fishX;
    }
    public float getFishY() {
        return fishY;
    }
    public float getFishW() {
        return fishW;
    }
    public float getFishH() {
        return fishH;
    }
    public void handleTouch(float touchX, float touchY, int dWidth) {
        if (touchY >= fishY) {
            float shift = oldX - touchX;
            float newFishX = oldFishX - shift;
            // Cập nhật hình ảnh cá dựa trên hướng di chuyển
            if (touchX > oldX) {
                Log.d("TouchDirection", "Moving right");
                // Cập nhật hình ảnh cá sang phải
                this.setFish(fishRight);
            } else if (touchX < oldX) {
                Log.d("TouchDirection", "Moving left");
                // Cập nhật hình ảnh cá sang trái
                this.setFish(fishLeft);
            }
            if (newFishX <= 0) {
                setFishX(0);
            } else if (newFishX >= dWidth - getFishW()) {
                setFishX(dWidth - getFishW());
            } else {
                setFishX(newFishX);
            }
            oldX = touchX;
            oldFishX = newFishX;
        }
    }
    public void handleTouch(float touchX, float touchY, int dWidth, int dHeight) {
        // Xử lý theo chiều ngang (trái/phải)
        float shiftX = oldX - touchX;
        float newFishX = oldFishX - shiftX;
        if (touchX > oldX) {
            Log.d("con ca", "handleTouch: sang phai");
            setFish(this.fishRight);
        } else if (touchX < oldX) {
            Log.d("con ca", "handleTouch: sang trai");
            setFish(this.fishLeft);
        }
        // Xử lý theo chiều dọc (lên/xuống)
        float shiftY = oldY - touchY;
        float newFishY = oldFishY - shiftY;
        // Kiểm tra ranh giới và cập nhật vị trí mới
        setFishX(Math.max(0, Math.min(newFishX, dWidth - getFishW())));
        setFishY(Math.max(0, Math.min(newFishY, dHeight - getFishH())));
        // Cập nhật lại các giá trị cũ
        oldX = touchX;
        oldY = touchY;
        oldFishX = newFishX;
        oldFishY = newFishY;
    }
}

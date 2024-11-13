package com.example.save_my_friend.model.bossLv2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.SurfaceHolder;

import com.example.save_my_friend.R;
import com.example.save_my_friend.model.weapons.Animation;
import com.example.save_my_friend.utils.AnimationCallback;
import com.example.save_my_friend.utils.AnimationUtils;
import com.example.save_my_friend.utils.ImageUtils;

public class Boss {
    float bossX, bossY;
    float wBoss, hBoss;
    Bitmap Boss, Bosshited;
    AnimationUtils BossAni;
    Context context;
    float screenWidth, screenHeight;
    int  life = 100;
    private SurfaceHolder surfaceHolder;
    Rect hitbox;
    Paint healthPaint= new Paint();
    private long hitTime = 0;
    private long hitDuration = 500; // 0.5 giây
    Boolean isHited = false;
    float playerX, playerY;
    private  Context thisContext;
    float speed = 4;
    boolean attacked= false;
    private static final float WIDTH_SCALE = 0.7f;
    private static final float HEIGHT_SCALE = 0.35f;
    public Boss(Context context, int dWidth, int dHeight, float x, float y, int health, int attackPower, int defense, AnimationCallback callback) {
        this.Bosshited = ImageUtils.getResizedBitmap(context, R.drawable.boss_hited,0.65f, 0.325f);
        this.BossAni = new AnimationUtils(context, callback);
        this.thisContext = context;
        this.setBossNoMove();
        // Lấy kích thước từ frame đầu tiên của animation
        Bitmap firstFrame = BossAni.getBossAnimation();
        if (firstFrame != null) {
            this.wBoss = firstFrame.getWidth(); // Gán chiều rộng
            this.hBoss = firstFrame.getHeight(); // Gán chiều cao
        } else {
           // Log.e("Boss", "First frame bitmap is null");
        }
        this.screenWidth = dWidth;
        this.screenHeight = dHeight;
        this.bossX = x;
        this.bossY = y;
        // Gán lại giá trị cho boss
        sethBoss(this.hBoss);
        setwBoss(this.wBoss);
        setBossX(this.bossX);
        setBossY(this.bossY);
        updateHitbox();
        // Thiết lập trạng thái ban đầu
        setBossNoMove();
    }
    // Phương thức chung để thiết lập animation
    private void setBossAnimation(int[] drawableIds) {
        Bitmap[] frames = new Bitmap[drawableIds.length];
        for (int i = 0; i < drawableIds.length; i++) {
            frames[i] = getBitMap(thisContext, drawableIds[i]);
        }
        BossAni.setBossAnimation(new Animation(frames));
    }

    public void setBossNoMove() {
        setBossAnimation(new int[]{
                R.drawable.boss_attack2,
                R.drawable.boss_attack1,
                R.drawable.boss_attack2,
                R.drawable.boss_attack2,
                R.drawable.boss_attack1,
                R.drawable.boss_attack2,

        });
    }

    public void setBossGoRight() {
        setBossAnimation(new int[]{
                R.drawable.boss_goright2,
                R.drawable.boss_1,
                R.drawable.boss_1,
                R.drawable.boss_goright1,
                R.drawable.boss_1,
                R.drawable.boss_1
        });
    }

    public void setBossGoLeft() {
        setBossAnimation(new int[]{
                R.drawable.boss_go_left,
                R.drawable.boss_1,
                R.drawable.boss_1,
                R.drawable.boss_1,
                R.drawable.boss_go_left,
                R.drawable.boss_1,
                R.drawable.boss_1
        });
    }
    public static Bitmap getBitMap(Context context, int idPng) {
        return ImageUtils.getResizedBitmap(context, idPng, WIDTH_SCALE, HEIGHT_SCALE);
    }
    public void attackBoss(int damage) {
        takeDamage(damage);  // Gọi khi boss bị tấn công
    }
    public void takeDamage(int damage) {
        this.life -= damage;
        this.isHited = true;
        this.hitTime = System.currentTimeMillis(); // Lưu lại thời gian boss bị đánh
    }
    public void draw(Canvas canvas) {
        // Thanh máu của boss
        healthPaint.setColor(Color.RED);
        float healthPercentage = (float) this.life / 100;
        canvas.drawRect(400, 30, 400 + (this.screenWidth - 800) * healthPercentage, 50, healthPaint);
        // Lấy thời gian hiện tại
        long currentTime = System.currentTimeMillis();
        // Kiểm tra nếu boss bị đánh và thời gian còn trong khoảng hitDuration (0.5 giây)
        if (this.isHited) {
            if (currentTime - hitTime <= hitDuration) {
                canvas.drawBitmap(Bosshited, this.getBossX()+40, this.bossY+40, null);
                moveFarPlayer();
            } else {
                // Nếu đã quá 0.5 giây thì quay lại trạng thái bình thường
                this.isHited = false;
                moveTowardsPlayer();
            }
        }
        // Nếu boss không bị đánh, hiển thị hình ảnh bình thường
        if (!this.isHited) {
        //    Log.d("BOSS", "draw: dang ve boss");
            BossAni.updateBoss();
            Bitmap currentFrame = BossAni.getBossAnimation();
            canvas.drawBitmap(currentFrame, this.getBossX(), this.getBossY(), null);
        }
    }
    private void updateHitbox() {
        // bo nhỏ vòng va chạm lại do ảnh png của con boss to quá
        hitbox = new Rect((int)bossX+50, (int)bossY+100, (int)(bossX + wBoss-100), (int)(bossY + hBoss-200));
        //Log.d("hitbox", "updateHitbox: "+ hitbox.bottom +" "+ hitbox.left+" "+ hitbox.top +" "+ hitbox.right);
    }
    public Rect getHitbox() {
      //  Log.d("BOSS", "getHitbox: " + (int)bossX +" "+ (int)bossY+" "+ (int)(bossX + wBoss)+" "+ (int)(bossY + hBoss));
        //return new Rect((int)bossX, (int)bossY, (int)(bossX + wBoss), (int)(bossY + hBoss));
        return hitbox;
    }

    public void attackPlayer() {
        float distance = (float) Math.sqrt((playerX - bossX) * (playerX - bossX) + (playerY - bossY) * (playerY - bossY));
        if (distance <= 50) {
//            Log.d("Boss", "attackPlayer: TAN CONG NGUOI CHOI");
            setAttacked(true);
        }
        else{
            Log.d("Boss", "attackPlayer: Dung tan cong");
            setAttacked(false);
        }
    }
    public void setPlayerPosition(float playercenterX, float playercenterY) {
        this.playerX = playercenterX;
        this.playerY = playercenterY;
    }
    public void moveTowardsPlayer() {
        // Tính khoảng cách theo hai trục
        float dx = playerX - bossX;
        float dy = playerY - bossY;
        // Ghi log giá trị dx
        //Log.d("Movement", "dx: " + dx + ", dy: " + dy);
        if(dx>0)
        {this.setBossGoRight();}
        else if (dx<0) {this.setBossGoLeft();}
        else {
            //Log.d("TAG", "moveTowardsPlayer: no move");
        }
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        // Nếu khoảng cách lớn hơn ngưỡng, boss di chuyển
        if (distance > 10) { // Ngưỡng di chuyển
            // Tính toán hướng di chuyển
            this.setBossX(this.bossX += (dx / distance) * speed);
            this.setBossY(this.bossY += (dy / distance) * speed);
        }
        else if(distance<10){
            this.setBossNoMove();
        }
        // Cập nhật hitbox của boss sau khi di chuyển
        updateHitbox();
    }
    public void moveFarPlayer() {
        float dx = playerX - bossX;
        float dy = playerY - bossY;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        if (distance > 10) { // Ngưỡng di chuyển
            // Tính toán hướng di chuyển
            this.setBossX(this.bossX -= (dx / distance) * speed);
            this.setBossY(this.bossY -= (dy / distance) * speed);
        }
        updateHitbox();
    }
    public void update() {
        // move();
        moveTowardsPlayer();
        // Boss tấn công khi đến gần người chơi
        attackPlayer();
        // Cập nhật logic khác như tấn công, hồi máu...
    }
    public float getBossX() {
        return bossX;
    }
    public void setBossX(float bossX) {
        this.bossX = bossX;
    }
    public float getBossY() {
        return bossY;
    }
    public void setBossY(float bossY) {
        this.bossY = bossY;
    }
    public float getwBoss() {
        return wBoss;
    }
    public void setwBoss(float wBoss) {
        this.wBoss = wBoss;
    }
    public void sethBoss(float hBoss) {
        this.hBoss = hBoss;
    }
    public void setBoss(Bitmap boss) {
        Boss = boss;
    }
    public Context getContext() {
        return context;
    }
    public void setContext(Context context) {
        this.context = context;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }


    public void setHited(Boolean hited) {
        isHited = hited;
    }

    public boolean isAttacked() {
        return attacked;
    }

    public void setAttacked(boolean attacked) {
        this.attacked = attacked;
    }
}

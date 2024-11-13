package com.example.save_my_friend.view.level2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.core.content.res.ResourcesCompat;

import com.example.save_my_friend.MainActivity;
import com.example.save_my_friend.R;
import com.example.save_my_friend.model.Fish;
import com.example.save_my_friend.model.bossLv2.Boss;
import com.example.save_my_friend.model.weapons.Animation;
import com.example.save_my_friend.model.weapons.WeaponChoice;
import com.example.save_my_friend.utils.AnimationCallback;
import com.example.save_my_friend.utils.AnimationUtils;
import com.example.save_my_friend.utils.ImageCache;
import com.example.save_my_friend.utils.ImageUtils;
import com.example.save_my_friend.view.GameOver;
import com.example.save_my_friend.view.level2.buttom.ChangeWeaponButton;

public class GameView2 extends SurfaceView implements SurfaceHolder.Callback, Runnable, AnimationCallback {
    private static final String TAG = "Preview";
    private Handler handler = new Handler();
    private boolean isPaused = false; // Biến để lưu trạng thái tạm dừng

    private Runnable attackRunnable;
    private boolean isAttacking = false;
    private SurfaceHolder surfaceHolder;
    private boolean running;
    private Thread drawingThread;
    private Bitmap background, pausebtn;
    private Fish fish;
    private Boss boss;
    final long UPDATE_MILLIS = 50;

    private int dWidth, dHeight;
    private AnimationUtils weapon;
    private Rect rectBackground, rectFightButton, hitBoxBoss, weaponsRect,rectPause;
    private Bitmap fightButton;
    private int fb_l, fb_r, fb_t, fb_b;
    public Runnable runnable;

    public boolean isFighting = false;
    public float TEXT_SIZE = 90;
    public int points = 0;
    public int life = 3;
    public Paint textPaint = new Paint();
    public Paint healthPaint = new Paint();
    private ChangeWeaponButton changeWeaponButton;
    private boolean isHit = false;
    private int heart = 1000;
    public Boolean gun = false;
    public WeaponChoice newChoice;
    // Constructor chỉ với Context
    public GameView2(Context context, int points) {
        super(context);
        init(context);
        this.points = points;
    }

    // Constructor với Context và AttributeSet
    public GameView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    @Override
    public void onAnimationComplete() {
        // Xử lý khi animation hoàn tất
        setFighting(false);
        this.isHit = false;
        boss.setHited(false);
    }

    public Rect getWeaponsRect() {
        return weaponsRect;
    }

    public void setWeaponsRect(Rect weaponsRect) {
        this.weaponsRect = weaponsRect;
    }

    public int getHeart() {
        return heart;
    }

    public void setHeart(int heart) {
        this.heart = heart;
    }

    public int getFb_l() {
        return fb_l;
    }

    public void setFb_l(int fb_l) {
        this.fb_l = fb_l;
    }

    public int getFb_r() {
        return fb_r;
    }

    public void setFb_r(int fb_r) {
        this.fb_r = fb_r;
    }

    public int getFb_t() {
        return fb_t;
    }

    public void setFb_t(int fb_t) {
        this.fb_t = fb_t;
    }

    public int getFb_b() {
        return fb_b;
    }

    public void setFb_b(int fb_b) {
        this.fb_b = fb_b;
    }

    public boolean isFighting() {
        return isFighting;
    }

    public void setFighting(boolean fighting) {
        isFighting = fighting;
    }

    public Boolean getGun() {
        return gun;
    }

    public void setGun(Boolean gun) {
        this.gun = gun;
    }
    private Animation cachedRightGunAnimation;
    private Animation cachedLeftGunAnimation;
    private Animation cachedRightSwordAnimation;
    private Animation cachedLeftSwordAnimation;
    private void init(Context context) {
        Log.d(TAG, "Preview()");
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        newChoice = new WeaponChoice(context);
        pausebtn=ImageUtils.getResizedBitmap(context, R.drawable.pause, 0.125f, 0.125f);

        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        fightButton = ImageUtils.getResizedBitmapByWidth(context, R.drawable.fight_button_lv2, 200);
        if (background == null) {
            background = ImageUtils.getResizedBitmap(context, R.drawable.background_lv2, 1.0f, 1.0f);
        }
        if (background == null) {
            throw new RuntimeException("One or more bitmaps are not loaded correctly.");
        }
        if (fightButton == null) {
            throw new RuntimeException("Fight button bitmap not loaded correctly.");
        }
        weapon = new AnimationUtils(context, this);
        weapon.setWeaponAnimation(newChoice.getSwordright());
        // Khởi tạo ChangeWeaponButton
        changeWeaponButton = new ChangeWeaponButton(context);
        // Đặt kích thước và vị trí cho ChangeWeaponButton
        changeWeaponButton.setLayoutParams(new ViewGroup.LayoutParams(200, 200)); // Ví dụ: kích thước 200x200
        updateScreenSize();
        changeWeaponButton.setLocation(dWidth, dHeight);
        fish = new Fish(context, dWidth, dHeight, dHeight / 2, 10);
        boss = new Boss(context, dWidth, dHeight, 400, 400, 1, 1, 1, this);
        rectBackground = new Rect(0, 0, dWidth, dHeight);
        setFb_l(this.dWidth - this.fightButton.getWidth() - 100);
        setFb_t(this.dHeight - this.fightButton.getHeight() - 250);
        setFb_r(this.dWidth - 100);
        setFb_b(this.dHeight - 250);
        rectFightButton = new Rect(fb_l, fb_t, fb_r, fb_b);
        textPaint.setColor(Color.rgb(255, 165, 0));
        textPaint.setTextSize(TEXT_SIZE);
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTypeface(ResourcesCompat.getFont(context, R.font.matemasie_regular));
        healthPaint.setColor(Color.GREEN);
        rectPause = new Rect(200,20,pausebtn.getWidth()+200,pausebtn.getHeight()+20);
        runnable = new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };
    }
    public void ChangeWeapon(Context context) {
        if (this.getGun()) {
            ChangeTurnWeaponGun(context);
        } else {
            ChangeTurnWeaponSword(context);
        }
    }
    public void ChangeTurnWeaponGun(Context context) {
        if (fish.isTurnRight()) {
            if (cachedRightGunAnimation == null) {
                cachedRightGunAnimation = newChoice.getGunright();
            }
            weapon.setWeaponAnimation(cachedRightGunAnimation);
        } else {
            if (cachedLeftGunAnimation == null) {
                cachedLeftGunAnimation = newChoice.getGunleft();
            }
            weapon.setWeaponAnimation(cachedLeftGunAnimation);
        }
    }

    public void ChangeTurnWeaponSword(Context context) {
        if (fish.isTurnRight()) {
            if (cachedRightSwordAnimation == null) {
                cachedRightSwordAnimation = newChoice.getSwordright();
            }
            weapon.setWeaponAnimation(cachedRightSwordAnimation);
        } else {
            if (cachedLeftSwordAnimation == null) {
                cachedLeftSwordAnimation = newChoice.getSwordleft();
            }
            weapon.setWeaponAnimation(cachedLeftSwordAnimation);
        }
    }

    private void updateScreenSize() {
        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        this.dWidth = size.x;
        this.dHeight = size.y;
    }

    @Override
    public void run() {
        while (running) {
            if (!surfaceHolder.getSurface().isValid()) continue;
            Canvas canvas = surfaceHolder.lockCanvas();
            if (canvas != null) {
                try {
                    drawContent(canvas, getContext());
                } finally {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }

    private void drawContent(Canvas canvas, Context context) {
        // Draw background and UI elements
        canvas.drawBitmap(background, null, rectBackground, null);
        canvas.drawBitmap(fightButton, null, rectFightButton, null);
        canvas.drawBitmap(pausebtn, null, rectPause, null);
        Log.d(TAG, "drawContent: rectPause" + rectPause.bottom + " " +
                rectPause.top + " " + rectPause.right + " " + rectPause.left);
        // Draw boss and fish if they exist
        if (boss != null) boss.draw(canvas);
        if (fish != null) fish.draw(canvas, 0);
        if (this.isFighting) {
            // hiện animation vũ khí
            weapon.update();
            Bitmap currentFrame = weapon.getWeaponAnimation();
            if (currentFrame != null) {
                // Cache fish position and size to avoid multiple method calls
                float fishX = fish.getFishX();
                float fishY = fish.getFishY();
                float fishW = fish.getFishW();
                float fishH = fish.getFishH();

                // Calculate weapon position based on fish's direction
                float swordX = fish.isTurnRight() ? fishX + 50 : fishX - 150;
                float swordY = fishY - 3 * fishH / 4;
                float swordR = swordX + (fish.isTurnRight() ? 200 : 150);
                float swordB = swordY + 200;

                this.setWeaponsRect(new Rect((int) swordX, (int) swordY, (int) swordR, (int) swordB));
                canvas.drawBitmap(currentFrame, swordX, swordY, null);

                // Check va chạm màn 2 tấn công boss
                if (boss != null && Rect.intersects(this.getWeaponsRect(), boss.getHitbox())) {
                    if (!isHit) {
                        boss.takeDamage(1);
                        boss.setLife(boss.getLife() - 1);
                        points += 10;
                        boss.setHited(true);
                        this.isHit = true;
                    }
                } else {
                    this.isHit = false;
                    if (boss != null) boss.setHited(false);
                }
            }
        }
        // Health reduction logic
        if (boss.isAttacked()) {
            startHealthReduction(context);
        } else {
            stopHealthReduction();
        }
        // Draw health bar and points
        canvas.drawRect(dWidth - 200, 30, dWidth - 200 + 20 * fish.getHealth(), 80, healthPaint);
        canvas.drawText("" + points, 20, TEXT_SIZE, textPaint);

        // Draw weapon change button
        if (changeWeaponButton != null) {
            changeWeaponButton.draw(canvas, this.dWidth, this.dHeight);
        }

        // Update game state if not paused
        if (!isPaused) {
            if (boss != null) {
                boss.setPlayerPosition(fish.getFishX() - fish.getFishW() + 20, fish.getFishY() - fish.getFishH());
                boss.update();
            }
            handler.postDelayed(runnable, UPDATE_MILLIS);
        }
    }
    public void stopHealthReduction() {
        isAttacking = false; // Đặt lại cờ tấn công
        handler.removeCallbacks(attackRunnable); // Hủy Runnable
    }
    public void startHealthReduction(Context context) {
        if (isAttacking) return; // Nếu đang tấn công, không khởi động lại
        isAttacking = true;
        attackRunnable = new Runnable() {
            @Override
            public void run() {
                if (boss.isAttacked() && fish.getHealth() > 0) {
                    // Trừ máu của fish
                    fish.setHealth(fish.getHealth() - 1);
                    Log.d(TAG, "Fish health: " + fish.getHealth());
                    // Thay đổi màu thanh máu dựa trên số máu hiện tại
                    if (fish.getHealth() < 7) {
                        healthPaint.setColor(Color.YELLOW);
                    }
                    if (fish.getHealth() < 4) {
                        healthPaint.setColor(Color.RED);
                    }
                    // Nếu máu của fish về 0
                    if (fish.getHealth() == 0) {
                        boss.setAttacked(false);
                        isAttacking = false; // Dừng quá trình tấn công

                        // Chuyển đến màn hình Game Over
                        if (getContext() instanceof Activity) {
                            Intent intent = new Intent(context, GameOver.class);
                            intent.putExtra("points", points); // Truyền dữ liệu điểm số
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            Log.d("NGUOI CHOI OIIII", "BAN DA CHETTT");
                            getContext().startActivity(intent);
                            ((Activity) getContext()).finish();
                        } else {
                            Log.e(TAG, "Context không phải là Activity, không thể gọi startActivity.");
                        }
                    } else {
                        // Tiếp tục trừ máu sau 1 giây nếu fish chưa chết
                        handler.postDelayed(this, 1000);
                    }
                } else {
                    // Dừng tấn công nếu boss không còn tấn công
                    isAttacking = false;
                }
            }
        };
        // Bắt đầu chạy lệnh trừ máu sau 1 giây
        handler.postDelayed(attackRunnable, 1000);
    }




    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // Log.d(TAG, "surfaceChanged");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        // Xử lý nút tạm dừng
        if (isPauseButtonPressed(x, y)) {
            handlePauseEvent();
            return true;
        }

        // Xử lý nút đổi vũ khí
        if (changeWeaponButton != null && changeWeaponButton.onTouchEvent(event, x, y)) {
            handleChangeWeaponEvent();
            return true;
        }

        // Xử lý nút tấn công
        if (event.getAction() == MotionEvent.ACTION_DOWN && isFightButtonPressed(x, y)) {
            handleFightButtonPressed();
            return true;
        }

        // Xử lý di chuyển cá
        if (event.getAction() == MotionEvent.ACTION_MOVE && isInsideGameArea(x, y)) {
            handleFishMovement(x, y);
            return true;
        }

        return true; // Luôn trả về true để xử lý sự kiện touch
    }

    private boolean isPauseButtonPressed(float x, float y) {
        return x > rectPause.left && x < rectPause.right && y < rectPause.bottom && y > rectPause.top;
    }

    private void handlePauseEvent() {
        Log.d("TAG", "onTouchEvent: Pause/Resume " + isPaused);
        showPauseDialog();
    }

    private void handleChangeWeaponEvent() {
        this.setGun(!this.getGun());
        ChangeWeapon(this.getContext());
    }

    private boolean isFightButtonPressed(float x, float y) {
        return x >= this.fb_l && x <= this.fb_r && y >= this.fb_t && y <= this.fb_b;
    }

    private void handleFightButtonPressed() {
        weapon.setAnimating(true);
        setFighting(true);
        Log.d(TAG, "FIGHT button clicked");
    }

    private boolean isInsideGameArea(float x, float y) {
        return x < this.dWidth * 4 / 5 && y < this.dHeight * 4 / 5;
    }

    private void handleFishMovement(float x, float y) {
        Boolean rightBeforeMove = fish.isTurnRight();

        if (!isPaused) {
            fish.handleTouch(x, y, this.dWidth, this.dHeight, boss.getHitbox());
        }

        Boolean rightAfterMove = fish.isTurnRight();
        if (rightBeforeMove != rightAfterMove) {
            ChangeWeapon(getContext());
        }
    }

    private boolean isDialogShowing = false;

    private void showPauseDialog() {
        if (isDialogShowing) return; // Nếu dialog đang hiển thị, không tạo thêm nữa
        isDialogShowing = true;
        // Dừng game
        isPaused = true;
        pauseGame();  // Gọi phương thức tạm dừng game
        // Tạo Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder((Activity) getContext());
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.dialog_pause, null);
        // Thiết lập giao diện và sự kiện cho dialog
        builder.setView(dialogView);
        AlertDialog pauseDialog = builder.create();
        pauseDialog.setCancelable(false); // Không cho phép tắt dialog bằng cách nhấn ngoài
        // Nút Resume
        Button btnResume = dialogView.findViewById(R.id.btnResume);
        btnResume.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                isPaused = false; // Cập nhật trạng thái
                resumeGame();  // Gọi phương thức tiếp tục game
                pauseDialog.dismiss(); // Đóng dialog
                isDialogShowing = false; // Đặt lại cờ
            }
        });
        Button quitButton = dialogView.findViewById(R.id.btnQuit);
        quitButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo Intent để chuyển về MainActivity
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                // Đảm bảo không quay lại màn hình GameView
                getContext().startActivity(intent); // Bắt đầu MainActivity
                ((Activity) getContext()).finish(); // Đóng Activity hiện tại
                isDialogShowing = false; // Đặt lại cờ
            }
        });
        // Hiển thị Dialog
        pauseDialog.show();
    }

    //set lai toa do khi xoay man hinh
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        updateScreenSize();
        setFb_l(this.dWidth - this.fightButton.getWidth() - 100);
        setFb_t(this.dHeight - this.fightButton.getHeight() - 250);
        setFb_r(this.dWidth - 100);
        setFb_b(this.dHeight - 250);
        rectFightButton.set(fb_l, fb_t, fb_r, fb_b);
    }
    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (visibility == View.GONE) {
            pauseGame(); // Gọi hàm tạm dừng game khi Activity không còn hiển thị
        } else if (visibility == View.VISIBLE) {
            resumeGame(); // Tiếp tục game khi Activity quay trở lại
        }
    }
    public void pauseGame() {
        if (!isPaused) {
            isPaused = true; // Đặt trạng thái tạm dừng
            handler.removeCallbacks(runnable); // Ngừng các callback đang chạy
            stopHealthReduction(); // Ngừng các quá trình tấn công của boss
        }
    }

    public void resumeGame() {
        if (isPaused) {
            isPaused = false; // Cập nhật trạng thái game
            handler.postDelayed(runnable, UPDATE_MILLIS); // Tiếp tục cập nhật game
            startHealthReduction(getContext()); // Tiếp tục trừ máu nếu boss đang tấn công
        }
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!isPaused) {
            running = true;
            drawingThread = new Thread(this);
            drawingThread.start();
            weapon.startAnimation();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        running = false;
        boolean retry = true;
        while (retry) {
            try {
                drawingThread.join();
                retry = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
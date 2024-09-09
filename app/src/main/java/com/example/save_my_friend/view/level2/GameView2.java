package com.example.save_my_friend.view.level2;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.core.content.res.ResourcesCompat;

import com.example.save_my_friend.R;
import com.example.save_my_friend.model.Fish;
import com.example.save_my_friend.model.weapons.Animation;
import com.example.save_my_friend.utils.AnimationCallback;
import com.example.save_my_friend.utils.AnimationUtils;
import com.example.save_my_friend.utils.ImageUtils;

public class GameView2 extends SurfaceView implements SurfaceHolder.Callback, Runnable, AnimationCallback {

    private static final String TAG = "Preview";
    private SurfaceHolder surfaceHolder;
    private boolean running;
    private Thread drawingThread;
    private Bitmap background;
    private Fish fish;
    private int dWidth, dHeight;
    private AnimationUtils animationUtils;
    private Rect rectBackground, rectFightButton;
    private Bitmap fightButton;
    private int fb_l , fb_r , fb_t, fb_b;
    public boolean isFighting = false;
    public float TEXT_SIZE = 120;
    public int points = 0;
    public int life = 3;
    public Paint textPaint = new Paint();
    public Paint healthPaint = new Paint();

    // Constructor với Context
    public GameView2(Context context) {
        super(context);
        init(context);
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
        Log.d("TAG", "onAnimationComplete: endddddddddddddd");
        System.out.println("Animation complete!"); // Hoặc thực hiện hành động khác
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

    private void init(Context context) {
        Log.d(TAG, "Preview()");
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        fightButton = ImageUtils.getResizedBitmapByWidth(context, R.drawable.fight_button_lv2, 200);
        background = ImageUtils.getResizedBitmap(context, R.drawable.background_lv2, 1.0f, 1.0f);
        if (background == null) {
            throw new RuntimeException("One or more bitmaps are not loaded correctly.");
        }

        if (fightButton == null) {
            throw new RuntimeException("Fight button bitmap not loaded correctly.");
        }
        animationUtils = new AnimationUtils(context, this);
        animationUtils.setCurrentAni(new Animation(new Bitmap[]{
                getBitMap(context, R.drawable.gun_lv2_1),
                getBitMap(context, R.drawable.gun_lv2_2),
                getBitMap(context, R.drawable.gun_lv2_3),
                getBitMap(context, R.drawable.gun_lv2_4),
                getBitMap(context, R.drawable.gun_lv2_5),
                getBitMap(context, R.drawable.gun_lv2_6),
                getBitMap(context, R.drawable.gun_lv2_7),
                getBitMap(context, R.drawable.gun_lv2_8),
                // Add other frames
        }));
        updateScreenSize();
        fish = new Fish(context, dWidth, dHeight, dHeight / 2);
        rectBackground = new Rect(0, 0, dWidth, dHeight);

        Log.d(TAG, "Fight width: " + fightButton.getWidth() + ", height nut: " + fightButton.getHeight() + " rong man hinh " + this.dWidth + " cao man hinh " + dHeight);
        Log.d(TAG, "Fight button left: " + (this.dWidth - this.fightButton.getWidth()) + "top nut" + (this.dHeight - this.fightButton.getHeight()) + ", right nut: " + this.dWidth + "bot nut " + this.dHeight);

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
    }
    public static Bitmap getBitMap(Context context, int idPng) {
        return ImageUtils.getResizedBitmap(context, idPng, 0.5f, 0.5f);
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
        // Vẽ hình nền
        canvas.drawBitmap(background, null, rectBackground, null);
        canvas.drawBitmap(fightButton, null, rectFightButton, null);

        // Vẽ con cá
        fish.draw(canvas, 0);

        if(this.isFighting){
            // Vẽ animation thanh kiếm bên phải con cá
            animationUtils.update();
            Bitmap currentFrame = animationUtils.getCurrentAni();
            float swordX = fish.getFishX()+50;
            float swordY = fish.getFishY() - 3*fish.getFishH()/4 ;
            canvas.drawBitmap(currentFrame, swordX, swordY, null);
        }
        canvas.drawRect(dWidth - 200, 30, dWidth - 200 + 60 * life, 80, healthPaint);
        canvas.drawText("" + points, 20, TEXT_SIZE, textPaint);
        // Vẽ nút (thay thế bằng vẽ bitmap cho nút hoặc vẽ hình chữ nhật)
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        animationUtils.startAnimation();
        Log.d(TAG, "surfaceCreated");
        running = true;
        drawingThread = new Thread(this);
        drawingThread.start();
    }


    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "surfaceDestroyed");
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

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(TAG, "surfaceChanged");
    }

    @Override

    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (x >= this.fb_l && x <= this.fb_r && y >= this.fb_t && y <= this.fb_b) {
                // Xử lý sự kiện khi nút được nhấn
                animationUtils.setAnimating(true);
                setFighting(true);
                Log.d(TAG, "FIGHT button clicked");
                return true; // Trả về true để tiếp tục nhận các sự kiện touch khác
            }
        }
        if(x<this.dWidth*3/4&&y<this.dHeight*3/4){
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                // Di chuyển con cá khi người dùng kéo ngón tay
                fish.handleTouch(x, y, this.dWidth, this.dHeight);
                //Log.d("action move", "onTouchEvent: Moving");
                return true; // Trả về true để thông báo rằng bạn đang xử lý sự kiện này
            }
        }

        return true; // Trả về true để xử lý tất cả các sự kiện touch
    }

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

}
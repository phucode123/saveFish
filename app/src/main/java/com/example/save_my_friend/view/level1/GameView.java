package com.example.save_my_friend.view.level1;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import com.example.save_my_friend.MainActivity;
import com.example.save_my_friend.utils.ImageUtils;
import com.example.save_my_friend.R;
import com.example.save_my_friend.model.Dizzy;
import com.example.save_my_friend.model.Explosion;
import com.example.save_my_friend.model.Fish;
import com.example.save_my_friend.model.Spike;
import com.example.save_my_friend.view.ChangeLevel;
import com.example.save_my_friend.view.GameOver;

import java.util.ArrayList;
import java.util.Random;
import android.app.AlertDialog;
public class GameView extends View {

    public Bitmap background, ground, pausebtn;
    public Fish fish;
    public Rect rectBackground, reactGround, rectPause;
    public Context context;
    public Handler handler;
    final long UPDATE_MILLIS = 30;
    public Runnable runnable;
    public Paint textPaint = new Paint();
    public Paint healthPaint = new Paint();
    public float TEXT_SIZE = 120;
    public int points = 0;
    public int life = 3;
    public static int dWidth, dHeight;
    public Random random;
    ArrayList<Spike> spikes;
    ArrayList<Explosion> explosions;
    public Dizzy dizzy;
    private boolean isPaused = false; // Biến để lưu trạng thái tạm dừng
    public GameView(Context context) {
        super(context);
        this.context = context;

        background = ImageUtils.getResizedBitmap(context, R.drawable.in_game_background, 1.0f, 1.0f); // Toàn màn hình
        ground = ImageUtils.getResizedBitmap(context, R.drawable.ground_1, 1.0f, 0.25f); // Rộng màn hình, cao 1/8 màn hình
        pausebtn=ImageUtils.getResizedBitmap(context, R.drawable.pause, 0.125f, 0.125f);

        if (background == null || ground == null ) {
            throw new RuntimeException("One or more bitmaps are not loaded correctly.");
        }
        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        dWidth = size.x;
        dHeight = size.y;
        fish = new Fish(context, dWidth, dHeight, ground.getHeight()+110, 60);
        rectBackground = new Rect(0, 0, dWidth, dHeight);
        reactGround = new Rect(0, dHeight - ground.getHeight()-100, dWidth, dHeight);
        rectPause = new Rect(0,0,pausebtn.getWidth(),pausebtn.getHeight());
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };

        textPaint.setColor(Color.rgb(255, 165, 0));
        textPaint.setTextSize(TEXT_SIZE);
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTypeface(ResourcesCompat.getFont(context, R.font.matemasie_regular));
        healthPaint.setColor(Color.GREEN);
        random = new Random();
        dizzy = new Dizzy(context);
        spikes = new ArrayList<>();
        explosions = new ArrayList<>();
        //số lượng đá rơi xuống 1 chu kì
        for (int i = 0; i < 3; i++) {
            Spike spike = new Spike(context);
            spikes.add(spike);
        }
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(background, null, rectBackground, null);
        canvas.drawBitmap(ground, null, reactGround, null);
        canvas.drawBitmap(pausebtn, null, rectPause,null);
        dizzy.draw(canvas);
        fish.draw(canvas, 0);
        for (int i = 0; i < spikes.size(); i++) {
            canvas.drawBitmap(
                    spikes.get(i).getSpike(
                                    spikes.get(i).spikeFrame),
                                    spikes.get(i).spikeX,
                                    spikes.get(i).spikeY,
                                    null);
            spikes.get(i).spikeFrame++;
            if (spikes.get(i).spikeFrame > 2) {
                spikes.get(i).spikeFrame = 0;
            }
            spikes.get(i).spikeY += spikes.get(i).spikeVelocity;
            if (spikes.get(i).spikeY + spikes.get(i).getSpikeHeight() >= dHeight - ground.getHeight()-100) {
                points += 10;
                Explosion explosion = new Explosion(context);
                explosion.explosionX = spikes.get(i).spikeX;
                explosion.explosionY = spikes.get(i).spikeY;
                explosions.add(explosion);
                spikes.get(i).resetPosition();

                if(points==110){
                    Intent intent = new Intent(context, ChangeLevel.class);
                    intent.putExtra("points", points);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                    // Ép kiểu context thành Activity và gọi finish()
                    if (context instanceof Activity) {
                        ((Activity) context).finish();
                    }
                }
            }
        }
        //check va chạm
        for (int i = 0; i < spikes.size(); i++) {
            // Check va chạm với cá
            if (spikes.get(i).spikeX + spikes.get(i).getSpikeWidth() >= fish.getFishX()
                    && spikes.get(i).spikeX <= fish.getFishX() + fish.getFishW()
                    && spikes.get(i).spikeY + spikes.get(i).getSpikeHeight() >= fish.getFishY()
                    && spikes.get(i).spikeY <= fish.getFishY() + fish.getFishH()) {
                fish.setHealth(fish.getHealth() - 1);
                spikes.get(i).resetPosition();
                if (fish.getHealth() == 0) {
                    Intent intent = new Intent(context, GameOver.class);
                    intent.putExtra("points", points);
                    Log.d("NGUOI CHOI OIIII", "BAN DA CHETTT");
                    context.startActivity(intent);
                    if (context instanceof Activity) {
                        ((Activity) context).finish();
                    }
                } else {
                    // Hiển thị Dizzy trên đầu cá
                    dizzy.show(fish.getFishX(), fish.getFishY());
                    // Ẩn Dizzy sau 1 giây
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dizzy.hide();
                        }
                    }, 1000); // Delay 1000ms = 1 giây
                }
            }

        }
        for (int i = 0; i < explosions.size(); i++) {
            canvas.drawBitmap(explosions.get(i).getExplosion(explosions.get(i).explosionFrame), explosions.get(i).explosionX,
                    explosions.get(i).explosionY, null);
            explosions.get(i).explosionFrame++;
            if (explosions.get(i).explosionFrame > 3) {
                explosions.remove(i);
            }
        }
        if (fish.getHealth() < 7) {
            healthPaint.setColor(Color.YELLOW);
        } else if (fish.getHealth() < 4) {
            healthPaint.setColor(Color.RED);
        }
        canvas.drawRect(dWidth - 300, 30, dWidth -250+ 20* fish.getHealth()/6, 80, healthPaint);
        canvas.drawText("" + points, pausebtn.getWidth()+20, TEXT_SIZE, textPaint);
        // Nếu game không bị tạm dừng, tiếp tục gọi lại onDraw để vẽ frame tiếp theo
        if (!isPaused) {
            handler.postDelayed(runnable, UPDATE_MILLIS);
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();
        fish.handleTouch(touchX, touchY, dWidth);
        if (touchX > rectPause.left && touchX < rectPause.right && touchY < rectPause.bottom && touchY > rectPause.top) {
            showPauseDialog();
            Log.d("TAG", "onTouchEvent: Pause/Resume"+ isPaused);
        }
        // Nếu game không bị tạm dừng, cho phép di chuyển cá
        if (!isPaused) {
            fish.handleTouch(touchX, touchY, dWidth);
        }
        return true;
    }
    // Phương thức để hiển thị Dialog Pause
    private boolean isDialogShowing = false;
    private void showPauseDialog() {
        if (isDialogShowing) return; // Nếu dialog đang hiển thị, không tạo thêm nữa
        isDialogShowing = true;

        // Dừng game
        isPaused = true;
        handler.removeCallbacks(runnable); // Ngừng cập nhật game

        // Tạo Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder((Activity) context);
        LayoutInflater inflater = LayoutInflater.from(context);
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
                handler.postDelayed(runnable, UPDATE_MILLIS); // Tiếp tục cập nhật game
                pauseDialog.dismiss(); // Đóng dialog
                isDialogShowing = false; // Đặt lại cờ
            }
        });

        // Nút Quit
        Button btnQuit = dialogView.findViewById(R.id.btnQuit);
        btnQuit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo Intent để chuyển về MainActivity
                Intent intent = new Intent(context, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Đảm bảo không quay lại màn hình GameView
                context.startActivity(intent); // Bắt đầu MainActivity
                ((Activity) context).finish(); // Đóng Activity hiện tại
              //  ((Activity) context).finish(); // Thoát game
            }
        });

        // Hiển thị dialog
        pauseDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                isDialogShowing = false; // Đặt lại cờ khi dialog bị đóng
            }
        });

        pauseDialog.show();
    }
}

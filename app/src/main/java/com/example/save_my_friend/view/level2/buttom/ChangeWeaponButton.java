package com.example.save_my_friend.view.level2.buttom;

import static com.example.save_my_friend.view.level1.GameView.dHeight;
import static com.example.save_my_friend.view.level1.GameView.dWidth;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.save_my_friend.R;
import com.example.save_my_friend.utils.ImageUtils;

public class ChangeWeaponButton extends View {
    private Bitmap currentWeaponImage;
    private Bitmap swordImage;
    private Bitmap gunImage;
    private int cb_l, cb_r, cb_t, cb_b;
    private boolean isSword = true; // Trạng thái hiện tại của vũ khí
    public ChangeWeaponButton(Context context) {
        super(context);
        init(context);
    }
    public ChangeWeaponButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    private void init(Context context) {
        // Load hình ảnh vũ khí
        swordImage = ImageUtils.getResizedBitmap(context, R.drawable.sword, 0.2f, 0.2f);
        gunImage = ImageUtils.getResizedBitmap(context, R.drawable.gun, 0.2f, 0.2f);
        currentWeaponImage = swordImage; // Ban đầu là kiếm
    }

    public void setLocation(int dWidth, int dHeight){
        Log.d("log kich thuoc", "setLocation: width"+ dWidth+ "height: "+ dHeight);
        setCb_b(dHeight/2 - currentWeaponImage.getHeight());
        setCb_t(dHeight/2 - currentWeaponImage.getHeight()-200);
        setCb_l(dWidth - currentWeaponImage.getWidth());
        setCb_r(dWidth );
    }
    //  @Override
    public void draw(Canvas canvas, int dWidth, int dHeight) {
        super.draw(canvas);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.GRAY); // Chọn màu tùy ý
        canvas.drawBitmap(currentWeaponImage, this.getCb_l(), this.getCb_t(), null);
    }
    public boolean onTouchEvent(MotionEvent event,float x,float y) {
           // Log.d("An change", "onTouchEvent: Vao den touchEvent roi x va y = "+ x+" "+ y+ " toa do: l r t b la: "+ this.getCb_l()+" "+this.getCb_r());
            if(event.getAction() == MotionEvent.ACTION_DOWN)
            {
                if (x >= this.getCb_l() && x <= this.getCb_r() && y >= this.getCb_t() && y <= this.getCb_b()) {
                   // Log.d("An change", "onTouchEvent: Vao den nut nay rui");
                    // Đổi vũ khí khi ấn
                    isSword = !isSword;
                    currentWeaponImage = isSword ? swordImage : gunImage;
                    return true; // Trả về true để tiếp tục nhận các sự kiện touch khác
                }
            }
//            return true;
        return false;
    }

    public int getCb_l() {
        return cb_l;
    }

    public void setCb_l(int cb_l) {
        this.cb_l = cb_l;
    }

    public int getCb_r() {
        return cb_r;
    }

    public void setCb_r(int cb_r) {
        this.cb_r = cb_r;
    }

    public int getCb_t() {
        return cb_t;
    }

    public void setCb_t(int cb_t) {
        this.cb_t = cb_t;
    }

    public int getCb_b() {
        return cb_b;
    }

    public void setCb_b(int cb_b) {
        this.cb_b = cb_b;
    }
}


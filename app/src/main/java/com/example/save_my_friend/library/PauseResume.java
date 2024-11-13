package com.example.save_my_friend.library;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.example.save_my_friend.MainActivity;
import com.example.save_my_friend.R;

public class PauseResume {
    public Handler handler;
    private Context context;
    public boolean isPaused;
  //  private boolean isDialogShowing;
    private Runnable updateRunnable; // Runnable để tiếp tục cập nhật game
    private static final long UPDATE_MILLIS = 100; // Thời gian cập nhật game
    public Runnable runnable;
    private boolean isDialogShowing = false;
    public PauseResume(Context context, Runnable runnable) {
        this.context = context;
        this.updateRunnable = runnable;
        this.isPaused = false;
        this.isDialogShowing = false;
    }

    public void showPauseDialog(Runnable runnable) {
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
        btnResume.setOnClickListener(new View.OnClickListener() {
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
        btnQuit.setOnClickListener(new View.OnClickListener() {
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
//    private void resumeGame(AlertDialog pauseDialog) {
//        isPaused = false; // Cập nhật trạng thái
//        ((Activity) context).runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                ((Activity) context).runOnUiThread(updateRunnable); // Tiếp tục cập nhật game
//                pauseDialog.dismiss(); // Đóng dialog
//                isDialogShowing = false; // Đặt lại cờ
//            }
//        });
//    }
//
//    private void quitGame() {
//        Intent intent = new Intent(context, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Đảm bảo không quay lại màn hình GameView
//        context.startActivity(intent); // Bắt đầu MainActivity
//        ((Activity) context).finish(); // Đóng Activity hiện tại
//    }
//
//    public boolean isPaused() {
//        return isPaused; // Trả về trạng thái pause
//    }
//
//    public void setPaused(boolean paused) {
//        this.isPaused = paused; // Thiết lập trạng thái pause
//    }
}

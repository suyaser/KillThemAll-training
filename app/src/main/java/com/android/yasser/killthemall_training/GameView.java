package com.android.yasser.killthemall_training;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by yasser on 1/13/2015.
 */
public class GameView extends SurfaceView {
    private Sprite sprite;
    private Bitmap bmp;
    private SurfaceHolder holder;
    private GameLoopThread gameLoopThread;

    public GameView(Context context)
    {
        super(context);
        gameLoopThread = new GameLoopThread(this);
        holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback(){
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                gameLoopThread.setRunning(true);
                gameLoopThread.start();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                gameLoopThread.setRunning(false);
                while (true){
                    try {
                        gameLoopThread.join();
                        break;
                    } catch(InterruptedException e) {
                    }
                }
            }
        });
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.good1);
        sprite = new Sprite(this, bmp);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.RED);
        sprite.onDraw(canvas);
    }
}

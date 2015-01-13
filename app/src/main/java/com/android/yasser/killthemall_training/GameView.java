package com.android.yasser.killthemall_training;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yasser on 1/13/2015.
 */
public class GameView extends SurfaceView {
    private final int sndMale;
    private final int sndFemale;
    private Bitmap bmpBlood;
    private SoundPool sounds;
    private Sprite sprite;
    private Bitmap bmp;
    private SurfaceHolder holder;
    private GameLoopThread gameLoopThread;
    private List<Sprite> sprites = new ArrayList<Sprite>();
    private long lastClick;
    private List<TempSprite> temps = new ArrayList<TempSprite>();

    public GameView(Context context) {
        super(context);
        gameLoopThread = new GameLoopThread(this);
        holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                createSprites();
                gameLoopThread.setRunning(true);
                gameLoopThread.start();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                gameLoopThread.setRunning(false);
                while (true) {
                    try {
                        gameLoopThread.join();
                        break;
                    } catch (InterruptedException e) {
                    }
                }
            }
        });
        bmpBlood = BitmapFactory.decodeResource(getResources(), R.drawable.blood1);
        sounds = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        sndMale = sounds.load(context, R.raw.man, 1);
        sndFemale = sounds.load(context, R.raw.woman, 1);
    }


    private void createSprites() {
        sprites.add(createSprite(R.drawable.bad1, true));
        sprites.add(createSprite(R.drawable.bad2, true));
        sprites.add(createSprite(R.drawable.bad3, true));
        sprites.add(createSprite(R.drawable.bad4, true));
        sprites.add(createSprite(R.drawable.bad5, true));
        sprites.add(createSprite(R.drawable.bad6, true));
        sprites.add(createSprite(R.drawable.good1, false));
        sprites.add(createSprite(R.drawable.good2, false));
        sprites.add(createSprite(R.drawable.good3, false));
        sprites.add(createSprite(R.drawable.good4, false));
        sprites.add(createSprite(R.drawable.good5, false));
        sprites.add(createSprite(R.drawable.good6, false));
    }

    private Sprite createSprite(int resource, boolean bool) {
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), resource);
        return new Sprite(this,bmp,bool);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.LTGRAY);
        for (int i = temps.size() - 1; i >= 0; i--) {
            temps.get(i).onDraw(canvas);
        }
        for (Sprite sprite : sprites) {
            sprite.onDraw(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (System.currentTimeMillis() - lastClick > 500) {
            lastClick = System.currentTimeMillis();
            float x = event.getX();
            float y = event.getY();
            synchronized (getHolder()) {
                for (int i = sprites.size() - 1; i >= 0; i--) {
                    Sprite sprite = sprites.get(i);
                    if (sprite.isCollision(x, y)) {
                        if(sprite.isBad())
                            sounds.play(sndMale, 1.0f, 1.0f, 0, 0, 1.5f);
                        else
                            sounds.play(sndFemale, 1.0f, 1.0f, 0, 0, 1.5f);
                        sprites.remove(sprite);
                        temps.add(new TempSprite(temps, this, x, y, bmpBlood));
                        break;
                    }
                }
            }
        }
        return true;
    }

}

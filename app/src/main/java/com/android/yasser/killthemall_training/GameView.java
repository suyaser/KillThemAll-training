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
import java.util.Random;

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
    private int[] zprites = {R.drawable.bad1,
            R.drawable.bad2,R.drawable.bad3,
            R.drawable.bad4,R.drawable.bad5,
            R.drawable.bad6,R.drawable.good1,
            R.drawable.good2,R.drawable.good3,
            R.drawable.good4,R.drawable.good5,
            R.drawable.good6};

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
        for(int i = 0; i<50; i++){
            Random r = new Random();
            int x = r.nextInt(11);
            sprites.add(createSprite(zprites[x], x<6));
        }
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

package com.android.yasser.killthemall_training;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;

/**
 * Created by yasser on 1/13/2015.
 */
public class GameView extends View {
    private final Bitmap bmp;

    public GameView(Context context)
    {
        super(context);
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.GREEN);
        canvas.drawBitmap(bmp, 10 ,10 , null);
    }
}

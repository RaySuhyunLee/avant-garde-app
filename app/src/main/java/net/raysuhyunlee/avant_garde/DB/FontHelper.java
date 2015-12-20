package net.raysuhyunlee.avant_garde.DB;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;

/**
 * Created by SuhyunLee on 2015. 12. 20..
 */
public class FontHelper {
    public static Bitmap textAsBitmap(Context context, String text, float textSize, int textColor) {
        Paint paint = new Paint();
        Typeface plain = Typeface.createFromAsset(context.getAssets(), "fonts/bitmap.ttf");
        paint.setTypeface(plain);
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.LEFT);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 0.5f); // round
        int height = (int) (baseline + paint.descent() + 0.5f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
        return image;
    }
}

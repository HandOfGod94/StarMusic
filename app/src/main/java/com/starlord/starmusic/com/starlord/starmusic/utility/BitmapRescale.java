package com.starlord.starmusic.com.starlord.starmusic.utility;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapRescale
{

    private static int calculateInSampleSize(BitmapFactory.Options options, int destWidth,int destHeight)
    {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;

        if(height > destHeight || width > destWidth)
        {
            int halfHeight = height / 2;
            int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > destHeight && (halfWidth / inSampleSize) > destWidth)
                inSampleSize *= 2;
        }
        return inSampleSize;
    }

    public static Bitmap rescale(Resources res, int resId, int destWidth, int destHeight)
    {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        options.inSampleSize = calculateInSampleSize(options,destWidth,destHeight);

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static Bitmap rescale(String pathName,int destWidth,int destHeight)
    {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName,options);

        options.inSampleSize = calculateInSampleSize(options,destWidth,destHeight);

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(pathName,options);
    }

    public static Bitmap rescale(byte[] data, int destWidth, int destHeight)
    {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data ,0, data.length, options);

        options.inSampleSize = calculateInSampleSize(options,destWidth,destHeight);

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(data, 0 ,data.length, options);
    }

}

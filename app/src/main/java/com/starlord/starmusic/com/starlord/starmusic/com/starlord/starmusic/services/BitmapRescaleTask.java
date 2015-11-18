package com.starlord.starmusic.com.starlord.starmusic.com.starlord.starmusic.services;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.starlord.starmusic.com.starlord.starmusic.utility.BitmapRescale;

import java.lang.ref.WeakReference;

public class BitmapRescaleTask extends AsyncTask<Integer,Void,Bitmap>
{
    private final WeakReference<ImageView> imageViewWeakReference;
    private Context context;
    private String pathName;
    private byte[] data;

    public BitmapRescaleTask(ImageView imageViewWeakReference, Context context, String pathName)
    {
        this.imageViewWeakReference = new WeakReference<ImageView>(imageViewWeakReference);
        this.context=context;
        this.pathName = pathName;
    }

    public BitmapRescaleTask(ImageView imageViewWeakReference, Context context,byte[] data)
    {
        this.imageViewWeakReference = new WeakReference<ImageView>(imageViewWeakReference);
        this.context=context;
        this.data = data;
    }

    @Override
    protected Bitmap doInBackground(Integer... params)
    {
        Bitmap src;
        int destWidth = params[0];
        int destHeight = params[1];
        if(pathName==null)
        {
            src = BitmapRescale.rescale(data, destWidth, destHeight);
            return Bitmap.createScaledBitmap(src, destWidth, destHeight, true);
        }
        else
        {
            src = BitmapRescale.rescale(pathName,destWidth,destHeight);
            return Bitmap.createScaledBitmap(src,destWidth,destHeight,true);
        }
    }

    @Override
    protected void onPostExecute(Bitmap bitmap)
    {
        if(imageViewWeakReference !=null && bitmap!=null)
        {
            ImageView imageView = imageViewWeakReference.get();
            if(imageView!=null)
                imageView.setImageBitmap(bitmap);
        }
    }
}

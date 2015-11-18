package com.starlord.starmusic.com.starlord.starmusic.com.starlord.starmusic.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;

import java.io.IOException;

public class MediaPlaybackService extends Service implements MediaPlayer.OnCompletionListener
{
    private MediaPlayer mediaPlayer;

    private final IBinder binder = new MediaPlaybackServiceBinder();

    //create notification manager

    public MediaPlaybackService()
    {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        mediaPlayer = new MediaPlayer();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return binder;
    }

    public class MediaPlaybackServiceBinder extends Binder
    {
        MediaPlaybackService getService()
        {
            return MediaPlaybackService.this;
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp)
    {
        if(mp!=null)
            mp.stop();
    }

    @Override
    public void onDestroy()
    {
        if(mediaPlayer!=null)
            mediaPlayer.release();
        super.onDestroy();
    }


    //Utility methods used for controlling media playback from Activity

    /**
     * Starts or resumes playback. If playback had previously been paused, playback
     * will continue from where it was paused. If playback had been stopped, or never
     * started before, playback will start at the beginning.
     */
    public void start()
    {
        mediaPlayer.start();
    }

    /**
     * Pause the playback
     */
    public void pause()
    {
        if(mediaPlayer!=null && mediaPlayer.isPlaying())
            mediaPlayer.pause();
    }

    /**
     * Stop the playback
     */
    public void stop()
    {
        if(mediaPlayer!=null)
            mediaPlayer.stop();
    }

    /**
     *  Checks whether media player is playing or not.
     * @return true - if MediaPlayer is currently playing
     * <br/>   false - if MediaPlayer is not playing
     */
    public boolean isPlaying()
    {
        return mediaPlayer.isPlaying();
    }

    /**
     * Gets the duration of the file
     * @return int - total duration of media file in milliseconds
     */
    public int getDuration()
    {
        return mediaPlayer.getDuration();
    }

    /**
     * Gets the current playback position
     * @return int - current position of media file in millisecond
     */
    public int getCurrentPosition()
    {
        return mediaPlayer.getCurrentPosition();
    }

    /**
     * Seeks to specified time position.
     * @param msec the offset in milliseconds from the start to seek to
     */
    public void seekTo(int msec)
    {
        mediaPlayer.seekTo(msec);
    }


    /**
     * Set Data source as content uri
     * @param context context to use when resolving Uri
     * @param uri content of uri data you want to play
     */
    public void setDataSource(Context context,Uri uri)
    {
        try
        {
            mediaPlayer.setDataSource(context,uri);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}

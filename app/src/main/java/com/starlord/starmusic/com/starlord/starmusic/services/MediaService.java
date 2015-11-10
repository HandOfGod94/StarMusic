package com.starlord.starmusic.com.starlord.starmusic.services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;

import com.starlord.starmusic.NowPlayingActivity;

public class MediaService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener
{
    private MediaPlayer mediaPlayer;
    private Uri songUri;

    public MediaService()
    {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        songUri = Uri.parse(intent.getStringExtra(NowPlayingActivity.SONG_PATH));
        initMediaPlayer();
        return super.onStartCommand(intent, flags, startId);
    }

    private void initMediaPlayer()
    {
        mediaPlayer = MediaPlayer.create(getApplicationContext(),songUri);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.prepareAsync();
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onPrepared(MediaPlayer mp)
    {
        mp.start();
    }

    @Override
    public void onCompletion(MediaPlayer mp)
    {
        mp.stop();
    }
}

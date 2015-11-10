package com.starlord.starmusic.com.starlord.starmusic.utility;

import android.media.MediaPlayer;
import android.widget.SeekBar;
import android.widget.TextView;

import com.starlord.starmusic.NowPlayingActivity;

import java.util.concurrent.TimeUnit;

//TODO Improve implementation

/**
 * This class is responsible for updating seek bar
 * and elapsed time on separate thread other than main UI
 */
public class UpdateProgress implements Runnable
{
    private MediaPlayer mediaPlayer = null;

    private TextView textViewCurrentPosition;
    private SeekBar seekBar;
    private long totalDuration;

    /**
     * Creates a new UpdateProgress object and uses NowPlayingActivity.handler.
     * This handler is static in nature and is required for continuous update at specified interval.
     * It'll initializes object with passed arguments as it's refrences.
     * @param mediaPlayer Currently playing media player. Note: It must be a created MediaPlayer.
     * @param textViewCurrentPosition TextView which contains text for elapsed time
     * @param seekBar SeekBar android widget which is responsible for showing progress.
     */
    public UpdateProgress(MediaPlayer mediaPlayer, TextView textViewCurrentPosition, SeekBar seekBar)
    {
        this.mediaPlayer = mediaPlayer;
        this.textViewCurrentPosition = textViewCurrentPosition;
        this.seekBar = seekBar;
        totalDuration = mediaPlayer.getDuration();
    }

    @Override
    public void run()
    {
        long currentPosition = mediaPlayer.getCurrentPosition();
        long progress = (long) (Math.floor(currentPosition*100F / totalDuration));

        //Update Seek bar
        seekBar.setProgress((int) progress);

        //Update elapsed time
        long currentMinute = TimeUnit.MILLISECONDS.toMinutes(currentPosition);
        long currentSecond = TimeUnit.MILLISECONDS.toSeconds(currentPosition) % 60L;
        textViewCurrentPosition.setText(String.format("%02d:%02d",currentMinute,currentSecond));

        //Repeat this every 100ms
        NowPlayingActivity.handler.postDelayed(this, 100);

    }
}

package com.starlord.starmusic;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class NowPlayingActivity extends Activity
{
    public static final String KEY_CURRENT_POSITION = "CURRENT_POSITION";
    public static final String KEY_IS_PLAYING = "IS_PLAYING";

    public int currentDuration = -1;
    public boolean isPlaying  = false;

    private MediaPlayer mediaPlayer;

    private ImageView imageViewPlay,imageViewPause,imageViewPrevious,imageViewNext;
    private TextView textViewCurrentPosition,textViewTotalDuration;
    private SeekBar seekBar;

    private Handler handler;
    public long totalDuration, totalMinute,totalSeconds;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_playing);

        //Check if previous instance is running. Helpful in case of orientation change.
        if(savedInstanceState!=null)
        {
            currentDuration = savedInstanceState.getInt(KEY_CURRENT_POSITION);
            isPlaying = savedInstanceState.getBoolean(KEY_IS_PLAYING);
        }

        //Initialize UI Components
        imageViewPlay = (ImageView) findViewById(R.id.imageViewPlay);
        imageViewPause = (ImageView) findViewById(R.id.imageViewPause);
        imageViewNext = (ImageView) findViewById(R.id.imageViewNext);
        imageViewPrevious = (ImageView) findViewById(R.id.imageViewPrevious);
        textViewTotalDuration = (TextView) findViewById(R.id.textViewTotalTime);
        textViewCurrentPosition = (TextView) findViewById(R.id.textViewCurrentPostion);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        mediaPlayer = MediaPlayer.create(this,R.raw.song);
        handler = new Handler();


        //Actions to be performed and view restoring if previous instance was running.
        //Helpful in orientation change
        if(isPlaying)
        {
            mediaPlayer.seekTo(currentDuration);
            mediaPlayer.start();
            imageViewPlay.setVisibility(View.GONE);
            imageViewPause.setVisibility(View.VISIBLE);
            handler.postDelayed(UpdateProgress,100);
        }
        else
        {
            if(savedInstanceState!=null && currentDuration!=-1)
                mediaPlayer.seekTo(currentDuration);
            imageViewPause.setVisibility(View.GONE);
            imageViewPlay.setVisibility(View.VISIBLE);
        }

        //Get total duration of currently playing song in milliseconds and
        // calculate total minutes ans total seconds and update it's TextView
        totalDuration = mediaPlayer.getDuration();
        totalMinute = TimeUnit.MILLISECONDS.toMinutes(totalDuration);
        totalSeconds = TimeUnit.MILLISECONDS.toSeconds(totalDuration) % 60L;
        textViewTotalDuration.setText(String.format("%02d:%02d",totalMinute,totalSeconds));

        seekBarProgress();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);

        //store isPlaying() and currentPosition() in order to save state
        // on orientatoin change.
        outState.putBoolean(KEY_IS_PLAYING, mediaPlayer.isPlaying());
        outState.putInt(KEY_CURRENT_POSITION, mediaPlayer.getCurrentPosition());
    }

    @Override
    protected void onDestroy()
    {
        //Release media player to free resource
        mediaPlayer.release();
        //stop handler thread
        handler.removeCallbacks(UpdateProgress);
        super.onDestroy();
    }

    /**
     * Handles the click event of play button
     * @param view
     */
    public void onPlay(View view)
    {
        if(!mediaPlayer.isPlaying())
        {
            //switching between play and pause image views.
            imageViewPlay.setVisibility(View.GONE);
            imageViewPause.setVisibility(View.VISIBLE);
            mediaPlayer.start();
            handler.postDelayed(UpdateProgress,100);
        }
    }

    /**
     * Handles the click event of pause button
     * @param view
     */
    public void onPause(View view)
    {
        if(mediaPlayer.isPlaying())
        {
            //switching between pause and play image views.
            imageViewPause.setVisibility(View.GONE);
            imageViewPlay.setVisibility(View.VISIBLE);
            mediaPlayer.pause();
        }
    }


    /**
     * Initliazes seekbar OnChangeListener to handle
     * any mannual changes to seek bar. It is responsible for
     * seeking media player to requested position.
     */
    private void seekBarProgress()
    {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                if(fromUser)
                {
                    int currentMilliSec = (int) Math.floor((seekBar.getProgress()/100F)*totalDuration);
                    mediaPlayer.seekTo(currentMilliSec);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {

            }
        });
    }
    /**
     * This interface is responsible for updating seek bar and
     * elapsed time of currently playing song.
     */
    private Runnable UpdateProgress = new Runnable()
    {
        @Override
        public void run()
        {
            long currentTime = mediaPlayer.getCurrentPosition();
            long progress = (long) (Math.floor(currentTime*100F / totalDuration));

            //Update Seek bar
            seekBar.setProgress((int) progress);

            //Update elapsed time
            long currentMinute = TimeUnit.MILLISECONDS.toMinutes(currentTime);
            long currentSecond = TimeUnit.MILLISECONDS.toSeconds(currentTime) % 60L;
            textViewCurrentPosition.setText(String.format("%02d:%02d",currentMinute,currentSecond));

            //Repeat this every 15ms
            handler.postDelayed(this,100);
        }
    };
}

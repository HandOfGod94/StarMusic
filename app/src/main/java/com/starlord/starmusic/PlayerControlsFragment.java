package com.starlord.starmusic;


import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;


/**
 * This class is responsible for handling
 * button events.
 */
public class PlayerControlsFragment extends Fragment
{
    public long totalDuration, totalMinute,totalSeconds;
    public MediaPlayer mediaPlayer;

    private View fragmentView;
    private Handler handler = new Handler();

    protected SeekBar seekBar;
    protected TextView textViewCurrentTime;

    public Drawable drawablePlay,drawablePause;
    public ImageView buttonPlay, buttonPause;

    public PlayerControlsFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
//        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        fragmentView = inflater.inflate(R.layout.fragment_player_controls, container, false);
        Uri filePath = Uri.parse("android.resource://com.starlord.starmusic/" + R.raw.song );

        //Initialize play and pause drawable so it can be used during clicking event
        drawablePause = fragmentView.getResources().getDrawable(R.drawable.pause);
        drawablePlay = fragmentView.getResources().getDrawable(R.drawable.play);

        mediaPlayer = MediaPlayer.create(getActivity(),filePath);
        seekBar = (SeekBar) fragmentView.findViewById(R.id.seekBar);
        TextView textViewTotalTime = (TextView) fragmentView.findViewById(R.id.textViewTotalTime);
        textViewCurrentTime = (TextView) fragmentView.findViewById(R.id.textViewCurrentTime);

        //Get total duration of currently playing song in milliseconds and
        // calculate total minutes ans total seconds and update it's TextView
        totalDuration = mediaPlayer.getDuration();
        totalMinute = TimeUnit.MILLISECONDS.toMinutes(totalDuration);
        totalSeconds = TimeUnit.MILLISECONDS.toSeconds(totalDuration) % 60L;
        textViewTotalTime.setText(String.format("%02d:%02d",totalMinute,totalSeconds));

        //action handling event calls
        buttonPlay = (ImageView) fragmentView.findViewById(R.id.buttonPlay);
        buttonPause = (ImageView) fragmentView.findViewById(R.id.buttonPause);
        actionPlayPause();
        actionSeekBar(seekBar);

        return fragmentView;
    }

    /**
     *
     * This method defines click listener action for
     * play pause button and take action accordingly for
     * current media player.
     */
    protected void actionPlayPause()
    {
        buttonPlay.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mediaPlayer.start();
                buttonPlay.setVisibility(View.INVISIBLE);
                buttonPause.setVisibility(View.VISIBLE);
                handler.postDelayed(UpdateProgress,100);
            }
        });

        buttonPause.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mediaPlayer.pause();
                buttonPause.setVisibility(View.INVISIBLE);
                buttonPlay.setVisibility(View.VISIBLE);
            }
        });
    }


    /**
     * This method is responsible for handling Seek Bar press
     * and drag events which will change current time of media being played.
     * @param seekBar seek bar which is to be controlled
     */
    protected void actionSeekBar(SeekBar seekBar)
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

            //These methods are helpful in changing textViewElapsedTime when user starts dragging.
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
            long totalTime = mediaPlayer.getDuration();
            long progress = (long) (Math.floor(currentTime*100F / totalTime));

            //Update Seek bar
            seekBar.setProgress((int) progress);

            //Update elapsed time
            long currentMinute = TimeUnit.MILLISECONDS.toMinutes(currentTime);
            long currentSecond = TimeUnit.MILLISECONDS.toSeconds(currentTime) % 60L;
            textViewCurrentTime.setText(String.format("%02d:%02d",currentMinute,currentSecond));

            //Repeat this every 15ms
            handler.postDelayed(this,100);
        }
    };
}

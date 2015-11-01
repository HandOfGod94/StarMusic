package com.starlord.starmusic;


import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;


/**
 * This class is responsible for handling
 * button events.
 */
public class PlayerControlsFragment extends Fragment
{
    View fragmentView;
    MediaPlayer mediaPlayer;
    SeekBar seekBar;
    TextView textViewCurrentTime;
    Handler handler = new Handler();

    public long totalMinute,totalSeconds;

    public PlayerControlsFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        fragmentView = inflater.inflate(R.layout.fragment_player_controls, container, false);
        Uri filePath = Uri.parse("android.resource://com.starlord.starmusic/" + R.raw.song );

        mediaPlayer = MediaPlayer.create(getActivity(),filePath);
        seekBar = (SeekBar) fragmentView.findViewById(R.id.seekBar);
        TextView textViewTotalTime = (TextView) fragmentView.findViewById(R.id.textViewTotalTime);
        textViewCurrentTime = (TextView) fragmentView.findViewById(R.id.textViewCurrentTime);

        totalMinute = TimeUnit.MILLISECONDS.toMinutes(mediaPlayer.getDuration());
        totalSeconds = TimeUnit.MILLISECONDS.toSeconds(mediaPlayer.getDuration()) % 60L;

        textViewTotalTime.setText(String.format("%02d:%02d",totalMinute,totalSeconds));

        Button buttonPlayPause = (Button) fragmentView.findViewById(R.id.buttonPlayPause);
        actionPlayPause(buttonPlayPause);

        return fragmentView;
    }


    /**
     * This method defines click listener action for
     * play pause button and take action accordingly for
     * current media player.
     * @param button play/pause button obtained from finding by id.
     */
    public void actionPlayPause(final Button button)
    {
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (button.getText().equals(">"))
                {
                    mediaPlayer.start();
                    button.setText("| |");
                }

                else if(button.getText().equals("| |"))
                {
                    mediaPlayer.pause();
                    button.setText(">");
                }
                handler.postDelayed(UpdateProgress,100);
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
            long progress = (long) (Math.ceil(currentTime*100F / totalTime));

            //Update Seek bar
            seekBar.setProgress((int) progress);

            //Update elapsed time
            long currentMinute = TimeUnit.MILLISECONDS.toMinutes(currentTime);
            long currentSecond = TimeUnit.MILLISECONDS.toSeconds(currentTime) % 60L;
            textViewCurrentTime.setText(String.format("%02d:%02d",currentMinute,currentSecond));

            //Repeat this every 100ms
            handler.postDelayed(this,100);
        }
    };
}

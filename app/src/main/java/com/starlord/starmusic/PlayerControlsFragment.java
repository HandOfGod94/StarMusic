package com.starlord.starmusic;


import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * This class is responsible for handling
 * button events.
 */
public class PlayerControlsFragment extends Fragment
{
    View fragmentView;
    MediaPlayer mediaPlayer;

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

            }
        });
    }
}

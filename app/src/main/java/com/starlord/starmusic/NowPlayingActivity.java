package com.starlord.starmusic;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;

public class NowPlayingActivity extends Activity
{
    //Note: ActionBarActivity is deprecated, but I'm using dependency 21.0.0, which doesn't have AppCompatActivity

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_playing);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }
}

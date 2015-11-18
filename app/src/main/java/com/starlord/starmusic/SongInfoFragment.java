package com.starlord.starmusic;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.starlord.starmusic.com.starlord.starmusic.com.starlord.starmusic.services.BitmapRescaleTask;

import java.io.File;


/**
 * This fragment is responsible for initialing and retrieving
 * song details
 */
public class SongInfoFragment extends Fragment
{
    private View fragmentView;

    protected String title,artist,album;
    protected int track = -1;
    protected Bitmap albumArt;

    private TextView textViewTitle,textViewAlbum,textViewArtist;
    private BitmapRescaleTask bitmapRescaleTask;

    public SongInfoFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        fragmentView = inflater.inflate(R.layout.fragment_song_info,container,false);
        Uri filePath = Uri.fromFile(new File(Environment.getExternalStorageDirectory().getPath()+"/Music/song.mp3"));
        textViewTitle = (TextView) fragmentView.findViewById(R.id.textViewTitle);
        textViewAlbum = (TextView) fragmentView.findViewById(R.id.textViewAlbum);
        textViewArtist = (TextView) fragmentView.findViewById(R.id.textViewArtist);
        ImageView imageViewAlbumArt = (ImageView) fragmentView.findViewById(R.id.imageViewAlbumArt);

        setMetaData(getActivity(),filePath);

        String trackString = (track==-1)?"--":String.format("%02d",track);
        String titleString  = trackString +". "+ title;
        if(title!=null) textViewTitle.setText(titleString);
        if(album!=null) textViewAlbum.setText("Album: "+ album);
        if(artist!=null) textViewArtist.setText("Artist: " + artist);
//        if(albumArt!=null) imageViewAlbumArt.setImageBitmap(albumArt);

        return fragmentView;
    }

    @Override
    public void onDestroy()
    {
        bitmapRescaleTask.cancel(true);
        super.onDestroy();
    }

    /**
     * This method reads ID3 tags from mp3 and assign it to appropriate values
     * @param context activity context
     * @param filePath Uri of input file
     */
    private void setMetaData(Context context,Uri filePath)
    {

        MediaMetadataRetriever metadataRetriever = new MediaMetadataRetriever();
        metadataRetriever.setDataSource(context, filePath);

        track = Integer.parseInt(metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_CD_TRACK_NUMBER));
        title = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        album = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
        artist = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST);
        byte[] albumArtBytes = metadataRetriever.getEmbeddedPicture();
        if(albumArtBytes!=null)
        {
//            InputStream inputStream = new ByteArrayInputStream(albumArtBytes);
            ImageView imageViewAlbumArt = (ImageView) fragmentView.findViewById(R.id.imageViewAlbumArt);
            bitmapRescaleTask = new BitmapRescaleTask(imageViewAlbumArt,getActivity(),albumArtBytes);
            bitmapRescaleTask.execute(300,300);
        }
    }
}

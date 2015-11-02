package com.starlord.starmusic;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * This fragment is responsible for initialing and retriving
 * song details
 */
public class SongInfoFragment extends Fragment
{
    private View fragmentView;

    protected String title,artist,album;
    protected Bitmap albumArt;

    public SongInfoFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        fragmentView = inflater.inflate(R.layout.fragment_song_info,container,false);
        Uri filePath = Uri.parse("android.resource://com.starlord.starmusic/" + R.raw.song );
        TextView textViewTitle = (TextView) fragmentView.findViewById(R.id.textViewTitle);
        TextView textViewAlbum = (TextView) fragmentView.findViewById(R.id.textViewAlbum);
        TextView textViewArtist = (TextView) fragmentView.findViewById(R.id.textViewArtist);
        ImageView imageViewAlbumArt = (ImageView) fragmentView.findViewById(R.id.imageViewAlbumArt);

        setMetaData(getActivity(),filePath);

        textViewTitle.setText(title);
        textViewAlbum.setText(album);
        textViewArtist.setText(artist);
        imageViewAlbumArt.setImageBitmap(albumArt);

        return fragmentView;
    }


    /**
     * This method reads ID3 tags from mp3 and assigne it to appropriate values
     * @param context activity context
     * @param filePath Uri of source
     */
    private void setMetaData(Context context,Uri filePath)
    {

        MediaMetadataRetriever metadataRetriever = new MediaMetadataRetriever();
        metadataRetriever.setDataSource(context, filePath);

        title = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        album = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
        artist = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST);
        byte[] albumArtBytes = metadataRetriever.getEmbeddedPicture();
        albumArt = BitmapFactory.decodeByteArray(albumArtBytes,0,albumArtBytes.length);
    }
}

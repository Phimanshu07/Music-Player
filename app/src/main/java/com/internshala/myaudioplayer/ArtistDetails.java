package com.internshala.myaudioplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import static com.internshala.myaudioplayer.MainActivity.musicFiles;

public class ArtistDetails extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageView artistPhoto;
    String artistName;
    ArrayList<MusicFiles> artistSong=new ArrayList<>(  );

    ArtistDetailsAdapter artistDetailsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_artist_details );
        recyclerView=findViewById( R.id.recyclerview_artist );
        artistPhoto=findViewById( R.id.artistphoto );
        artistName=getIntent().getStringExtra( "artistName" );
        int j=0;
        for(int i=0; i<musicFiles.size();i++){
            if(artistName.contains( musicFiles.get( i ).getArtist() )){
                artistSong.add( j,musicFiles.get( i ) );
                j++;
            }
        }
        byte[] image=getArtistArt( artistSong.get( 0 ).getPath() );
        if(image!=null){
            Glide.with( this )
                    .load( image )
                    .into( artistPhoto );
        }
        else {

            Glide.with( this )
                    .load( R.drawable.music )
                    .into( artistPhoto );
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(!((artistSong.size()) <1)){
            artistDetailsAdapter=new ArtistDetailsAdapter( this,artistSong );
            recyclerView.setAdapter( artistDetailsAdapter );
            recyclerView.setLayoutManager( new LinearLayoutManager( this,RecyclerView.VERTICAL,false ) );
        }
    }
    public  byte[] getArtistArt(String uri){
        MediaMetadataRetriever retriever=new MediaMetadataRetriever();
        retriever.setDataSource( uri );
        byte[] art=retriever.getEmbeddedPicture();
        return art;
    }
}
package com.internshala.myaudioplayer;

import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.MyArtistHolder> {
    private ArrayList<MusicFiles> artistFiles;
    private Context mContext1;
    View view;

    public ArtistAdapter( Context mContext1,ArrayList<MusicFiles> artistFiles) {

        this.mContext1 = mContext1;
        this.artistFiles = artistFiles;
    }

    @NonNull
    @Override
    public MyArtistHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view= LayoutInflater.from( mContext1 ).inflate( R.layout.artist_items,parent,false );
        return new MyArtistHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull MyArtistHolder holder, final int position) {

        holder.artist_name.setText( artistFiles.get( position ).getArtist() );
        final byte[] image=getArtist( artistFiles.get( position ).getPath() );
        if(image!=null){
            Glide.with( mContext1 ).asBitmap()
                    .load(  R.drawable.music )
                    .into( holder.artist_images );
        }
        else{
            Glide.with( mContext1 ).asBitmap()
                    .load( R.drawable.music )
                    .into( holder.artist_images );
        }
        holder.itemView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent( mContext1,ArtistDetails.class );
                intent.putExtra( "artistName",artistFiles.get( position ).getArtist() );
                mContext1.startActivity( intent );
            }
        } );
    }

    @Override
    public int getItemCount() {
        return artistFiles.size();
    }

    public class MyArtistHolder extends RecyclerView.ViewHolder {
        ImageView artist_images;
        TextView artist_name;
        public MyArtistHolder(@NonNull View itemView) {
            super( itemView );
            artist_images=itemView.findViewById( R.id.artist_image );
            artist_name=itemView.findViewById( R.id.artist_name );

        }
    }
    public  byte[] getArtist(String uri){
        MediaMetadataRetriever retriever=new MediaMetadataRetriever();
        retriever.setDataSource( uri );
        byte[] art=retriever.getEmbeddedPicture();
        return art;
    }
    void updatelist(ArrayList<MusicFiles> musicFilesArrayList){
        artistFiles=new ArrayList<>(  );
        artistFiles.addAll( musicFilesArrayList );
        notifyDataSetChanged();
    }
}

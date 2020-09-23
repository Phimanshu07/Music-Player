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


public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.MyHolder> {

    static ArrayList<MusicFiles> albumFiles;
    private Context mContext;
    View view;

    public AlbumAdapter( Context mContext,ArrayList<MusicFiles> albumFiles) {
        this.mContext = mContext;
        this.albumFiles = albumFiles;
    }



    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view= LayoutInflater.from( mContext ).inflate( R.layout.album_items,parent,false );
        return new MyHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {


        holder.album_name.setText( albumFiles.get( position ).getAlbum() );
        final byte[] image=getAlbum( albumFiles.get( position ).getPath() );
        if(image!=null){
            Glide.with( mContext ).asBitmap()
                    .load( image )
                    .into( holder.album_images );
        }
        else{
            Glide.with( mContext ).asBitmap()
                    .load( R.drawable.music )
                    .into( holder.album_images );
        }
        holder.itemView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent( mContext,AlbumDetails.class );
                intent.putExtra( "albumName",albumFiles.get( position ).getAlbum() );
                mContext.startActivity( intent );
            }
        } );
    }

    @Override
    public int getItemCount() {
        return albumFiles.size();
    }


    public class MyHolder extends RecyclerView.ViewHolder{
        ImageView album_images;
        TextView album_name;
        public MyHolder(@NonNull View itemView) {
            super( itemView );
            album_images=itemView.findViewById( R.id.album_image );
            album_name=itemView.findViewById( R.id.album_name );
        }
    }
    public  byte[] getAlbum(String uri){
        MediaMetadataRetriever retriever=new MediaMetadataRetriever();
        retriever.setDataSource( uri );
        byte[] art=retriever.getEmbeddedPicture();
        return art;
    }
    void updatelist(ArrayList<MusicFiles> musicFilesArrayList){
        albumFiles=new ArrayList<>(  );
        albumFiles.addAll( musicFilesArrayList );
        notifyDataSetChanged();
    }
}




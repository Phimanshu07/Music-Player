package com.internshala.myaudioplayer;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ArtistDetailsAdapter extends RecyclerView.Adapter<ArtistDetailsAdapter.MyArtistHolder>  {
    static ArrayList<MusicFiles> artistFiles;
    private Context mContext1;
    View view;
    public ArtistDetailsAdapter(Context mContext, ArrayList<MusicFiles> artistFiles) {
        this.mContext1 = mContext;
        this.artistFiles = artistFiles;
    }


    @NonNull
    @Override
    public MyArtistHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view= LayoutInflater.from( mContext1 ).inflate( R.layout.music_item,parent,false );
        return new MyArtistHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull MyArtistHolder holder, final int position) {

        holder.artist_name.setText( artistFiles.get( position ).getTitle() );
        final byte[] image=getArtist( artistFiles.get( position ).getPath() );
        if(image!=null){
            Glide.with( mContext1 ).asBitmap()
                    .load( image )
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
                Intent intent=new Intent( mContext1,PlayerActivity.class );
                intent.putExtra( "sender1","artistDetails" );
                intent.putExtra( "position",position );
                mContext1.startActivity( intent );
            }
        } );
        holder.MenuMore.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                PopupMenu popupMenu=new PopupMenu( mContext1,v );
                popupMenu.getMenuInflater().inflate( R.menu.popup,popupMenu.getMenu() );
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener( new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.delete:
                                Toast.makeText( mContext1, "Delete Clicked!", Toast.LENGTH_SHORT ).show();
                                deleteFile( position, v );
                                break;
                        }
                        return true;
                    }
                } );
            }
        } );
    }

    private void deleteFile(int position,View v){
        Uri contentUri= ContentUris.withAppendedId( MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                Long.parseLong( artistFiles.get( position ).getId() ) );//content
        File file=new File( artistFiles.get( position ).getPath() );
        boolean  deleted=file.delete();//delete your file
        if(deleted) {
            mContext1.getContentResolver().delete( contentUri,null,null );
            artistFiles.remove( position );
            notifyItemRemoved( position );
            notifyItemRangeChanged( position, artistFiles.size() );
            Snackbar.make( v, "File Deleted :", Snackbar.LENGTH_LONG )
                    .show();
        }
        else {
            //may be file in sd card
            Snackbar.make( v, "Cant't be Deleted :", Snackbar.LENGTH_LONG )
                    .show();

        }
    }
    @Override
    public int getItemCount() {
        return artistFiles.size();
    }

    public class MyArtistHolder extends RecyclerView.ViewHolder {
        ImageView artist_images,MenuMore;
        TextView artist_name;
        public MyArtistHolder(@NonNull View itemView) {
            super( itemView );
            artist_images=itemView.findViewById( R.id.music_img );
            artist_name=itemView.findViewById( R.id.music_file_name );
            MenuMore=itemView.findViewById( R.id.menuMore );
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

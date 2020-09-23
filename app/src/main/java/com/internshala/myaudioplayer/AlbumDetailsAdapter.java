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


public class AlbumDetailsAdapter extends RecyclerView.Adapter<AlbumDetailsAdapter.MyHolder> {

    static ArrayList<MusicFiles> albumFiles;
    private Context mContext;
    View view;

    public AlbumDetailsAdapter( Context mContext,ArrayList<MusicFiles> albumFiles) {
        this.mContext = mContext;
        this.albumFiles = albumFiles;
    }



    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view= LayoutInflater.from( mContext ).inflate( R.layout.music_item,parent,false );
        return new MyHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {


        holder.album_name.setText( albumFiles.get( position ).getTitle() );
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
                Intent intent=new Intent( mContext,PlayerActivity.class );
                intent.putExtra( "sender","albumDetails" );
                intent.putExtra( "position",position );
                mContext.startActivity( intent );
            }
        } );
        holder.MenuMore.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                PopupMenu popupMenu=new PopupMenu( mContext,v );
                popupMenu.getMenuInflater().inflate( R.menu.popup,popupMenu.getMenu() );
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener( new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.delete:
                                Toast.makeText( mContext, "Delete Clicked!", Toast.LENGTH_SHORT ).show();
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
                Long.parseLong( albumFiles.get( position ).getId() ) );//content
        File file=new File( albumFiles.get( position ).getPath() );
        boolean  deleted=file.delete();//delete your file
        if(deleted) {
            mContext.getContentResolver().delete( contentUri,null,null );
            albumFiles.remove( position );
            notifyItemRemoved( position );
            notifyItemRangeChanged( position, albumFiles.size() );
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
        return albumFiles.size();
    }


    public class MyHolder extends RecyclerView.ViewHolder{
        ImageView album_images,MenuMore;
        TextView album_name;
        public MyHolder(@NonNull View itemView) {
            super( itemView );
            album_images=itemView.findViewById( R.id.music_img );
            album_name=itemView.findViewById( R.id.music_file_name );
            MenuMore=itemView.findViewById( R.id.menuMore );
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




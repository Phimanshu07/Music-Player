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

public class MusicAdpater extends RecyclerView.Adapter<MusicAdpater.MyViewHolder> {

    private Context mcontext;
    static ArrayList<MusicFiles> mfiles;

    MusicAdpater(Context mcontext,ArrayList<MusicFiles> mfiles){
        this.mfiles=mfiles;
        this.mcontext=mcontext;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from( mcontext ).inflate(  R.layout.music_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        holder.file_name.setText( mfiles.get( position ).getTitle() );
        byte[] image=getAlbum( mfiles.get( position ).getPath() );
        if(image!=null){
            Glide.with( mcontext ).asBitmap()
                    .load( image )
                    .into( holder.album_art );
        }
        else{
            Glide.with( mcontext ).asBitmap()
                    .load( R.drawable.music )
                    .into( holder.album_art );
        }

        holder.itemView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent( mcontext,PlayerActivity.class );
                intent.putExtra( "position",position );
                mcontext.startActivity( intent );
            }
        } );
        holder.MenuMore.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                PopupMenu popupMenu=new PopupMenu( mcontext,v );
                popupMenu.getMenuInflater().inflate( R.menu.popup,popupMenu.getMenu() );
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener( new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.delete:
                                Toast.makeText( mcontext, "Delete Clicked!", Toast.LENGTH_SHORT ).show();
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
                Long.parseLong( mfiles.get( position ).getId() ) );//content
        File file=new File( mfiles.get( position ).getPath() );
        boolean  deleted=file.delete();//delete your file
        if(deleted) {
            mcontext.getContentResolver().delete( contentUri,null,null );
            mfiles.remove( position );
            notifyItemRemoved( position );
            notifyItemRangeChanged( position, mfiles.size() );
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
        return mfiles.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView file_name;
        ImageView album_art,MenuMore;
        public MyViewHolder(@NonNull View itemView) {
            super( itemView );
            file_name=itemView.findViewById( R.id.music_file_name );
            album_art=itemView.findViewById( R.id.music_img );
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
        mfiles=new ArrayList<>(  );
        mfiles.addAll( musicFilesArrayList );
        notifyDataSetChanged();
    }
}

package com.internshala.myaudioplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private static final int REQUEST_CODE =1 ;
    static   ArrayList<MusicFiles> musicFiles;
    static boolean shuffleBoolean=false,repeatBoolean=false;
    static ArrayList<MusicFiles> albums=new ArrayList<>(  );
    static ArrayList<MusicFiles> artists=new ArrayList<>(  );
    private String Sort_Pref_Order="SortOrder";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        permission();


    }

    private void permission() {
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE )!= PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions( MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE );

        }
        else {

                musicFiles=getAllAudio(this  );
                initViewPager();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult( requestCode, permissions, grantResults );
        if(requestCode==REQUEST_CODE){

            if(grantResults.length>0 && grantResults[0]  == PackageManager.PERMISSION_GRANTED){
                //do whatever you want permission  related;

                musicFiles=getAllAudio( this );
                initViewPager();
            }
            else {
                ActivityCompat.requestPermissions( MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE );

            }

        }
    }

    private void initViewPager() {
        ViewPager viewPager=findViewById( R.id.viewpager );
        TabLayout tabLayout=findViewById( R.id.tab_layout );
        ViewPagerADPATER viewPagerADPATER =new ViewPagerADPATER(  getSupportFragmentManager());
        viewPagerADPATER.addFragment( new SongFragment(),"Songs" );
        viewPagerADPATER.addFragment( new AlbumFragment(),"Album" );
        viewPagerADPATER.addFragment( new ArtistFragment(),"Artist" );
        viewPager.setAdapter( viewPagerADPATER );
        tabLayout.setupWithViewPager( viewPager );
    }



    public  static class ViewPagerADPATER extends FragmentPagerAdapter{

        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;
        public ViewPagerADPATER(@NonNull FragmentManager fm) {
            super( fm );
            this.fragments=new ArrayList<>(  );
            this.titles=new ArrayList<>(  );
        }

        void addFragment(Fragment fragment,String title){
            fragments.add( fragment );
            titles.add( title );
        }
        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get( position );
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position){
            return titles.get( position );
        }
    }

    public  ArrayList<MusicFiles> getAllAudio(Context context){
        SharedPreferences preferences=getSharedPreferences( Sort_Pref_Order,MODE_PRIVATE );
        String sortOrder=preferences.getString( "sorting","sortByName" );
        ArrayList<String> duplicate=new ArrayList<>(  );
        albums.clear();
        artists.clear();
        ArrayList<MusicFiles> tempAudioList=new ArrayList<>(  );
        String order=null;
        Uri uri= MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        switch (sortOrder){
            case "sortByName":
                order=MediaStore.MediaColumns.DISPLAY_NAME + " ASC";
                break;
            case "sortByDate":
                order=MediaStore.MediaColumns.DATE_ADDED + " ASC";
                break;
            case "sortBySize":
                order=MediaStore.MediaColumns.SIZE + " DESC";
                break;
        }
        String[] projection={
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,//for path
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media._ID


        };

        Cursor cursor=context.getContentResolver().query( uri,projection,null,null,order );

                       if(cursor !=null){
                    while (cursor.moveToNext()){
                        String album=cursor.getString( 0 );
                        String title=cursor.getString( 1 );
                        String duration=cursor.getString( 2 );
                        String path=cursor.getString( 3 );
                        String artist=cursor.getString( 4 );
                        String id=cursor.getString( 5 );

                MusicFiles musicFiles=new MusicFiles( path,title,artist,album,duration,id );
              //take log.e for check
                Log.e("Path:"+path,"Album:"+album);
                tempAudioList.add( musicFiles );
                if(!duplicate.contains( album )){
                    albums.add( musicFiles );
                    duplicate.add( album );
                }
                if(!duplicate.contains( artist )){
                    artists.add(musicFiles);
                    duplicate.add( artist );
                }
            }
            cursor.close();
        }
        return tempAudioList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.search,menu );
        MenuItem menuitem=menu.findItem( R.id.search_option );
        SearchView searchView=(SearchView) menuitem.getActionView();
        searchView.setOnQueryTextListener( this );
        return super.onCreateOptionsMenu( menu );
    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String userInput=newText.toLowerCase();
        ArrayList<MusicFiles> myfiles=new ArrayList<>(  );
        ArrayList<MusicFiles> myalbum=new ArrayList<>(  );
        ArrayList<MusicFiles> myartist=new ArrayList<>(  );
        for(MusicFiles song:musicFiles){
            if(song.getTitle().toLowerCase().contains( userInput )){
                myfiles.add( song );
            }
        }
        SongFragment.musicAdpater.updatelist( myfiles );

        for(MusicFiles song:albums){
            if(song.getAlbum().toLowerCase().contains( userInput )){
                myalbum.add( song );
            }
        }
        AlbumFragment.albumAdapter.updatelist( myalbum );
        for(MusicFiles song:artists){
            if(song.getArtist().toLowerCase().contains( userInput )){
                myartist.add( song );
            }
        }
        ArtistFragment.artistAdapter.updatelist( myartist );

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        SharedPreferences.Editor editor=getSharedPreferences( Sort_Pref_Order,MODE_PRIVATE ).edit();
        switch (item.getItemId()){
            case R.id.by_name:
                editor.putString( "sorting","sortByName" );
                editor.apply();
                this.recreate();
                break;
            case R.id.by_date:
                editor.putString( "sorting","sortByDate" );
                editor.apply();
                this.recreate();
                break;
            case R.id.by_size:
                editor.putString( "sorting","sortBySize" );
                editor.apply();
                this.recreate();
                break;
        }
        return super.onOptionsItemSelected( item );
    }
}
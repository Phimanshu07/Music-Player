package com.internshala.myaudioplayer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.media.session.MediaSessionCompat;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Random;

import static com.internshala.myaudioplayer.AlbumDetailsAdapter.albumFiles;
import static com.internshala.myaudioplayer.ArtistDetailsAdapter.artistFiles;
import static com.internshala.myaudioplayer.MainActivity.musicFiles;
import static com.internshala.myaudioplayer.MainActivity.repeatBoolean;
import static com.internshala.myaudioplayer.MainActivity.shuffleBoolean;
import static com.internshala.myaudioplayer.MusicAdpater.mfiles;

public class PlayerActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener {
    TextView  song_name,artist_name,duration_played,duration_total;
    ImageView cover_art,nextBtn,prebtn,backbtn,shufflebtn,repeatbtn;
    FloatingActionButton playpausebtn;
    SeekBar seekBar;
    boolean musicisplayed=false;
    static int Duration= 0;
    int position=-1;
    Uri uri;

    static MediaPlayer mediaPlayer;
    static ArrayList<MusicFiles> listSong=new ArrayList<>(  );
    private Handler handler=new Handler(  );
    private Thread playthread,prethread,nextthread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_player );
        initViews();
        getIntentMethod();
        if(mediaPlayer.isPlaying()){
            createNotification();
        }
        song_name.setText( listSong.get( position ).getTitle() );
        artist_name.setText( listSong.get( position ).getArtist() );
        mediaPlayer.setOnCompletionListener( this );
        seekBar.setOnSeekBarChangeListener( new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
             if(mediaPlayer!=null && fromUser){
                 mediaPlayer.seekTo( progress*1000 );
             }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        } );
        PlayerActivity.this.runOnUiThread( new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer!=null ){


                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        seekBar.setProgress( mCurrentPosition );
                        duration_played.setText( formattedTime( mCurrentPosition ) );

                }
                handler.postDelayed( this,1000 );
            }
        } );
        shufflebtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(shuffleBoolean){
                    shuffleBoolean=false;
                    shufflebtn.setImageResource( R.drawable.ic_baseline_shuffle_off );
                }
                else {
                    shuffleBoolean=true;
                    shufflebtn.setImageResource( R.drawable.ic_baseline_shuffle_on );
                }
            }
        } );
        repeatbtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(repeatBoolean){
                    repeatBoolean=false;
                    repeatbtn.setImageResource( R.drawable.ic_baseline_repeat_off );
                }
                else {
                    repeatBoolean=true;
                    repeatbtn.setImageResource( R.drawable.ic_baseline_repeat_24 );
                }
            }
        } );
        backbtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        } );
    }

    private void createNotification() {

    }

    @Override
    protected void onResume() {
        playthreadBtn();
        prethreadBtn();
        nextthreadBtn();
        super.onResume();
    }

    private void nextthreadBtn() {

        nextthread=new Thread(  ){
            @Override
            public void run() {
                super.run();
                nextBtn.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nextbtnClicked();
                    }
                } );

            }
        };
        nextthread.start();
    }

    private void nextbtnClicked() {
        if(mediaPlayer.isPlaying()){

            mediaPlayer.stop();
            mediaPlayer.release();
            if(shuffleBoolean && !repeatBoolean){
                position=getRandom(listSong.size()-1);
            }
            else if(!shuffleBoolean && !repeatBoolean) {
                position = ((position + 1) % listSong.size());
            }
            //else position will be position
            uri=Uri.parse( listSong.get( position ).getPath() );
            mediaPlayer=MediaPlayer.create( getApplicationContext(),uri );
            metadata( uri );
            song_name.setText( listSong.get( position ).getTitle() );
            artist_name.setText( listSong.get( position ).getArtist() );
            seekBar.setMax(mediaPlayer.getDuration()/1000  );
            PlayerActivity.this.runOnUiThread( new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer!=null){
                        int mCurrentPosition=mediaPlayer.getCurrentPosition()/1000;
                        seekBar.setProgress( mCurrentPosition );
                    }
                    handler.postDelayed( this,1000 );
                }
            } );
            mediaPlayer.setOnCompletionListener( this );
            playpausebtn.setBackgroundResource( R.drawable.ic_baseline_pause_ );
            mediaPlayer.start();
        }
        else {

            mediaPlayer.stop();
            mediaPlayer.release();
            if(shuffleBoolean && !repeatBoolean){
                position=getRandom(listSong.size()-1);
            }
            else if(!shuffleBoolean && !repeatBoolean) {
                position = ((position + 1) % listSong.size());
            }
            uri=Uri.parse( listSong.get( position ).getPath() );
            mediaPlayer=MediaPlayer.create( getApplicationContext(),uri );
            metadata( uri );
            song_name.setText( listSong.get( position ).getTitle() );
            artist_name.setText( listSong.get( position ).getArtist() );
            seekBar.setMax(mediaPlayer.getDuration()/1000  );
            PlayerActivity.this.runOnUiThread( new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer!=null){
                        int mCurrentPosition=mediaPlayer.getCurrentPosition()/1000;
                        seekBar.setProgress( mCurrentPosition );
                    }
                    handler.postDelayed( this,1000 );
                }
            } );
            mediaPlayer.setOnCompletionListener( this );
            playpausebtn.setBackgroundResource( R.drawable.ic_baseline_play );

        }
    }

    private int getRandom(int i) {
        Random random=new Random(  );
        return random.nextInt(i+1);
    }

    private void prethreadBtn() {
        prethread=new Thread(  ){
            @Override
            public void run() {
                super.run();
                prebtn.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        prebtnClicked();
                    }
                } );

            }
        };
        prethread.start();

    }

    private void prebtnClicked() {
        if(mediaPlayer.isPlaying()){

            mediaPlayer.stop();
            mediaPlayer.release();
            if(shuffleBoolean && !repeatBoolean){
                position=getRandom(listSong.size()-1);
            }
            else if(!shuffleBoolean && !repeatBoolean) {
                position=((position -1) < 0 ? (listSong.size()-1):(position-1));
            }
            uri=Uri.parse( listSong.get( position ).getPath() );
            mediaPlayer=MediaPlayer.create( getApplicationContext(),uri );
            metadata( uri );
            song_name.setText( listSong.get( position ).getTitle() );
            artist_name.setText( listSong.get( position ).getArtist() );
            seekBar.setMax(mediaPlayer.getDuration()/1000  );
            PlayerActivity.this.runOnUiThread( new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer!=null){
                        int mCurrentPosition=mediaPlayer.getCurrentPosition()/1000;
                        seekBar.setProgress( mCurrentPosition );
                    }
                    handler.postDelayed( this,1000 );
                }
            } );
            mediaPlayer.setOnCompletionListener( this );
            playpausebtn.setBackgroundResource( R.drawable.ic_baseline_pause_ );
            mediaPlayer.start();
        }
        else {

            mediaPlayer.stop();
            mediaPlayer.release();
            if(shuffleBoolean && !repeatBoolean){
                position=getRandom(listSong.size()-1);
            }
            else if(!shuffleBoolean && !repeatBoolean) {
                position=((position -1) < 0 ? (listSong.size()-1):(position-1));
            }
            uri=Uri.parse( listSong.get( position ).getPath() );
            mediaPlayer=MediaPlayer.create( getApplicationContext(),uri );
            metadata( uri );
            song_name.setText( listSong.get( position ).getTitle() );
            artist_name.setText( listSong.get( position ).getArtist() );
            seekBar.setMax(mediaPlayer.getDuration()/1000  );
            PlayerActivity.this.runOnUiThread( new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer!=null){
                        int mCurrentPosition=mediaPlayer.getCurrentPosition()/1000;
                        seekBar.setProgress( mCurrentPosition );
                    }
                    handler.postDelayed( this,1000 );
                }
            } );
            mediaPlayer.setOnCompletionListener( this );
            playpausebtn.setBackgroundResource( R.drawable.ic_baseline_play );

        }

    }

    private void playthreadBtn() {

        playthread=new Thread(  ){
            @Override
            public void run() {
                super.run();
                playpausebtn.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playpausebtnClicked();
                    }
                } );

            }
        };
        playthread.start();
    }


    private void playpausebtnClicked() {
        if(mediaPlayer.isPlaying()){
            playpausebtn.setImageResource( R.drawable.ic_baseline_play );
            mediaPlayer.pause();
            seekBar.setMax(mediaPlayer.getDuration()/1000  );
            PlayerActivity.this.runOnUiThread( new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer!=null){
                        int mCurrentPosition=mediaPlayer.getCurrentPosition()/1000;
                        seekBar.setProgress( mCurrentPosition );
                    }
                    handler.postDelayed( this,1000 );
                }
            } );
        }
        else {
            playpausebtn.setImageResource( R.drawable.ic_baseline_pause_ );
            mediaPlayer.start();
            seekBar.setMax( mediaPlayer.getDuration()/1000 );
            PlayerActivity.this.runOnUiThread( new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer!=null){
                        int mCurrentPosition=mediaPlayer.getCurrentPosition()/1000;
                        seekBar.setProgress( mCurrentPosition );
                    }
                    handler.postDelayed( this,1000 );
                }
            } );
        }


    }

    private String formattedTime(int mCurrentPosition) {
        String totalout="";
        String totalNew="";
        String seconds=String.valueOf( mCurrentPosition%60 );
        String minutes=String.valueOf( mCurrentPosition/60 );
        totalout=minutes+":"+seconds;
        totalNew=minutes+":"+"0"+seconds;
        if(seconds.length()==1){
            return totalNew;
        }
        else {
           return   totalout;
        }

    }

    private void getIntentMethod() {
        position=getIntent().getIntExtra( "position",-1 );
        String sender=getIntent().getStringExtra( "sender" );
        String sender1=getIntent().getStringExtra( "sender1" );
        if(sender!=null && sender.equals( "albumDetails" )){
            listSong=albumFiles;
        }
        else if(sender1!=null && sender1.equals( "artistDetails" )){
            listSong=artistFiles;
        }
        else {
            listSong = mfiles;
        }
        if(listSong!=null){
            playpausebtn.setImageResource( R.drawable.ic_baseline_pause_ );
            uri=Uri.parse( listSong.get( position ).getPath() );
        }
        if (mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer=MediaPlayer.create( getApplicationContext(),uri );
            mediaPlayer.start();
        }
        else {
            mediaPlayer=MediaPlayer.create( getApplicationContext(),uri );
            mediaPlayer.start();
        }
        seekBar.setMax( mediaPlayer.getDuration()/1000 );
        metadata( uri );
    }

    private void initViews() {
        song_name=findViewById( R.id.song_name );
        artist_name=findViewById( R.id.song_artist );
        duration_played=findViewById( R.id.durationplayed );
        duration_total=findViewById( R.id.durationtotal );
        cover_art=findViewById( R.id.cover_art );
        nextBtn=findViewById( R.id.id_next );
        prebtn=findViewById(R.id.  id_pre);
        backbtn=findViewById(R.id.back_btn  );
        shufflebtn=findViewById(R.id. id_shuffle );
        repeatbtn=findViewById(R.id. id_repeat );
        playpausebtn=findViewById(R.id. play_pause );
        seekBar=findViewById(R.id. SeekBar );
    }

    private void metadata(Uri uri){
        MediaMetadataRetriever retriever=new MediaMetadataRetriever();
        retriever.setDataSource( uri.toString() );
        int durationtotal=Integer.parseInt( listSong.get( position ).getDuration() )/1000;
        duration_total.setText( formattedTime( durationtotal ) );
        byte[] art=retriever.getEmbeddedPicture();
        Bitmap bitmap ;
        if(art!=null){

            bitmap= BitmapFactory.decodeByteArray( art,0,art.length );
            ImageAnimation( this,cover_art,bitmap );
            Palette.from( bitmap ).generate( new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(@Nullable Palette palette) {
                    Palette.Swatch swatch=palette.getDominantSwatch();
                    if(swatch!=null){
                        ImageView gradient=findViewById( R.id.imageViewGradient );
                        RelativeLayout mContainer=findViewById( R.id.mContainer );
                        gradient.setBackgroundResource( R.drawable.gradient_bg );
                        mContainer.setBackgroundResource( R.drawable.main_bg );
                        GradientDrawable gradientDrawable=new GradientDrawable( GradientDrawable.Orientation.BOTTOM_TOP,
                                new int[]{swatch.getRgb(),0x00000000});
                        gradient.setBackground( gradientDrawable );
                        GradientDrawable gradientDrawableBg=new GradientDrawable( GradientDrawable.Orientation.BOTTOM_TOP,
                                new int[]{swatch.getRgb(),swatch.getRgb()});
                        mContainer.setBackground( gradientDrawableBg );
                        song_name.setTextColor( swatch.getTitleTextColor() );
                        artist_name.setTextColor( swatch.getBodyTextColor() );
                    }
                    else {
                        ImageView gradient=findViewById( R.id.imageViewGradient );
                        RelativeLayout mContainer=findViewById( R.id.mContainer );
                        gradient.setBackgroundResource( R.drawable.gradient_bg );
                        mContainer.setBackgroundResource( R.drawable.main_bg );
                        GradientDrawable gradientDrawable=new GradientDrawable( GradientDrawable.Orientation.BOTTOM_TOP,
                                new int[]{0xff000000,0x00000000});
                        gradient.setBackground( gradientDrawable );
                        GradientDrawable gradientDrawableBg=new GradientDrawable( GradientDrawable.Orientation.BOTTOM_TOP,
                                new int[]{0xff000000,0xff000000});
                        mContainer.setBackground( gradientDrawableBg );
                        song_name.setTextColor( Color.WHITE );
                        artist_name.setTextColor(Color.DKGRAY);
                    }
                }
            } );
        }
        else {
            Glide.with( this )
                    .asBitmap()
                    .load( R.drawable.music )
                    .into( cover_art ) ;

            ImageView gradient=findViewById( R.id.imageViewGradient );
            RelativeLayout mContainer=findViewById( R.id.mContainer );
            gradient.setBackgroundResource( R.drawable.gradient_bg );
            mContainer.setBackgroundResource( R.drawable.main_bg );
            song_name.setTextColor( Color.WHITE );
            artist_name.setTextColor(Color.DKGRAY);
        }

    }
    public void ImageAnimation(final Context context, final ImageView imageView, final Bitmap bitmap){
        Animation animout= AnimationUtils.loadAnimation( context,android.R.anim.fade_out );
        final Animation animIn= AnimationUtils.loadAnimation( context,android.R.anim.fade_in );
        animout.setAnimationListener( new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
              Glide.with( context ).load( bitmap ).into( imageView );
              animIn.setAnimationListener( new Animation.AnimationListener() {
                  @Override
                  public void onAnimationStart(Animation animation) {

                  }

                  @Override
                  public void onAnimationEnd(Animation animation) {

                  }

                  @Override
                  public void onAnimationRepeat(Animation animation) {

                  }
              } );
              imageView.startAnimation( animIn );
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        } );
        imageView.startAnimation( animout );

    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        nextbtnClicked();
        if(mediaPlayer!=null){
            mediaPlayer=MediaPlayer.create( getApplicationContext(),uri );
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener( this );
        }
    }
}
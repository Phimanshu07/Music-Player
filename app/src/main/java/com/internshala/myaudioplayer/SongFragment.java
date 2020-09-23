package com.internshala.myaudioplayer;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import static com.internshala.myaudioplayer.MainActivity.musicFiles;


public class SongFragment extends Fragment {


    RecyclerView recyclerView;
    static MusicAdpater musicAdpater;
    public SongFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate( R.layout.fragment_song, container, false );
         recyclerView=view.findViewById( R.id.recyclerview );
         recyclerView.setHasFixedSize(true);
         if(!(musicFiles.size()<1)){
             musicAdpater=new MusicAdpater( getContext(),musicFiles );
             recyclerView.setAdapter( musicAdpater );
             recyclerView.setLayoutManager( new LinearLayoutManager( getContext(),RecyclerView.VERTICAL,false ) );
         }
        return view;
    }
}
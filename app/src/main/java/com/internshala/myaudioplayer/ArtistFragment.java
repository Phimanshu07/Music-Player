package com.internshala.myaudioplayer;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import static com.internshala.myaudioplayer.MainActivity.artists;
import static com.internshala.myaudioplayer.MainActivity.musicFiles;


public class ArtistFragment extends Fragment {

    RecyclerView recyclerView;
    static ArtistAdapter artistAdapter;

    public ArtistFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate( R.layout.fragment_artist, container, false );
        recyclerView=view.findViewById( R.id.recyclerview );
        recyclerView.setHasFixedSize(true);
        if(!(artists.size()<1)){
            artistAdapter=new ArtistAdapter( getContext(),artists );
            recyclerView.setAdapter( artistAdapter );
            recyclerView.setLayoutManager( new GridLayoutManager( getContext(),2 ));
        }
        return view;
    }
}
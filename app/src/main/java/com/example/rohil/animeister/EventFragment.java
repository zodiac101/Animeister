package com.example.rohil.animeister;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventFragment extends Fragment {

    ProgressBar progressBar;
    private RecyclerView recyclerView;
    TextView textViewNoEvent;
    int count;


    public EventFragment() {
        // Required empty public constructor
    }

    public static EventFragment newInstance(int sectionNumber) {
        EventFragment fragment = new EventFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_event, container, false);



        progressBar = (ProgressBar)rootView.findViewById(R.id.progressBarEventsFragment);



        final ArrayList<ListItem> listItems = new ArrayList<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("events");
        databaseReference.keepSynced(true);

        textViewNoEvent = (TextView)rootView.findViewById(R.id.textViewNoEvent);


        recyclerView = (RecyclerView)rootView.findViewById(R.id.eventRecyclerView);
        LinearLayoutManager layoutManager= new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        final EventListAdapter eventListAdapter = new EventListAdapter(listItems, getContext());
        recyclerView.setAdapter(eventListAdapter);

        count = 0;


        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                if (dataSnapshot.getKey().equals("count"))
                {
                    count = Integer.parseInt(dataSnapshot.getValue().toString());
                    if (count == 0){
                        progressBar.setVisibility(View.GONE);
                        textViewNoEvent.setVisibility(View.VISIBLE);
                    }
                }
                else {
                    textViewNoEvent.setVisibility(View.GONE);
                    listItems.add(new ListItem(dataSnapshot.child("name").getValue().toString(), dataSnapshot.child("pic").getValue().toString(), dataSnapshot.getKey()));
                    eventListAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;

    }

}

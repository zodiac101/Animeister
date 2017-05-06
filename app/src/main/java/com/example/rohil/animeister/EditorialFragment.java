package com.example.rohil.animeister;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
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
public class EditorialFragment extends Fragment {


    private ListView listView;
    ProgressBar progressBar;




    public EditorialFragment() {
        // Required empty public constructor
    }

    public static EditorialFragment newInstance(int sectionNumber) {
        EditorialFragment fragment = new EditorialFragment();
        return fragment;
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("editorials");
        databaseReference.keepSynced(true);

        final ArrayList<ListItem> editorialses = new ArrayList<ListItem>();


        View rootView = inflater.inflate(R.layout.fragment_editorial, container, false);







        listView = (ListView)rootView.findViewById(R.id.editorialList);

        final EditorialListAdapter editorialListAdapter = new EditorialListAdapter(getContext(), editorialses);
        listView.setAdapter(editorialListAdapter);

        progressBar = (ProgressBar)rootView.findViewById(R.id.progressBarEditorialFragment);




        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    editorialses.add(new ListItem(dataSnapshot.child("title").getValue().toString(), dataSnapshot.child("pic").getValue().toString(), dataSnapshot.getKey()));
                    editorialListAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);

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



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), EditorialActivity.class);
                intent.putExtra("id", editorialses.get(i).getId());
                intent.putExtra("title", editorialses.get(i).getTitle());
                intent.putExtra("pic", editorialses.get(i).getPicture());
                startActivity(intent);
            }
        });
        return rootView;
    }







}

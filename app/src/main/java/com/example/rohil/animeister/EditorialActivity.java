package com.example.rohil.animeister;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.UnsupportedEncodingException;

public class EditorialActivity extends AppCompatActivity {

    TextView textViewEditorialTitle;
    ImageView imageViewToolbar;
    TextView textViewEditorialContent;
    ProgressBar progressBar;

    String link;
    String title;

    private ShareActionProvider mShareActionProvider;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editorial, menu);

        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.menu_item_share);

        // Fetch and store ShareActionProvider
    //    mShareActionProvider = (ShareActionProvider) item.getActionProvider();

        // Return true to display menu
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editorial);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Editorial");


        progressBar = (ProgressBar)findViewById(R.id.progressBarEditorialActivity);

        Intent intent = getIntent();
        final String id = intent.getStringExtra("id");

        textViewEditorialTitle = (TextView)findViewById(R.id.textViewEditorialTitle);
        imageViewToolbar = (ImageView)findViewById(R.id.toolbarImage);
        textViewEditorialContent = (TextView)findViewById(R.id.textViewEditorialContent);

        textViewEditorialTitle.setText(intent.getStringExtra("title"));

        title = intent.getStringExtra("title");

        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageRef = storage.getReferenceFromUrl("gs://animeister-607c8.appspot.com");



        Glide.with(this)
                .using(new FirebaseImageLoader())
                .load(storageRef.child("editorials").child(id).child(intent.getStringExtra("pic")))
                .into(imageViewToolbar);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("editorials").child(id);




        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String contentAddr = dataSnapshot.child("content").getValue().toString();

                link = dataSnapshot.child("link").getValue().toString();


                final long ONE_MEGABYTE = 1024 * 1024;
                storageRef.child("editorials").child(id).child(contentAddr).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        // Data for "images/island.jpg" is returns, use this as needed
                        try {
                            textViewEditorialContent.setText(new String(bytes, "UTF-8"));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                        progressBar.setVisibility(View.GONE);


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_item_share){
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TITLE, title);
            intent.putExtra(Intent.EXTRA_TEXT, link);
            intent.putExtra(android.content.Intent.EXTRA_SUBJECT, title);
            startActivity(Intent.createChooser(intent, "Send article to.."));
        }


        return super.onOptionsItemSelected(item);
    }
}

package com.example.rohil.animeister;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;

public class EventActivity extends AppCompatActivity {



    TextView textViewEventName;
    TextView textViewDesc;
    TextView textViewVenue;
    TextView textViewDate;
    TextView textViewTime;
    ImageView imageView;
    ProgressBar progressBar;

    String day ;
    String month;
    String year;
    String hour;
    String minute;
    String title;
    String desc;
    String venue;
    String pic;
    String date;
    String time;

    File localFile;
    File file;

    StorageReference storageRef;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_event, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Intent intent = getIntent();

        String id  = intent.getStringExtra("id");
        pic = intent.getStringExtra("pic");

        //toolbar.setTitleTextColor(getResources().getColor(R.color.colorAccent));

        textViewVenue = (TextView)findViewById(R.id.textViewEventVenue);
        textViewDesc = (TextView)findViewById(R.id.textViewEventDesc);
        textViewDate = (TextView)findViewById(R.id.textViewEventDate);
        textViewTime = (TextView)findViewById(R.id.textViewEventTime);
        imageView = (ImageView)findViewById(R.id.imageViewEventPic);
        progressBar = (ProgressBar)findViewById(R.id.progressBarEventActivity);
        textViewEventName = (TextView)findViewById(R.id.textViewEventName);

        textViewEventName.setText(intent.getStringExtra("title"));

        title = intent.getStringExtra("title");



        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://animeister-607c8.appspot.com");

        Glide.with(this)
                .using(new FirebaseImageLoader())
                .load(storageRef.child("events").child(pic))
                .into(imageView);

        try {
            localFile = File.createTempFile("images", "jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }



        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("events").child(id);
        databaseReference.keepSynced(true);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                desc = dataSnapshot.child("desc").getValue().toString();
                date = dataSnapshot.child("date").getValue().toString();
                time = dataSnapshot.child("time").getValue().toString();
                venue = dataSnapshot.child("venue").getValue().toString();
                textViewDesc.setText(desc);
                textViewDate.setText(date);
                textViewTime.setText(time);
                textViewVenue.setText(venue);

                day = dataSnapshot.child("metadata").child("day").getValue().toString();
                month = dataSnapshot.child("metadata").child("month").getValue().toString();
                year = dataSnapshot.child("metadata").child("year").getValue().toString();
                hour = dataSnapshot.child("metadata").child("hour").getValue().toString();
                minute = dataSnapshot.child("metadata").child("minute").getValue().toString();

                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//        buttonRemind.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent calIntent = new Intent(Intent.ACTION_INSERT);
//                calIntent.setType("vnd.android.cursor.item/event");
//                calIntent.putExtra(CalendarContract.Events.TITLE, intent.getStringExtra("title"));
//                calIntent.putExtra(CalendarContract.Events.EVENT_LOCATION, textViewVenue.getText());
//                calIntent.putExtra(CalendarContract.Events.DESCRIPTION, textViewDesc.getText());
//
//                Calendar cal = Calendar.getInstance();
//                cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day[0]));
//                cal.set(Calendar.MONTH, Integer.parseInt(month[0]));
//                cal.set(Calendar.YEAR, Integer.parseInt(year[0]));
//                cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour[0]));
//                cal.set(Calendar.MINUTE, Integer.parseInt(minute[0]));
//                calIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, cal.getTime().getTime());
//
//                startActivity(calIntent);
//
//            }
//        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(), EventPosterActivity.class);
                intent1.putExtra("poster", pic);
                startActivity(intent1);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            file.delete();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_reminder){
            Intent calIntent = new Intent(Intent.ACTION_INSERT);
                calIntent.setType("vnd.android.cursor.item/event");
                calIntent.putExtra(CalendarContract.Events.TITLE,title);
                calIntent.putExtra(CalendarContract.Events.EVENT_LOCATION, venue);
                calIntent.putExtra(CalendarContract.Events.DESCRIPTION, desc);

                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));
                cal.set(Calendar.MONTH, Integer.parseInt(month));
                cal.set(Calendar.YEAR, Integer.parseInt(year));
                cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
                cal.set(Calendar.MINUTE, Integer.parseInt(minute));
                calIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, cal.getTime().getTime());

                startActivity(calIntent);
        }

        if (item.getItemId() == R.id.menu_item_share){

            progressBar.setVisibility(View.VISIBLE);


            storageRef.child("events").child(pic).getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                    progressBar.setVisibility(View.GONE);

                    File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "");
                    file = new File(dir, pic);
                    try {
                        copy(localFile, file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(file.getAbsolutePath()));
                    shareIntent.setType("image/jpeg");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, title+"\n"+desc+"\n"+"Venue: "+venue+"\nDate: "+date+"\nTime: "+time);
                    startActivityForResult(Intent.createChooser(shareIntent, "Send event to.."), 0);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });

        }


        return super.onOptionsItemSelected(item);
    }

    public void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

}

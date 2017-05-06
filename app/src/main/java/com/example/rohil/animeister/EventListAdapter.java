package com.example.rohil.animeister;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder>{

    Context mContext;
    List<ListItem> listItemList;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://animeister-607c8.appspot.com");

    public EventListAdapter(List<ListItem> listItemList, Context mContext) {
        this.listItemList = listItemList;
        this.mContext = mContext;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView eventTitle;
        public ImageView eventPic;
        public View view;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            eventTitle = (TextView) itemView.findViewById(R.id.articleTitle);
            eventPic = (ImageView)itemView.findViewById(R.id.imageViewEventItem);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_list_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final ListItem listItem = listItemList.get(position);
        holder.eventTitle.setText(listItem.getTitle());

        Glide.with(mContext)
                .using(new FirebaseImageLoader())
                .load(storageRef.child("events").child(listItem.getPicture()))
                .asBitmap()
                .into(holder.eventPic);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, EventActivity.class);
                intent.putExtra("id", listItem.getId());
                intent.putExtra("title", listItem.getTitle());
                intent.putExtra("pic", listItem.getPicture());
                mContext.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {

        return listItemList.size();
    }


}

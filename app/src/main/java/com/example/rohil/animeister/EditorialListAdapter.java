package com.example.rohil.animeister;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

/**
 * Created by Rohil on 12/10/2016.
 */

public class EditorialListAdapter extends ArrayAdapter<ListItem>{



    Context mContext;
    List<ListItem> listItemList;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://animeister-607c8.appspot.com");

    private static class ViewHolder {
        TextView articleTitle;
        ImageView articlePicture;
    }



    public EditorialListAdapter(Context context, List<ListItem> objects) {
        super(context, R.layout.editorial_list_item,objects);
        mContext = context;
        listItemList = objects;
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ListItem listItem = listItemList.get(position);
        final ViewHolder viewHolder;

        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.editorial_list_item, parent, false);
            viewHolder.articleTitle = (TextView) convertView.findViewById(R.id.articleTitle);
            viewHolder.articlePicture = (ImageView) convertView.findViewById(R.id.articlePicture);
            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data from the data object via the viewHolder object
        // into the template view.
        viewHolder.articleTitle.setText(listItem.getTitle());
        Glide.with(mContext)
                .using(new FirebaseImageLoader())
                .load(storageRef.child("editorials").child(listItem.getId()).child(listItem.getPicture()))
                .asBitmap()
                .into(new ImageViewTarget<Bitmap>(viewHolder.articlePicture) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                        circularBitmapDrawable.setCornerRadius((float) 100);
                        viewHolder.articlePicture.setImageDrawable(circularBitmapDrawable);
                    }
                });
        // Return the completed view to render on screen
        return convertView;



    }


}

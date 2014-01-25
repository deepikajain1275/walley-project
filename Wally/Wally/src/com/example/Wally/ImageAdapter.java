package com.example.Wally;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

class ImageAdapter extends BaseAdapter {
// take from internet
    private Context mContext; 
    private ArrayList<LoadedImage> photos = new ArrayList<LoadedImage>();

    public ImageAdapter(Context context) { 
        mContext = context; 
    } 

    public void addPhoto(LoadedImage photo) { 
        photos.add(photo); 
    } 

    public int getCount() { 
        return photos.size(); 
    } 

    public Object getItem(int position) { 
        return photos.get(position); 
    } 

    public long getItemId(int position) { 
        return position; 
    } 

    public View getView(int position, View convertView, ViewGroup parent) { 
        final ImageView imageView; 
        if (convertView == null) { 
            imageView = new ImageView(mContext); 
            
            
        } else { 
            imageView = (ImageView) convertView; 
        } 
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setPadding(8, 8, 8, 8);
        imageView.setImageBitmap(photos.get(position).getBitmap());
        return imageView; 
    } 
}

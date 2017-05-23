// Casey Scott
// Java 1 - 1704
// Adapter.Java
package com.fullsail.caseyscott.scottcasey_ce09;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.github.snowdream.android.widget.SmartImageView;


import java.util.ArrayList;


class Adapter extends BaseAdapter {

    //Member variables
    private final Context mContext;
    private final ArrayList<Books> mBooksList;
    private final int mLayout;


    //Class constructor
    Adapter(Context mContext, ArrayList<Books> mBooksList) {
        this.mContext = mContext;
        this.mBooksList = mBooksList;
        this.mLayout = R.layout.grid_view;
    }

    private static class  ViewHolder{

        //Views in the item layout

        final TextView bookTitle;
        final SmartImageView bookImage;

        //Constructor that sets the views up
        ViewHolder(View v){

            bookTitle = (TextView) v.findViewById(R.id.bookTitle);
            bookImage = (SmartImageView) v.findViewById(R.id.imageView);
        }
    }

    @Override
    public int getCount(){

        if(mBooksList != null){
            return mBooksList.size();
        }else{
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {

        if(mBooksList != null && position < mBooksList.size() && position >= 0){
            return mBooksList.get(position);
        }else {
            return null;
        }
    }

    private static final int ID_CONSTANT = 0x01000000;
    @Override
    public long getItemId(int position) {
        return ID_CONSTANT + position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        //Variable for the view holder and used to recycle views
        ViewHolder holder;

        //Check for recycled view first
        if(convertView == null){
            //IF no recycled view, create new using inflater
            convertView = LayoutInflater.from(mContext).inflate(mLayout, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);

        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        //Get the object for this position
        Books item = (Books)getItem(position);

        //Setup view as needed and return it.
        holder.bookTitle.setText(item.getName());



        holder.bookImage.setImageUrl(item.getImage(), holder.bookImage.getClipBounds());


        return convertView;
    }
}

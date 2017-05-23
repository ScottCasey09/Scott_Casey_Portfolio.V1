// Casey Scott
// AID - 1705
// ViewAdapter.java

package com.fullsail.caseyscott.scottcasey_04;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

class ViewAdapter extends BaseAdapter{

    //Member variables
    private final Context mContext;
    private final ArrayList<Reviews> mReviewsList;
    private final int mLayout;

    //Class constructor
    ViewAdapter(Context mContext, ArrayList<Reviews> mReviewsList) {
        this.mContext = mContext;
        this.mReviewsList = mReviewsList;
        this.mLayout = R.layout.custom_adapter_layout;
    }

    private static class  ViewHolder{

        //Views in the item layout
        final ImageView avatar;
        final TextView review;
        final TextView date;
        final TextView title;
        final RatingBar ratingBar;

        //Constructor that sets the views up
        ViewHolder(View v){

            avatar = (ImageView) v.findViewById(R.id.avatar);
            review = (TextView) v.findViewById(R.id.review_body);
            date = (TextView) v.findViewById(R.id.date);
            title = (TextView) v.findViewById(R.id.title_review);
            ratingBar = (RatingBar) v.findViewById(R.id.ratingBar);
        }
    }

    @Override
    public int getCount(){

        if(mReviewsList != null){
            return mReviewsList.size();
        }else{
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {

        if(mReviewsList != null && position < mReviewsList.size() && position >= 0){
            return mReviewsList.get(position);
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
        Reviews item = (Reviews) getItem(position);

        //Setup view as needed and return it.
        holder.review.setText(item.getReview());
        holder.title.setText(item.getTitle());
        holder.avatar.setImageDrawable(item.getAvatar());
        holder.ratingBar.setRating(item.getRating().getRating());
        holder.date.setText(item.getDate());

        return convertView;
    }
}

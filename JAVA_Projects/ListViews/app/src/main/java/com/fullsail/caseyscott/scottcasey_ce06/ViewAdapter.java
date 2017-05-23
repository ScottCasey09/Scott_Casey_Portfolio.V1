// Casey Scott
// JAV1 - 1704
// ViewAdapter.java
package com.fullsail.caseyscott.scottcasey_ce06;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

class ViewAdapter extends BaseAdapter{

    //Member variables
    private final Context mContext;
    private final ArrayList<Person> mPersonList;
    private final int mLayout;

    //Class constructor
    ViewAdapter(Context mContext, ArrayList<Person> mPersonList, int mLayout) {
        this.mContext = mContext;
        this.mPersonList = mPersonList;
        this.mLayout = mLayout;
    }

    private static class  ViewHolder{

        //Views in the item layout
        final TextView fullname;
        final TextView birthDate;
        final ImageView image;

        //Constructor that sets the views up
        ViewHolder(View v){

            fullname = (TextView) v.findViewById(R.id.name_view);
            birthDate = (TextView) v.findViewById(R.id.date_view);
            image = (ImageView) v.findViewById(R.id.image_view);
        }
    }

    @Override
    public int getCount(){

        if(mPersonList != null){
            return mPersonList.size();
        }else{
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {

        if(mPersonList != null && position < mPersonList.size() && position >= 0){
            return mPersonList.get(position);
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
        Person item = (Person)getItem(position);

        //Setup view as needed and return it.
        holder.fullname.setTag(item.toString());
        holder.birthDate.setText(item.getBirthdate());
        holder.image.setImageDrawable(item.getPicture());
        return convertView;
    }
}

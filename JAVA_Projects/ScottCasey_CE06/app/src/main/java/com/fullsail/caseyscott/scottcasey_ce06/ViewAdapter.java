// Casey Scott
// AID - 1705
// ViewAdapter.JAVA

package com.fullsail.caseyscott.scottcasey_ce06;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;

public class ViewAdapter extends BaseAdapter{

    //Member variables
    private final Context mContext;
    private final ArrayList<String> mContactsList;
    private final int mLayout;

    //Class constructor
   public ViewAdapter(Context mContext, ArrayList<String> mContactsList) {
        this.mContext = mContext;
        this.mContactsList = mContactsList;
        this.mLayout = R.layout.contacts_list_layout;
    }

    private static class  ViewHolder{

        //Views in the item layout
        final TextView fullname;
        final ImageView image;
        final RelativeLayout list_layout;

        //Constructor that sets the views up
        ViewHolder(View v){

            fullname = (TextView) v.findViewById(R.id.fullName_list);
            image = (ImageView) v.findViewById(R.id.icon);
            list_layout = (RelativeLayout) v.findViewById(R.id.list_items_layout);
        }
    }

    @Override
    public int getCount(){

        if(mContactsList != null){
            return mContactsList.size();
        }else{
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {

        if(mContactsList != null && position < mContactsList.size() && position >= 0){
            return mContactsList.get(position);
        }else {
            return null;
        }
    }

    private static final int ID_CONSTANT = 0x01000000;
    @Override
    public long getItemId(int position) {
        return ID_CONSTANT + position;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
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
        Contact item =  (Contact)getItem(position);

        final int[] colors = new int[]{
                R.color.colorLightPurple,
                R.color.colorLightestPurple,
        };
        int color;
        if(position % 2 == 0){
            color=0;
        }else{
            color=1;
        }


        //Setup view as needed and return it.
        holder.fullname.setText(item.getFirstName() + " "+ item.getLastName());
        holder.image.setImageResource(R.mipmap.ic_launcher_round);
        holder.list_layout.setBackgroundColor(convertView.getResources().getColor(colors[color], null));
        return convertView;
    }
}


// Casey Scott
// AID - 1705
// ViewAdapter.JAVA

package com.fullsail.caseyscott.scottcasey_ce05;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
        this.mLayout = R.layout.list_layout;
    }

    private static class  ViewHolder{

        //Views in the item layout
        final TextView fullname;
        final TextView option;

        //Constructor that sets the views up
        ViewHolder(View v){

            fullname = (TextView) v.findViewById(R.id.name);
            option = (TextView) v.findViewById(R.id.option);
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

        //Setup view as needed and return it.
        if (getItem(position).getClass().equals(Student.class)){
            Student item = (Student) getItem(position);
            holder.fullname.setText(item.getFirstName() + " " + item.getLastName());
            holder.option.setText(item.getId());
        }else if (getItem(position).getClass().equals(Teacher.class)){
            Teacher item = (Teacher) getItem(position);
            holder.fullname.setText(item.getFirstName() + " " + item.getLastName());
            holder.option.setText(item.gettClass());
        }else if (getItem(position).getClass().equals(Administrator.class)){
            Administrator item = (Administrator) getItem(position);
            holder.fullname.setText(item.getFirstName() + " " + item.getLastName());
            holder.option.setText(item.getProgram());
        }
        return convertView;
    }
}


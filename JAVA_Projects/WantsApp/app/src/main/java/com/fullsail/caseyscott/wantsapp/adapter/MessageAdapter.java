// Casey Scott
// PAPVI - 1710
// MessageAdapter.java
package com.fullsail.caseyscott.wantsapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fullsail.caseyscott.wantsapp.R;
import com.fullsail.caseyscott.wantsapp.objects.MessagePost;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;


public class MessageAdapter extends BaseAdapter {

    private final Context context;
    private final ArrayList<MessagePost> allMessages;
    private final String uID;
    private static final int ID_CONSTANT = 0x00001;

    public MessageAdapter(Context context, ArrayList<MessagePost> posts, String uID){
        this.context = context;
        this.allMessages = posts;
        this.uID = uID;
    }
    private class ViewHolder{
        final TextView message_TextView;
        final TextView dateTime_TextView;

        ViewHolder(View v){
            this.message_TextView = (TextView) v.findViewById(R.id.message_TextView);
            this.dateTime_TextView = (TextView) v.findViewById(R.id.dateTime);
        }
    }

    @Override
    public int getCount() {
        return allMessages.size();
    }

    @Override
    public Object getItem(int position) {
        return allMessages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position+ID_CONSTANT;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final MessagePost post = allMessages.get(position);
        try {
            if (convertView == null) {
                if(Objects.equals(post.getUsersId(), uID)){
                    convertView = LayoutInflater.from(context).inflate(R.layout.users_message_cell, parent, false);
                    holder = new ViewHolder(convertView);
                    holder.message_TextView.setBackgroundColor(context.getColor(R.color.background_light));
                }else{
                    convertView = LayoutInflater.from(context).inflate(R.layout.contacts_message_cell, parent, false);
                    holder = new ViewHolder(convertView);
                    holder.message_TextView.setBackgroundColor(context.getColor(R.color.colorPrimaryDark));
                    holder.message_TextView.setTextColor(context.getColor(R.color.colorWhite));
                }
                convertView.setTag(holder);

            } else {

                holder = (ViewHolder) convertView.getTag();
            }

            //Make view
            holder.message_TextView.setText(post.getMessage());
            holder.message_TextView.setTag(post);
            holder.message_TextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.getId() == R.id.message_TextView){
                        if(holder.dateTime_TextView.getVisibility() == View.GONE) {
                            MessagePost post1 = (MessagePost) v.getTag();
                            Date date = new Date(post1.getTimeStamp());
                            SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a", Locale.US);
                            dateFormat.setTimeZone(TimeZone.getTimeZone("EST"));
                            String formattedDate = dateFormat.format(date);
                            holder.dateTime_TextView.setText(formattedDate);
                            holder.dateTime_TextView.setVisibility(View.VISIBLE);
                        }else {
                            holder.dateTime_TextView.setVisibility(View.GONE);
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }
}

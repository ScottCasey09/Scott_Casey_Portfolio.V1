// Casey Scott
// PAPVI - 1710
// GiftsItemsListAdapter.java

package com.fullsail.caseyscott.wantsapp.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fullsail.caseyscott.wantsapp.R;
import com.fullsail.caseyscott.wantsapp.objects.Items;

import java.util.ArrayList;


public class GiftsItemsListAdapter extends BaseAdapter {

    private static final int ID_CONSTANT = 0x000001;
    private final Context context;
    private final ArrayList<Items> allItems;

    public GiftsItemsListAdapter(Context context, ArrayList<Items> allItems) {
        this.context = context;
        this.allItems = allItems;
    }

    private class ViewHolder {
        final TextView title;
        final ImageView priority;
        final TextView price;
        final LinearLayout descLayout;
        final LinearLayout locatLayout;
        final TextView detailsPriority;
        final ImageButton closeButton;
        final LinearLayout mainDetailsLayout;

        ViewHolder(View v) {
            title = (TextView) v.findViewById(R.id.title_giftsCell);
            price = (TextView) v.findViewById(R.id.price_giftsCell);
            priority = (ImageView) v.findViewById(R.id.priority_color_view);
            descLayout = (LinearLayout) v.findViewById(R.id.extraInfo_layout);
            locatLayout = (LinearLayout) v.findViewById(R.id.locations_layout);
            detailsPriority = (TextView) v.findViewById(R.id.priority_level_TextView);
            closeButton = (ImageButton) v.findViewById(R.id.closeButton);
            mainDetailsLayout = (LinearLayout) v.findViewById(R.id.itemDetailsView);

        }
    }

    @Override
    public int getCount() {
        return allItems.size();
    }

    @Override
    public Object getItem(int position) {
        return allItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return ID_CONSTANT + position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        try {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.gift_list_cell_for_contact, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);

            } else {

                holder = (ViewHolder) convertView.getTag();
            }

            Items item = allItems.get(position);

            holder.priority.setImageDrawable(getColorForPriority(item.getPriority_level()));
            ;
            holder.title.setText(item.getName());
            String price;
            if (!item.getPrice().equals(context.getString(R.string.unknown))) {
                price = context.getString(R.string.price)+ " " + context.getString(R.string._$) + item.getPrice();
            } else {
                price = context.getString(R.string.price) +" "+ item.getPrice();
            }
            holder.price.setText(price);
            holder.detailsPriority.setText(getTextForPriorityLevel(item.getPriority_level()));
            TextView textView = new TextView(context);
            textView.setText(item.getLocations());
            textView.setPadding(40, 2, 0, 2);
            textView.setTextSize(15);
            holder.locatLayout.addView(textView);
            textView = new TextView(context);
            textView.setPadding(40, 2, 0, 2);
            textView.setTextSize(15);
            textView.setText(item.getDescription());
            holder.descLayout.addView(textView);
            holder.closeButton.setFocusable(true);
            holder.closeButton.setFocusableInTouchMode(true);
            holder.closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.mainDetailsLayout.setVisibility(View.GONE);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }


    private Drawable getColorForPriority(int i) {

        switch (i) {
            case 1:
                return context.getResources().getDrawable(R.mipmap.blue_dot, context.getTheme());
            case 2:
                return context.getResources().getDrawable(R.mipmap.green_dot, context.getTheme());
            case 3:
                return context.getResources().getDrawable(R.mipmap.yellow_dot, context.getTheme());
            case 4:
                return context.getResources().getDrawable(R.mipmap.orange_dot, context.getTheme());
            case 5:
                return context.getResources().getDrawable(R.mipmap.red_dot, context.getTheme());
        }
        return context.getResources().getDrawable(R.mipmap.blue_dot, context.getTheme());
    }

    private String getTextForPriorityLevel(int i) {

        switch (i) {
            case 1:
                return context.getString(R.string.kinda_want);
            case 2:
                return context.getString(R.string.kinda_want_plus);
            case 3:
                return context.getString(R.string.want);
            case 4:
                return context.getString(R.string.want_plus);
            case 5:
                return context.getString(R.string.really_want);
        }
        return context.getString(R.string.want);
    }
}

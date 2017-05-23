// Casey Scott
// Java 1 - 1704
// MemberAdapter.Java
package com.fullsail.android.ScottCasey_CE12;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fullsail.android.ScottCasey_CE12.object.Member;

class MembersAdapter extends BaseAdapter {
	
	private static final long ID_CONSTANT = 0x01000000;//0x010101010L
	
	private final Context mContext;
	private final ArrayList<Member> mMembers;

	MembersAdapter(Context _context, ArrayList<Member> _members) {
		this.mContext = _context;
		this.mMembers = _members;
	}

	@Override
	public int getCount() {
		return mMembers.size();
	}

	@Override
	public Object getItem(int _position) {
			return mMembers.get(_position);
	}

	@Override
	public long getItemId(int _position) {
		return ID_CONSTANT + _position;
	}

	@Override
	public View getView(int _position, View _convertView, ViewGroup _parent) {

		//Variable for the view holder and used to recycle views
		ViewHolder holder;

		try {
			if (_convertView == null) {
				_convertView = LayoutInflater.from(mContext).inflate(R.layout.listview_layout, _parent, false);
				holder = new ViewHolder(_convertView);
				_convertView.setTag(holder);

			} else {

				holder = (ViewHolder) _convertView.getTag();
			}

			Member member = mMembers.get(_position);

			holder.nameView.setTag(member.toString());
			holder.nameView.setText(member.getName());
			holder.partyView.setText(member.getParty());

		}catch (Exception e){
			e.printStackTrace();
		}

		return _convertView;
	}
	private static class  ViewHolder{

		//Views in the item layout
		final TextView nameView;
		final TextView partyView;

		//Constructor that sets the views up
		ViewHolder(View v){

			nameView = (TextView) v.findViewById(R.id.name);
			partyView = (TextView) v.findViewById(R.id.party);
		}
	}

}
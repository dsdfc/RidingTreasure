package com.example.bike.music;

import java.util.ArrayList;

import com.example.bike.R;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MusicAadapter extends BaseAdapter {

	ArrayList<MusicModel> mMusicList;
	Context mContext;

	public MusicAadapter(ArrayList<MusicModel> alist, Context mContext) {
		this.mMusicList = alist;
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		return mMusicList.size();
	}

	@Override
	public Object getItem(int position) {
		return mMusicList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		viewhodler viewhodler;
		if (convertView == null) {
			viewhodler = new viewhodler();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_music_item, null);

			viewhodler.text_gq = (TextView) convertView.findViewById(R.id.text_gq);
			viewhodler.text_gs = (TextView) convertView.findViewById(R.id.text_gs);
			viewhodler.img = (ImageView) convertView.findViewById(R.id.img11);

			convertView.setTag(viewhodler);
		} else {
			viewhodler = (viewhodler) convertView.getTag();
		}

		viewhodler.text_gq.setText("" + mMusicList.get(position).music_name);
		viewhodler.text_gs.setText("" + mMusicList.get(position).singer);
		viewhodler.img.setImageResource(R.drawable.pic_11);

		return convertView;
	}

	static class viewhodler {
		TextView text_gq, text_gs;
		ImageView img;
	}

}

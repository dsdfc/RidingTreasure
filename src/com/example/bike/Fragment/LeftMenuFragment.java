package com.example.bike.Fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.bike.R;
import com.example.bike.music.MusicAadapter;
import com.example.bike.music.MusicModel;
import com.example.bike.music.MusicService;
import com.example.bike.music.Saomiao;

/**
 * 侧滑音乐界面
 * 
 * @author Administrator
 * 
 */
public class LeftMenuFragment extends BaseFragment {

	private ListView mMusicListView;
	private MusicAadapter mMusicAadapter;
	private Saomiao mSaomiao;
	private ArrayList<MusicModel> mMusicList;
	private Map<String, Integer> musicNameMap;

	
	LocalBroadcastManager lbm;
	
	@Override
	public View initViews() {
		View view = View.inflate(mActivity, R.layout.fragment_left_menu, null);
		mMusicListView = (ListView) view.findViewById(R.id.lv_music_list);

		mMusicList = new ArrayList<MusicModel>();
		mSaomiao = Saomiao.getInstance();
		mMusicList = mSaomiao.query(mActivity);
		musicNameMap = mSaomiao.getMusicNameIndex();
		
		mMusicAadapter = new MusicAadapter(mMusicList, mActivity);
		mMusicListView.setAdapter(mMusicAadapter);

		return view;
	}

	@Override
	public void initData() {

		// 启动服务
		Intent mintent = new Intent();
		mintent.setClass(mActivity, MusicService.class);
		mActivity.startService(mintent);
		
		
		mMusicListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(mActivity,"you click " + mMusicList.get(position).getMusic_name(),
						Toast.LENGTH_SHORT).show();
				
				Intent intent = new Intent();
				intent.setAction("ACTION_INDEX");
				intent.putExtra("index", position);
				
//				mActivity.sendBroadcast(intent);
				// 发送本地广播
				lbm = LocalBroadcastManager.getInstance(mActivity);
				lbm.sendBroadcast(intent);
			}
		});
	}
	
	public String settime(int time) {
		int fen = time / 60000;
		int miao = time / 1000 % 60;
		return fen + ":" + miao;
	}
}

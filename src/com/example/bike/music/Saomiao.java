package com.example.bike.music;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

/**
 * 扫描手机中的音乐列表
 * 
 * @author Administrator
 * 
 */
public class Saomiao {
	private static Saomiao instance;
	private Saomiao() {}
	public static synchronized Saomiao getInstance() {
		if (instance == null) {
			instance = new Saomiao();
		}
		return instance;
	}
	
	private ArrayList<MusicModel> arraylist = null;
	private Map<String, Integer> musicNameMap = null;
	
	public ArrayList<MusicModel> query(Context mcontext) {
		// 扫描->游标
		Cursor cursor = mcontext.getContentResolver().query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
				null);

		if (cursor != null) {
			arraylist = new ArrayList<MusicModel>();
			musicNameMap = new HashMap<String, Integer>();

			while (cursor.moveToNext()) {

				MusicModel musicModel;
				musicModel = new MusicModel();

				String music_name = cursor.getString(cursor
						.getColumnIndex(MediaStore.Audio.Media.TITLE));
				String singer = cursor.getString(cursor
						.getColumnIndex(MediaStore.Audio.Media.ARTIST));
				String path = cursor.getString(cursor
						.getColumnIndex(MediaStore.Audio.Media.DATA));
				long duration = cursor.getLong(cursor
						.getColumnIndex(MediaStore.Audio.Media.DURATION)); // 时长

				// System.out.println(""+music_name);
				// System.out.println(""+singer);
				// System.out.println(""+path);
				musicModel.setMusic_name(music_name);
				musicModel.setSinger(singer);
				musicModel.setPath(path);
				musicModel.setDuration(duration);
				if (musicModel.getDuration() > 1000 * 60) {
					arraylist.add(musicModel);
//					Log.d("dsdfc", "索引：" + arraylist.indexOf(musicModel));
					musicNameMap.put(musicModel.getMusic_name(), arraylist.indexOf(musicModel));
				}
			}
		}
		return arraylist;
	}
	
	/**
	 *  获取 (音乐名，位置) 映射
	 * @return
	 */
	public Map<String, Integer> getMusicNameIndex() {
		return musicNameMap;
	}

	/**
	 * 读取歌词
	 * 
	 * @param path
	 * @return
	 */
	public static ArrayList<LrcModel> readlrc(String path) {

		ArrayList<LrcModel> alist = new ArrayList<LrcModel>();
		File f = new File(path.replace(".mp3", ".lrc"));

		try {
			FileInputStream fs = new FileInputStream(f);

			InputStreamReader inputstreamreader = new InputStreamReader(fs,
					"utf-8");

			BufferedReader br = new BufferedReader(inputstreamreader);
			String s = "";

			while (null != (s = br.readLine())) {

				if (!TextUtils.isEmpty(s)) {
					LrcModel lrcmodel = new LrcModel();

					String lylrc = s.replace("[", "");

					String data_ly[] = lylrc.split("]");
					if (data_ly.length > 1) {

						String time = data_ly[0];
						lrcmodel.setTime(lrcdata(time));
						String lrc = data_ly[1];

						lrcmodel.setIrc(lrc);
						// System.out.println("..............."+lrcmodel.getIrc());
						alist.add(lrcmodel);
						// System.out.println(".................."+alist.get(0).getIrc());
					}
				}

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return alist;

	}

	public static int lrcdata(String time) {
		time = time.replace(":", "#");
		time = time.replace(".", "#");

		String mTime[] = time.split("#");

		int mtime = Integer.parseInt(mTime[0]);
		int stime = Integer.parseInt(mTime[1]);
		int mitime = Integer.parseInt(mTime[2]);

		int ctime = (mtime * 60 + stime) * 1000 + mitime * 10;

		return ctime;

	}

	/*
	 * 格式化时间，把毫秒转换成分：秒格式
	 */
	public static String formaTime(long time) {
		String min = time / (1000 * 60) + "";
		String sec = time % (1000 * 60) + "";
		if (min.length() < 2) {
			min = "0" + time / (1000 * 60) + "";
		} else {
			min = time / (1000 * 60) + "";
		}
		if (sec.length() == 4) {
			sec = "0" + (time % (1000 * 60)) + "";
		} else if (sec.length() == 3) {
			sec = "00" + (time % (1000 * 60)) + "";
		} else if (sec.length() == 2) {
			sec = "000" + (time % (1000 * 60)) + "";
		} else if (sec.length() == 1) {
			sec = "0000" + (time % (1000 * 60)) + "";
		}
		return min + ":" + sec.trim().substring(0, 2);
	}
}

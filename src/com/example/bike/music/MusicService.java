package com.example.bike.music;

import java.io.IOException;
import java.util.ArrayList;

import android.R.integer;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

public class MusicService extends Service {

	public final int LIST_CIRCLE = 1;
	public final int SINGLE_CIRCLE = 2;
	public final int RANDOM_PLAY = 3;

	MediaPlayer media;
	ArrayList<MusicModel> alist;
	Saomiao scan;
	Mybroadmusic myBM;
	int num = 0;
	int n = 0;
	int check = 1;
	Handler handler;
	Handler lrchandler;
	ArrayList<LrcModel> lrclist;

	LocalBroadcastManager lbm;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {

		Toast.makeText(getApplicationContext(), "MusicService onCreate",
				Toast.LENGTH_SHORT).show();
		super.onCreate();

		alist = new ArrayList<MusicModel>();
		lrclist = new ArrayList<LrcModel>();

		media = new MediaPlayer();
		scan = Saomiao.getInstance();
		alist = scan.query(this);
		myBM = new Mybroadmusic();
		handler = new Handler();
		lrchandler = new Handler();
		zhuce();

	}

	public class Mybroadmusic extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			// Toast.makeText(getApplicationContext(), "broadcast receiver",
			// Toast.LENGTH_SHORT).show();
			if (intent.getAction().equals("ACTION_INDEX")) {
				num = intent.getIntExtra("index", 0);
				String path = alist.get(num).getPath();
				setdata(path);
				panduan();
				play();
			} else if (intent.getAction().equals("ACTION_ISPLAY")) {
				boolean isplay = intent.getBooleanExtra("isplay", false);
				if (isplay == false) {
					Toast.makeText(getApplicationContext(), "暂停播放",
							Toast.LENGTH_SHORT).show();
					media.pause();
					handler.removeCallbacks(mRunnable);
					panduan();
				} else {
					Toast.makeText(getApplicationContext(), "继续播放",
							Toast.LENGTH_SHORT).show();
					media.start();
					handler.postDelayed(mRunnable, 1000);
					if (lrclist.size() != 0) {
						lrchandler.postDelayed(lrcRunnable, 100);
					}
				}
			} else if (intent.getAction().equals("ACTION_NEXT")) {
				// 上一曲，下一曲，0上一曲，1下一曲
				int next = intent.getIntExtra("next", 0);
				if (next == 0) { // 上一曲
					if (check == RANDOM_PLAY) {
						n = (int) (Math.random() * alist.size());
						if (n == num) {
							n = (int) (Math.random() * alist.size());
						}
						String path = alist.get(n).getPath();
						setdata(path);
						panduan();
						play();
					} else {
						up();
					}
				} else {
					if (check == RANDOM_PLAY) {
						n = (int) (Math.random() * alist.size());
						if (n == num) {
							n = (int) (Math.random() * alist.size());
						}
						String path = alist.get(n).getPath();
						setdata(path);
						panduan();
						play();
					} else {
						down();
					}
				}
			} else if (intent.getAction().equals("ACTION_SEEKBAR")) {
				int progress = intent.getIntExtra("seekbar", 0);
				media.seekTo(progress);
			} else if (intent.getAction().equals("ACTION_STYLE")) {
				check = intent.getIntExtra("check", 0);

				if (check == LIST_CIRCLE) {
					nextmusic();
				}
				if (check == SINGLE_CIRCLE) {

					danqu();
				}
				if (check == RANDOM_PLAY) {
					suiji();
				}
			}

		}
	}

	public void panduan() {
		if (lrclist.size() == 0) {
			lrchandler.removeCallbacks(lrcRunnable);
		}
	}

	public void zhuce() {
		lbm = LocalBroadcastManager.getInstance(this);
		IntentFilter mFilter = new IntentFilter();

		mFilter.addAction("ACTION_INDEX");
		mFilter.addAction("ACTION_ISPLAY");
		mFilter.addAction("ACTION_NEXT");
		mFilter.addAction("ACTION_SEEKBAR");
		mFilter.addAction("ACTION_STYLE");

		// registerReceiver(myBM, mFilter);
		// 注册接收器以及过滤规则
		lbm.registerReceiver(myBM, mFilter);
	}

	public void setdata(String path) {

		media.reset();

		try {
			media.setDataSource(path);
			media.prepare();

			lrclist = scan.readlrc(path);

			maxtime();
			nowmusic();

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void play() {

		media.start();
		// System.out.println("=================" + lrclist.size());
		if (lrclist.size() != 0) {
			lrchandler.postDelayed(lrcRunnable, 100);
		}
		handler.postDelayed(mRunnable, 1000);
	}

	public void nextmusic() {
		Toast.makeText(getApplicationContext(), "列表循环", Toast.LENGTH_SHORT)
				.show();
		media.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				num++;
				if (num > alist.size() - 1) {
					num = 0;
				}
				String path = alist.get(num).getPath();
				setdata(path);
				panduan();
				play();
			}
		});

	}

	public void suiji() {
		Toast.makeText(getApplicationContext(), "随机播放", Toast.LENGTH_SHORT)
				.show();
		n = (int) (Math.random() * alist.size());
		while (n == num) {
			n = (int) (Math.random() * alist.size());
		}
		media.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				String path = alist.get(n).getPath();
				setdata(path);
				panduan();
				play();
			}
		});
	}

	public void danqu() {
		Toast.makeText(getApplicationContext(), "单曲循环", Toast.LENGTH_SHORT)
				.show();
		media.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				String path = alist.get(num).getPath();
				setdata(path);
				panduan();
				play();
			}
		});
	}

	public void up() {
		num--;
		if (num < 0) {
			num = alist.size() - 1;
		}
		String path = alist.get(num).getPath();
		setdata(path);
		panduan();
		play();
	}

	public void down() {
		num++;
		if (num > alist.size() - 1) {
			num = 0;
		}
		String path = alist.get(num).getPath();
		setdata(path);
		panduan();
		play();
	}

	/**
	 * 给activity发广播告知歌曲的最长时间
	 */
	public void maxtime() {

		Intent intent = new Intent();
		intent.setAction("ACTION_MAXTIME");
		intent.putExtra("maxtime", media.getDuration());
		lbm.sendBroadcast(intent);
	}

	public void nowtime() {
		Intent intent = new Intent();
		intent.setAction("ACTION_NOWTIME");
		intent.putExtra("nowtime", media.getCurrentPosition());
		lbm.sendBroadcast(intent);
	}

	public void nowmusic() {
		Intent intent = new Intent();
		intent.setAction("ACTION_NOWMUSIC");
		intent.putExtra("nowmusic", alist.get(num).getMusic_name());
		lbm.sendBroadcast(intent);

	}

	public int lrcindex() {
		int nowtime = 0, alltime = 0, index = 0;
		if (media.isPlaying()) {
			nowtime = media.getCurrentPosition();
			alltime = media.getDuration();
		}
		if (nowtime < alltime) {
			for (int i = 0; i < lrclist.size(); i++) {
				if (i < lrclist.size() - 1) {
					if (nowtime < lrclist.get(i).getTime() && i == 0) {
						index = i;
					}
					if (nowtime > lrclist.get(i).getTime()
							&& nowtime < lrclist.get(i + 1).getTime()) {
						index = i;
					}

				}
				if (i == lrclist.size() - 1
						&& nowtime > lrclist.get(i).getTime()) {
					index = i;
				}
			}
		}

		return index;
	}

	Runnable lrcRunnable = new Runnable() {

		@Override
		public void run() {

			String lrc = lrclist.get(lrcindex()).getIrc();

			System.out.println("[[[[[[[[[[[[[[[[[[[" + lrc);
			Intent intent = new Intent();
			intent.setAction("ACTION_LRC");
			intent.putExtra("geci", lrc);
			sendBroadcast(intent);

			lrchandler.postDelayed(lrcRunnable, 100);
		}
	};

	// 每隔一秒更新播放进度
	Runnable mRunnable = new Runnable() {

		@Override
		public void run() {

			nowtime();

			handler.postDelayed(mRunnable, 1000);
		}
	};

	public void onDestroy() {
		super.onDestroy();
		lbm.unregisterReceiver(myBM);
	};

}

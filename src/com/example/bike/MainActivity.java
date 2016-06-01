package com.example.bike;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.example.bike.Fragment.MainFragment;
import com.example.bike.Fragment.LeftMenuFragment;
import com.example.bike.Fragment.RightMenuFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;


public class MainActivity extends SlidingFragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		setBehindContentView(R.layout.left_menu);// 璁剧疆渚ц竟鏍�
		SlidingMenu slidingMenu = getSlidingMenu();// 鑾峰彇渚ц竟鏍忓璞�
		slidingMenu.setMode(SlidingMenu.LEFT_RIGHT);// 开启左右布局
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);// 璁剧疆鍏ㄥ睆瑙︽懜
		slidingMenu.setBehindOffset(200);// 璁剧疆棰勭暀灞忓箷鐨勫搴
		slidingMenu.setFadeEnabled(true);// 设置滑动时菜单的是否淡入淡出
		slidingMenu.setBehindScrollScale(0.333f);// 设置滑动时拖拽效果
		slidingMenu.setFadeDegree(0.35f);// 设置淡入淡出的比例
		slidingMenu.setSecondaryMenu(R.layout.right_menu);
		
		initFragment();
	}

	
	private void initFragment() {
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();
		
		transaction.replace(R.id.fl_left_menu, new LeftMenuFragment());
		transaction.replace(R.id.fl_content, new MainFragment());
		transaction.replace(R.id.fl_right_menu, new RightMenuFragment());

		transaction.commit();
	}       
}



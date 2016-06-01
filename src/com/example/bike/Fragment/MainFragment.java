package com.example.bike.Fragment;

import java.util.ArrayList;

import com.example.bike.R;
import com.example.bike.Frag.sec.QiXingFragment;
import com.example.bike.Frag.sec.SheZhiFragment;
import com.example.bike.Frag.sec.ZhouBianFragment;
import com.example.bike.Adapter.*;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

/**
 * 主页内容
 * 
 * @author Kevin
 * 
 */
public class MainFragment extends BaseFragment {

	private RadioGroup rgGroup;

	private ViewPager mViewPager;

	private ArrayList<Fragment> mPagerList;
	com.example.bike.Adapter.FragmentVpAdapter tAdapter;//封装的适配器

	@Override
	public View initViews() {
		View view = View.inflate(mActivity, R.layout.fragment_content, null);
		// rgGroup = (RadioGroup) view.findViewById(R.id.rg_group);

		mViewPager = (ViewPager) view.findViewById(R.id.vp_content);
		rgGroup = (RadioGroup) view.findViewById(R.id.rg_group);
        
		return view;
	}

	@Override
	public void initData() {
		rgGroup.check(R.id.rb_QiXing);// 默认勾�?�首�?

		// 初始�?5个子页面
		mPagerList = new ArrayList<Fragment>();
		// for (int i = 0; i < 5; i++) {
		// BasePager pager = new BasePager(mActivity);
		// mPagerList.add(pager);
		// }
		// mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		mPagerList.add(new QiXingFragment());

		mPagerList.add(new ZhouBianFragment());
		mPagerList.add(new SheZhiFragment());
//		封装的TeamAdapter适配器
        /*
         * getSupportFragmentManager
         * ViewPager mViewPager
         * ArrayList<Fragment> mPagerList
         * *
         */
	
		tAdapter = new FragmentVpAdapter(getActivity().getSupportFragmentManager(),
				mViewPager, mPagerList);
		tAdapter.setOnExtraPageChangeListener(new FragmentVpAdapter.OnExtraPageChangeListener() {
			@Override
			public void onExtraPageSelected(int i) {
				System.out.println("Extra...i: " + i);
			}
		});
//
//		mViewPager.setAdapter(new ContentAdapter(getFragmentManager()));
		mViewPager.setAdapter(tAdapter);
		// 监听RadioGroup的�?�择事件
		rgGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rb_QiXing:
					mViewPager.setCurrentItem(0, false);// 去掉切换页面的动�?
					break;
				case R.id.rb_ZhouBian:
					mViewPager.setCurrentItem(1, false);// 设置当前页面
					break;
				case R.id.rb_SheZhi:
					mViewPager.setCurrentItem(2, false);// 设置当前页面
					break;

				default:
					break;
				}
			}
		});

	}

	
	@Override
	public void setMenuVisibility(boolean menuVisible) {

		super.setMenuVisibility(menuVisible);
		if (this.getView() != null) {
			this.getView()
					.setVisibility(menuVisible ? View.VISIBLE : View.GONE);
		}
	}

}

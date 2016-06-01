package com.example.bike.Adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;

public class FragmentVpAdapter extends PagerAdapter implements OnPageChangeListener {
	private List<Fragment> fragments; // ÿ��Fragment��Ӧһ��Page
	private FragmentManager fragmentManager;
	private ViewPager viewPager; // viewPager����
	private int currentPageIndex = 0; // ��ǰpage�����л�֮ǰ��

	private OnExtraPageChangeListener onExtraPageChangeListener; // ViewPager�л�ҳ��ʱ�Ķ��⹦����ӽӿ�

	public FragmentVpAdapter(FragmentManager fragmentManager, ViewPager viewPager,
			List<Fragment> fragments) {
		this.fragments = fragments;
		this.fragmentManager = fragmentManager;
		this.viewPager = viewPager;
		this.viewPager.setAdapter(this);
		this.viewPager.setOnPageChangeListener(this);
	}

	@Override
	public int getCount() {
		return fragments.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object o) {
		return view == o;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(fragments.get(position).getView()); // �Ƴ�ViewPager����֮���page����
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		Fragment fragment = fragments.get(position);
		if (!fragment.isAdded()) { // ���fragment��û��added
			FragmentTransaction ft = fragmentManager.beginTransaction();
			ft.add(fragment, fragment.getClass().getSimpleName());
			ft.commit();
			/**
			 * ����FragmentTransaction.commit()�����ύFragmentTransaction�����
			 * ���ڽ�̵����߳��У����첽�ķ�ʽ��ִ�С� �����Ҫ����ִ������ȴ��еĲ�������Ҫ�������������ֻ�������߳��е��ã���
			 * Ҫע����ǣ����еĻص�����ص���Ϊ��������������б�ִ����ɣ����Ҫ��ϸȷ����������ĵ���λ�á�
			 */
			fragmentManager.executePendingTransactions();
		}

		if (fragment.getView().getParent() == null) {
			container.addView(fragment.getView()); // ΪViewPager���Ӳ���
		}

		return fragment.getView();
	}

	/**
	 * ��ǰpage�����л�֮ǰ��
	 * 
	 * @return
	 */
	public int getCurrentPageIndex() {
		return currentPageIndex;
	}

	public OnExtraPageChangeListener getOnExtraPageChangeListener() {
		return onExtraPageChangeListener;
	}

	/**
	 * ����ҳ���л����⹦�ܼ�����
	 * 
	 * @param onExtraPageChangeListener
	 */
	public void setOnExtraPageChangeListener(
			OnExtraPageChangeListener onExtraPageChangeListener) {
		this.onExtraPageChangeListener = onExtraPageChangeListener;
	}

	@Override
	public void onPageScrolled(int i, float v, int i2) {
		if (null != onExtraPageChangeListener) { // ��������˶��⹦�ܽӿ�
			onExtraPageChangeListener.onExtraPageScrolled(i, v, i2);
		}
	}

	@Override
	public void onPageSelected(int i) {
		fragments.get(currentPageIndex).onPause(); // �����л�ǰFragment��onPause()
		// fragments.get(currentPageIndex).onStop(); // �����л�ǰFragment��onStop()
		if (fragments.get(i).isAdded()) {
			// fragments.get(i).onStart(); // �����л���Fragment��onStart()
			fragments.get(i).onResume(); // �����л���Fragment��onResume()
		}
		currentPageIndex = i;

		if (null != onExtraPageChangeListener) { // ��������˶��⹦�ܽӿ�
			onExtraPageChangeListener.onExtraPageSelected(i);
		}

	}

	@Override
	public void onPageScrollStateChanged(int i) {
		if (null != onExtraPageChangeListener) { // ��������˶��⹦�ܽӿ�
			onExtraPageChangeListener.onExtraPageScrollStateChanged(i);
		}
	}

	/**
	 * page�л����⹦�ܽӿ�
	 */
	public static class OnExtraPageChangeListener {
		public void onExtraPageScrolled(int i, float v, int i2) {
		}

		public void onExtraPageSelected(int i) {
		}

		public void onExtraPageScrollStateChanged(int i) {
		}
	}

}

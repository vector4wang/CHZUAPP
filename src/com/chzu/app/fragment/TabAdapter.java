package com.chzu.app.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * 
 * @author wangxingchao
 * @date 2015-3-22 下午3:53:41 
 * @Description: Tab适配器
 */
public class TabAdapter extends FragmentPagerAdapter{

	public static final String[] TITLES = new String[] {"蔚园要问","院部动态","通知公告","教科研信息"};
	
	public TabAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int arg0) {
		SonNewsFragment fragment = new SonNewsFragment(arg0);
		return fragment;
	}

	@Override
	public int getCount() {
		
		return TITLES.length;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return TITLES[position % TITLES.length];
	}
	
	
}

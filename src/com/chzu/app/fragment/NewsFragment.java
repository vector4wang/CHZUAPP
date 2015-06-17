package com.chzu.app.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chzu.app.activity.R;
import com.viewpagerindicator.TabPageIndicator;
/**
 * 
 * @author wangxingchao
 * @date 2015-3-22 下午3:07:05 
 * @Description: 校园新闻Fragment
 */
public class NewsFragment extends Fragment {
	private ViewPager mViewPager;//页卡内容 
	private TabPageIndicator mIndicator ;
	private FragmentPagerAdapter mAdapter ;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mViewPager.setAdapter(mAdapter);
		mIndicator.setViewPager(mViewPager, 0);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_school_news, null);
		initView(view);
		return view;
	}

	private void initView(View view) {
		mIndicator = (TabPageIndicator) view.findViewById(R.id.id_indicator);
		mViewPager = (ViewPager) view.findViewById(R.id.id_pager);
		mAdapter = new TabAdapter(getChildFragmentManager());
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}


	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}

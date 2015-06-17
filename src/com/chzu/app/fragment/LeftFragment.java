package com.chzu.app.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.chzu.app.activity.MainActivity;
import com.chzu.app.activity.R;
/**
 * 
 * @author wangxingchao
 * @date 2015-3-22 下午3:51:23 
 * @Description: 左侧菜单布局
 */
public class LeftFragment extends Fragment implements OnClickListener{
	private View mNewsView;//校园新闻
	private View mSchedualView;//课表查看
	private View mGradeView;//成绩
	private View mEmptyClassRoom;//空教室查询
	private View mContactUs;//教学评价
	private View mShareView;//分享
	
	Context context;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		Log.e("左侧菜单", "返回");
	}

	@Override
	public void onStart() {
		super.onStart();
		Log.e("左侧菜单", "开始");
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.layout_menu, null);
		findViews(view);
		context = getActivity();
		return view;
	}
	
	
	public void findViews(View view) {
		mNewsView = view.findViewById(R.id.id_school_news);
		mSchedualView = view.findViewById(R.id.id_view_schedule);
		mGradeView = view.findViewById(R.id.id_view_grade);
		mEmptyClassRoom = view.findViewById(R.id.id_empty_classroom);
		mContactUs = view.findViewById(R.id.id_contact_us);
		mShareView = view.findViewById(R.id.id_share);
		
		mNewsView.setOnClickListener(this);
		mSchedualView.setOnClickListener(this);
		mGradeView.setOnClickListener(this);
		mEmptyClassRoom.setOnClickListener(this);
		mContactUs.setOnClickListener(this);
		mShareView.setOnClickListener(this);
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		Fragment newContent = null;
		String title = null;
		
		switch (v.getId()) {
		case R.id.id_school_news: // 校园新闻
			newContent = new NewsFragment();
			title = getString(R.string.school_news);
			break;
		case R.id.id_view_schedule://课表查询
			newContent = new ScheduleFragment();
			
			title = getString(R.string.view_schedule);
			break;
		case R.id.id_view_grade: //成绩查看
			newContent = new GradeFragment();
			title = getString(R.string.view_grade);
			break;
		case R.id.id_empty_classroom: // 空教室查询
			newContent = new EmptyClassRoomFragment();
			title = getString(R.string.empty_classroom);
			break;
		case R.id.id_contact_us: // 联系我们
			newContent = new ContactUsFragment();
			title = getString(R.string.contast_us);
			break;
		case R.id.id_share: // 分享
			Intent intent=new Intent(Intent.ACTION_SEND);  
            intent.setType("text/plain");  
            intent.putExtra(Intent.EXTRA_SUBJECT, "分享");  
            intent.putExtra(Intent.EXTRA_TEXT, "我是滁院'爱拍拍',一起来下载使用吧!http://www.baidu.com");  
            startActivity(Intent.createChooser(intent, "分享到"));  
			break;
		default:
			break;
		}
		if (newContent != null) {
			switchFragment(newContent, title);
		}
	}

	/**
	 * 切换fragment
	 * @param fragment
	 */
	private void switchFragment(Fragment fragment, String title) {
		try{
			if (getActivity() == null) {
				return;
			}
			if (getActivity() instanceof MainActivity) {
				MainActivity fca = (MainActivity) getActivity();
				fca.switchConent(fragment, title);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
}

package com.chzu.app.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.chzu.app.fragment.ContactUsFragment;
import com.chzu.app.fragment.EmptyClassRoomFragment;
import com.chzu.app.fragment.GradeFragment;
import com.chzu.app.fragment.LeftFragment;
import com.chzu.app.fragment.NewsFragment;
import com.chzu.app.fragment.RightFragment;
import com.chzu.app.fragment.ScheduleFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class MainActivity extends SlidingFragmentActivity implements
OnClickListener {

	private ImageView topButtonLeft;
	private ImageView topButtonRight;
	private Fragment mContent;
	private TextView topTextView;
	
	/**
	 * 校园新闻Fragment
	 */
	private NewsFragment newsFragment;
	
	/**
	 * 课表Fragment
	 */
	private ScheduleFragment scheduleFragment;
	
	/**
	 * 成绩查看Fragment
	 */
	private GradeFragment gradeFragment;
	
	/**
	 * 空教室查询Fragment
	 */
	private EmptyClassRoomFragment emptyFragment;
	
	/**
	 * 联系我们Fragment
	 */
	private ContactUsFragment conFragment;
	
	
	/**
	 * 左侧菜单
	 */
	private LeftFragment leftFragment;
	
	/**
	 * 有侧菜单
	 */
	private RightFragment rightFragment;
	
	/**
	 * 用于对Fragment进行管理
	 */
	private FragmentManager fragmentManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		//requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		//初始化侧边菜单
		initSlidingMenu(savedInstanceState);
		
		fragmentManager = getSupportFragmentManager();
		
		//默认显示今日
		setTabSelection(0);
		
		topButtonLeft = (ImageView) findViewById(R.id.topButtonLeft);
		topButtonRight = (ImageView) findViewById(R.id.topButtonRight);
		topButtonLeft.setOnClickListener(this);
		topButtonRight.setOnClickListener(this);
		topTextView = (TextView) findViewById(R.id.topTv);
	}

	/**
	 * 初始化侧边栏
	 */
	private void initSlidingMenu(Bundle savedInstanceState) {
		if (mContent == null) {
			mContent = new NewsFragment();
		}

		// 设置左侧滑动菜单
		setBehindContentView(R.layout.menu_frame_left);

		if(leftFragment == null){
			leftFragment = new LeftFragment();
			getSupportFragmentManager().beginTransaction().add(R.id.menu_frame, leftFragment).commit();
		}
		// 实例化滑动菜单对象
		SlidingMenu sm = getSlidingMenu();

		// 设置可以左右滑动的菜单
		sm.setMode(SlidingMenu.LEFT_RIGHT);
		// 设置滑动阴影的宽度
		sm.setShadowWidthRes(R.dimen.shadow_width);
		// 设置滑动菜单阴影的图像资源
		sm.setShadowDrawable(null);
		// 设置滑动菜单视图的宽度
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		// 设置渐入渐出效果的值
		sm.setFadeDegree(0.35f);
		// 设置触摸屏幕的模式,这里设置为边缘
		//sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		// 设置下方视图的在滚动时的缩放比例
		sm.setBehindScrollScale(0.0f);

		//顶部action固定
		setSlidingActionBarEnabled(false);
		
		//设置右滑动菜单
		
		sm.setSecondaryMenu(R.layout.menu_frame_right);
		if(rightFragment == null){
			rightFragment = new RightFragment();
			getSupportFragmentManager().beginTransaction().add(R.id.menu_frame_two, rightFragment).commit();
		}
//		getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame_two, new RightFragment()).commit();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	/**
	 * 切换Fragment
	 * 
	 * @param fragment
	 */
	public void switchConent(Fragment fragment, String title) {
		mContent = fragment;
		if(fragment instanceof NewsFragment){
			setTabSelection(0);
		}
		if(fragment instanceof ScheduleFragment){
			setTabSelection(1);
		}
		if(fragment instanceof GradeFragment){
			setTabSelection(2);
		}
		if(fragment instanceof EmptyClassRoomFragment){
			setTabSelection(3);
		}
		if(fragment instanceof ContactUsFragment){
			setTabSelection(4);
		}
		
		
		getSlidingMenu().showContent();
		topTextView.setText(title);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.topButtonLeft:
			//显示左菜单
			getSlidingMenu().showMenu();
			break;
		case R.id.topButtonRight:
			//显示右菜单
			getSlidingMenu().showSecondaryMenu();
			break;
		default:
			break;
		}
	}
	
	
	/**
	 * 根据传入的index参数设置选中选项
	 * @param index
	 */
	private void setTabSelection(int index){
		//开启一个Fragment事物
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		//先隐藏掉所有的Fragment,以防止多个Fragment显示在界面上面
		hideFragments(transaction);
		switch (index) {
		case 0:
			//点击新闻
			if(newsFragment == null){
				newsFragment = new NewsFragment();
				transaction.add(R.id.content_frame, newsFragment);
			}else{
				transaction.show(newsFragment);
			}
			break;
		case 1:
			//点击课表
			if(scheduleFragment == null){
				scheduleFragment = new ScheduleFragment();
				Bundle bundle=new Bundle();
				bundle.putString("data", "data");
				scheduleFragment.setArguments(bundle);
				transaction.add(R.id.content_frame, scheduleFragment);
			}else{
				transaction.show(scheduleFragment);
			}
			break;
		case 2:
			//点击成绩
			if(gradeFragment == null){
				gradeFragment = new GradeFragment();
				transaction.add(R.id.content_frame, gradeFragment);
			}else{
				transaction.show(gradeFragment);
			}
			break;
		case 3:
			//点击网上
			if(emptyFragment == null){
				emptyFragment = new EmptyClassRoomFragment();
				transaction.add(R.id.content_frame, emptyFragment);
			}else{
				transaction.show(emptyFragment);
			}
			break;
		case 4:
			//点击评价
			if(conFragment == null){
				conFragment = new ContactUsFragment();
				transaction.add(R.id.content_frame, conFragment);
			}else{
				transaction.show(conFragment);
			}
			break;
		default:
			break;
		}
		transaction.commit();
	}
	
	/**
	 * 将所有的Fragment都只为隐藏状态
	 * @param transaction
	 * 用于对Fragment执行操作事务
	 */
	private void hideFragments(FragmentTransaction transaction) {
		if(newsFragment != null){
			transaction.hide(newsFragment);
		}
		if(scheduleFragment != null){
			transaction.hide(scheduleFragment);
		}
		if(gradeFragment != null){
			transaction.hide(gradeFragment);
		}
		if(emptyFragment != null){
			transaction.hide(emptyFragment);
		}
		if(conFragment != null){
			transaction.hide(conFragment);
		}
	}


}

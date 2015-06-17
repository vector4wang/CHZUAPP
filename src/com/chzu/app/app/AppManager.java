/**
 * 
 */
package com.chzu.app.app;

import java.util.Stack;

import android.app.Activity;

/**
 * @Description 全局应用类Activity的管理工具,用于压入栈/出栈/结束Activity等操作
 * @author wangxingchao
 * @version 1.0
 * @since 2015-5-17 下午7:32:32
 * 
 */
public class AppManager {

	/** 全局Activity 堆栈管理 */
	static Stack<Activity> sActivities = null;
	/** 全局的实例 */
	static AppManager sManager = null;

	/**
	 * 获取App Activity Manager 的实例,单例模式
	 * 
	 * @return Activity Manager 的堆栈管理
	 */
	public static AppManager getManager() {
		if (null == sManager) {
			sManager = new AppManager();
		}
		return sManager;
	}

	/**
	 * 保证单例模式,只能从静态方法获取实例
	 */
	private AppManager() {

	}

	/**
	 * Activity入栈
	 * 
	 * @param activity
	 */
	public void addActivity(Activity activity) {
		if (null == sActivities) {
			sActivities = new Stack<Activity>();
		}
		sActivities.add(activity);
	}

	/**
	 * Activity的栈顶
	 * 
	 * @return 当前的Activity
	 */
	public Activity currentActivity() {
		return sActivities.lastElement();
	}

	/**
	 * 从栈中移除指定的Activity
	 * 
	 * @param activity
	 * @return
	 */
	public boolean removeActivity(Activity activity) {
		return sActivities.remove(activity);
	}

	/**
	 * 从栈中移除指定类名的Activity
	 * 
	 * @param cls
	 */
	public void removeActivity(Class<?> cls) {
		if (null != cls) {
			for (Activity activity : sActivities) {
				if (cls.getClass().equals(activity)) {
					removeActivity(activity);
				}
			}
		}
	}

	/**
	 * 从栈中移除所有的Activity
	 */
	public void removeAll() {
		for (Activity activity : sActivities) {
			removeActivity(activity);
		}
	}

	/**
	 * 结束栈顶的Activity,即结束当前的Activity
	 */
	public void finishActivity() {
		finishActivity(currentActivity());
	}

	/**
	 * 结束指定的Activity
	 * 
	 * @param activity
	 */
	public void finishActivity(Activity activity) {
		if (null != activity) {
			sActivities.remove(activity);
			activity.finish();
			activity = null;
		}
	}

	/**
	 * 结束指定类名的Activity
	 * 
	 * @param cls
	 */
	public void finishActivity(Class<?> cls) {
		if (null != cls) {
			for (Activity activity : sActivities) {
				if (activity.getClass().equals(cls)) {
					finishActivity(activity);
				}
			}
		}
	}

	/**
	 * 结束所有的Activity
	 */
	public void finishAll() {
		for (Activity activity : sActivities) {
			if (null != activity) {
				finishActivity(activity);
			}
		}
		sActivities.clear(); // 清空堆栈
	}

}
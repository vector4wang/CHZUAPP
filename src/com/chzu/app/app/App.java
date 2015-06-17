/**
 * 
 */
package com.chzu.app.app;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.res.Configuration;

import com.chzu.app.bean.User;
import com.chzu.app.util.L;

/**
 * @Description 整个应用的全局上下文环境
 * @author wangxingchao
 * @version 1.0
 * @since 2015-4-30 下午7:41:56
 * 
 */
public class App extends Application {

	public static User sUser;
	
	public static String viewState;

	

	@Override
	public void onCreate() {
		super.onCreate();
		initApp();
		L.i("App OnCreate()");
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		L.i("App onTerminate()");
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		L.i("App onLowMemory()");
	}

	@SuppressLint("NewApi")
	@Override
	public void onTrimMemory(int level) {
		super.onTrimMemory(level);
		L.i("App onTrimMemory(" + level + ")");
	}

	private void initApp() {

	}

	/**
	 * 结束该应用
	 */
	public static void exit() {
		AppManager.getManager().finishAll();
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	public static void setUser(User user) {
		sUser = user;
	}

	public static User getUser() {
		return sUser;
	}
	
	public static String getViewState() {
		return viewState;
	}

	public static void setViewState(String viewState) {
		App.viewState = viewState;
	}

}

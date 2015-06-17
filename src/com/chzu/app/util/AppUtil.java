package com.chzu.app.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.chzu.app.app.AppConfig;

public class AppUtil {
	/**
	 * 根据新闻类型获取上次更新的时间
	 */
	public static String getRefreashTime(Context context , int newType){
		String timeStr = PreferenceUtil.readString(context, "NEWS_" + newType);
		if(TextUtils.isEmpty(timeStr)){
			return "醉翁之意不在酒...额,忘记时间了...";
		}
		return timeStr;
	}
	
	public static void setRefreashTime(Context context, int newType){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		PreferenceUtil.write(context, "NEWS_"+newType, df.format(new Date()));
	}
	
	/** 配置文件名,保存在/data/data/hstc/shared_prefs/app.xml */
	public static final String APP_CONFIG = "app";

	/**
	 * 获取Property配置文件
	 * 
	 * @param context
	 * @return
	 */
	public static Properties getProperties(Context context) {
		FileInputStream fis = null;
		Properties props = new Properties();
		try {
			// 获取配置文件目录
			File dirConf = context.getDir(APP_CONFIG, Context.MODE_PRIVATE);
			fis = new FileInputStream(dirConf.getPath() + File.separator + APP_CONFIG);
			// property从输入流中载入
			props.load(fis);
		} catch (Exception e) {
			if (AppConfig.isDebug) {
				e.printStackTrace();
			}
		} finally {
			try {
				if (null != fis) {
					fis.close();
				}
			} catch (Exception e) {
				if (AppConfig.isDebug) {
					e.printStackTrace();
				}
			}
		}
		return props;
	}

	/**
	 * 保存配置文件内容
	 * 
	 * @param context
	 * @param props
	 * @return
	 */
	public static boolean setProperties(Context context, Properties props) {
		FileOutputStream fos = null;

		// 文件保存目录
		File dirFile = context.getDir(APP_CONFIG, Context.MODE_APPEND);

		// 文件名
		File confFile = new File(dirFile, APP_CONFIG);
		try {
			fos = new FileOutputStream(confFile);
			// 保存 property文件,注释内容
			props.store(fos, "");
			fos.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			if (null != fos) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 读取配置信息
	 * 
	 * @param context
	 * @param key
	 *            指定获取内容的key
	 * @return 有指定的内容的key返回值,否则返回null
	 */
	public static String getValue(Context context, String key) {
		Properties props = getProperties(context);
		return null == props ? null : props.getProperty(key);
	}

	/**
	 * 写入指定的key 的value值
	 * 
	 * @param context
	 * @param key
	 *            需要写入信息的key
	 * @param value
	 *            写入信息
	 * @return
	 */
	public static boolean setValue(Context context, String key, String value) {
		Properties props = getProperties(context);
		props.setProperty(key, value);
		// 写入文件
		setProperties(context, props);
		return true;
	}

	/*************************** 使用SharedPreferences的写法 **************************************/

	/*
	 * 获取应用默认的 Preference
	 */
	public static SharedPreferences getDefSharedPreferences(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context);
	}

	/**
	 * 获取指定文件名的SharePreference
	 * 
	 * @param context
	 * @return 返回指定配置文件名的SharedPreferences
	 */
	public static SharedPreferences getSharedPreferences(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(APP_CONFIG, Context.MODE_PRIVATE);
		return preferences;
	}

	/**
	 * 获取SharePreference 的编辑对象 Editor ,用于写入不同的数据
	 * 
	 * @param context
	 * @return
	 */
	public static Editor getEditor(Context context) {
		return getSharedPreferences(context).edit();
	}

	/**
	 * 往配置文件中写入 String 类型的键值对
	 * 
	 * @param context
	 * @param key
	 *            指定写入的key
	 * @param value
	 *            对应key的value
	 * @return 写入成功返回true;否则返回false
	 */
	public static boolean putStr2Conf(Context context, String key, String value) {
		return getEditor(context).putString(key, value).commit();
	}

	/**
	 * 往配置文件中写入Boolean 的类型的键值对
	 * 
	 * @param context
	 * @param key
	 * @param value
	 * @return 写入成功返回true;否则返回false
	 */
	public static boolean putBool2Conf(Context context, String key, boolean value) {
		return getEditor(context).putBoolean(key, value).commit();
	}

	public static boolean getBoolFromConf(Context context, String key) {
		return getSharedPreferences(context).getBoolean(key, false);
	}

	public static String getStrFromConf(Context context, String key) {
		return getSharedPreferences(context).getString(key, "");
	}
}


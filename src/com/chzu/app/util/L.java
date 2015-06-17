/**
 * 
 */
package com.chzu.app.util;

import android.util.Log;

/**
 * @Description 日志工具类
 * @author act262
 * @version 1.0
 * @since 2014-4-30 下午7:49:03
 * 
 */
public class L {

	/** 默认使用的标记 */
	public static final String TAG = "Log_Utils";

	/**
	 * 打印等级最低的消息,所有的消息
	 * 
	 * @param tag
	 *            指定标记
	 * @param msg
	 *            打印的内容
	 */
	public static void v(String tag, String msg) {
		Log.v(tag, msg);
	}

	/**
	 * 打印等级为Debug的消息
	 * 
	 * @param tag
	 *            指定标记
	 * @param msg
	 *            打印的内容
	 */
	public static void d(String tag, String msg) {
		Log.d(tag, msg);
	}

	/**
	 * 打印等级为Information的消息
	 * 
	 * @param tag
	 *            指定标记
	 * @param msg
	 *            打印的内容
	 */
	public static void i(String tag, String msg) {
		Log.i(tag, msg);
	}

	/**
	 * 打印等级为Warning的消息
	 * 
	 * @param tag
	 *            指定标记
	 * @param msg
	 *            打印的内容
	 */
	public static void w(String tag, String msg) {
		Log.w(tag, msg);
	}

	/**
	 * 打印等级为Error的消息
	 * 
	 * @param tag
	 *            指定标记
	 * @param msg
	 *            打印的内容
	 */
	public static void e(String tag, String msg) {
		Log.e(tag, msg);
	}

	/**
	 * 使用默认的标记
	 * 
	 * @param msg
	 *            打印的消息
	 */
	public static void v(String msg) {
		v(TAG, msg);
	}

	/**
	 * 使用默认的标记
	 * 
	 * @param msg
	 *            打印的消息
	 */
	public static void d(String msg) {
		d(TAG, msg);
	}

	/**
	 * 使用默认的标记
	 * 
	 * @param msg
	 *            打印的消息
	 */
	public static void i(String msg) {
		i(TAG, msg);
	}

}

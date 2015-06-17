/**
 * 
 */
package com.chzu.app.app;

/**
 * @Description App 配置信息
 * @author wangxingchao
 * @version 1.0
 * @since 2015-4-25 上午12:06:41
 * 
 */
public class AppConfig {
	/** 调试模式开关 */
	public static boolean isDebug = true;

	/** 请求超时时间 10s */
	public static final int CONNECT_TIME_OUT = 1000 * 10;
	/** 读取超时时间10s */
	public static final int CONNECT_SO_TIMEOUT = 1000 * 10;

	/** HttpClient 的 User-Agent */
	public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/34.0.1847.116 Safari/537.36";

}
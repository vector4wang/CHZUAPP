package com.chzu.app.util;


public class URLUtil {
	/**
	 * 蔚园要问
	 * 院部动态
	 * 通知公告
	 * 教科研信息
	 */
	public static final String NEWS_LIST_WYYW = "http://www.chzu.edu.cn/s/1/t/1152/p/2/list.htm";
	public static final String NEWS_LIST_YBDT = "http://www.chzu.edu.cn/s/1/t/1152/p/3/list.htm";
	public static final String NEWS_LIST_TZGG = "http://www.chzu.edu.cn/s/1/t/1152/p/4/list.htm";
	public static final String NEWS_LIST_JKYXX = "http://www.chzu.edu.cn/s/1/t/1152/p/5/list.htm";
	
	public static String gengrateURL(int newsType){
		String urlStr = "";
		switch (newsType)
		{
		case URLDetail.NEWS_LIST_WYYW:
			urlStr = NEWS_LIST_WYYW;
			break;
		case URLDetail.NEWS_LIST_YBDT:
			urlStr = NEWS_LIST_YBDT;
			break;
		case URLDetail.NEWS_LIST_TZGG:
			urlStr = NEWS_LIST_TZGG;
			break;
		case URLDetail.NEWS_LIST_JKYXX:
			urlStr = NEWS_LIST_JKYXX;
			break;
		default:
			urlStr = NEWS_LIST_WYYW;
			break;
		}
		return urlStr;
	}
	
	/** 教务管理系统 */
	//池州学院http://211.86.193.14 default210.45.160.115
	public static final String JWGL_URL = "http://210.45.160.115";
	
	/** 教务系统登录地址 ,中间的参数为一段24位的的字符串和/() */
	public static final String JWGL_LOGIN_URL = JWGL_URL + "%s/default2.aspx";
	
	/** 教务系统验证码地址 ,中间的参数为一段24位的的字符串和/() */
	public static final String JWGL_CHECKCODE_URL = JWGL_URL + "%s/CheckCode.aspx";
	
	//public static final String JWGL_CHECKCODE_URL = JWGL_URL + "/CheckCode.aspx";
	/** 教务系统登录后的主页,第一个参数:一段24位的字符串和/() ,第二个参数:学号 */
	public static final String JWGL_MAIN_URL = JWGL_URL + "%s/xs_main.aspx?xh=%s";
	
	/** 教务系统用户的头像 ,第一个参数:一段24位的的字符串和/() ,第二个参数:学号 */
	public static final String JWGL_HEAD_ICON_URL = JWGL_URL + "%s/readimagexs.aspx?xh=%s";
	
	/** 查找用户信息,第一个参数为一段24位的的字符串和/(),第二个参数:学号 ,第三个参数:姓名 */
	public static final String JWGL_USER_INFO_URL = JWGL_URL + "%s/xsgrxx.aspx?xh=%s&xm=%s&gnmkdm=N121501";
	
	/** 查找课程表,第一个参数为一段24位的的字符串和/(),第二个参数:学号 ,第三个参数:姓名 */
	/**http://210.45.160.115/(iqsqej45zddpfxjx4z5xwiv4)/xskbcx.aspx?xh=2011211311&xm=%CD%F5%D0%CB%B3%AC&gnmkdm=N121603*/
	public static final String JWGL_USER_SCHEDULE_URL = JWGL_URL + "%s/xskbcx.aspx?xh=%s&xm=%s&gnmkdm=N121603";
	
	/** 查找用户成绩 ,第一个参数:24位安全码,第二个参数:学号,第三个参数姓名 */
	public static final String JWGL_USER_SCORE_URL = JWGL_URL + "%s/xscjcx.aspx?xh=%s&xm=%s&gnmkdm=N121605";
	
	/**查找空教室,第一个参数24位安全码，第二个参数学号，第三个参数姓名*/
	public static final String JWGL_EMPTY_CLASSROOM = JWGL_URL + "%s/xxjsjy.aspx?xh=%s&xm=%s&gnmkdm=N121611";
}

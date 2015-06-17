/**
 * 
 */
package com.chzu.app.util;

/**
 * @Description 字符串工具类
 * @author act262
 * @version 1.0
 * @since 2014-4-29 下午9:12:49
 * 
 */
public class StringUtils {

	/**
	 * 登录验证
	 * 
	 * @param params
	 *            第一个参数:登录账号 ,第二个参数:密码 , 第三个参数:验证码
	 * @return 返回匹配后的信息
	 */
	public static String loginMatch(String... params) {
		if (isEmpty(params[0])) {
			return "账号不能为空";
		} else if (isEmpty(params[1])) {
			return "密码不能为空";
		} else if (isEmpty(params[2])) {
			return "验证码不能为空";
		} else if (!isValidAccount(params[0])) {
			return "输入账号不合法";
		}
		return null;
	}

	/**
	 * 判断输入的字符串是否为空字符串
	 * 
	 * @param input
	 * @return 如果输入为空字符串返回true;不为空字符串则返回FALSE
	 */
	public static boolean isEmpty(String input) {
		if (null == input || "".equals(input) || input.length() <= 0) {
			return true;
		}
		return false;
	}

	/**
	 * 判断是否为合法的学号
	 * 
	 * @param input
	 * @return
	 */
	public static boolean isValidAccount(String input) {
		String regex = "\\d{8,12}"; // 8位数字到12为数字
		if (!input.matches(regex)) {
			return false;
		}
		return true;
	}

}

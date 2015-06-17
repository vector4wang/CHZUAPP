/**
 * 
 */
package com.chzu.app.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.util.EntityUtils;

import android.net.http.AndroidHttpClient;
import android.util.Log;

import com.chzu.app.activity.LoginActivity;

/**
 * @Description
 * @author act262
 * @version 1.0
 * @since 2014-6-23 下午1:37:44
 * 
 */
public class HttpUtils4Jwgl {

	static AndroidHttpClient httpClient = HttpUtils.getHttpClient();

	/**
	 * 获取验证码的输入流
	 * 
	 * @return
	 * @throws IOException
	 */
	public static synchronized InputStream getCheckCodeInputStream() throws IOException {
		String url = String.format(URLUtil.JWGL_CHECKCODE_URL, LoginActivity.randomUrl);
		HttpResponse response = HttpUtils.getHttpGetResponse(httpClient, url);
		if (null != response) {
			// 第一次连接将重定向，然后跳转到指定的地址，获取得中间的一段随机代码保存，
			int statusCode = response.getStatusLine().getStatusCode();
			if (HttpStatus.SC_MOVED_TEMPORARILY == statusCode || HttpStatus.SC_MOVED_PERMANENTLY == statusCode) {
				// 存储随机码
				LoginActivity.randomUrl = response.getFirstHeader("Location").getValue().substring(0, 27);

				//
				Log.i("", "randomUrl " + LoginActivity.randomUrl);
				//
				for (Header header : response.getAllHeaders()) {
					Log.d("为获取随机码请求", "" + header.toString());
				}
				//

				// 得到新的地址后再次请求
				url = String.format(URLUtil.JWGL_CHECKCODE_URL, LoginActivity.randomUrl);
				response = HttpUtils.getHttpGetResponse(httpClient, url);

				for (Header header : response.getAllHeaders()) {
					Log.d("再次请求获取验证码", "" + header.toString());
				}

			}
			// 第N次连接后获取的内容
			else if (HttpStatus.SC_OK == statusCode) {

			}
			return HttpUtils.getEntity(response).getContent();
		}

		return null;
	}

	/**
	 * 登陆
	 * 
	 * @param params
	 *            参数1：账号 ；参数2：密码 ；参数3：验证码
	 * @return 返回登陆时反馈的信息：登陆失败，验证码错误，不存在该用户，密码错误。。。
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public static int login(String... params) throws UnsupportedEncodingException, IOException {
		String url = String.format(URLUtil.JWGL_LOGIN_URL, LoginActivity.randomUrl);
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("__VIEWSTATE", "dDwyODE2NTM0OTg7Oz7bY/CYFuo3fHha6sypPxuBqKw0zw==");
		paramsMap.put("txtUserName", params[0]);// 账号
		paramsMap.put("TextBox2", params[1]);// 密码
		paramsMap.put("txtSecretCode", params[2]);// 验证码
		paramsMap.put("RadioButtonList1", URLEncoder.encode("学生", "GBK"));// 学生?教师
		paramsMap.put("Button1", "");
		paramsMap.put("lbLanguage", "");
		paramsMap.put("hidPdrs", "");
		paramsMap.put("hidsc", "");

		HttpResponse response = HttpUtils.getHttpPostResponse(httpClient, url, HttpUtils.parcelParams(paramsMap));
		
		int statusCode = response.getStatusLine().getStatusCode();
		String content = EntityUtils.toString(response.getEntity());
		
		if (HttpStatus.SC_MOVED_TEMPORARILY == statusCode ) {
			return LOGIN_STATUS_OK;
		}
		if (content.contains(CHECKCODE_ERROR)) {
			return LOGIN_STATUS_CHECKCODE_ERROR;
		} else if (content.contains(USER_ERROR)) {
			return LOGIN_STATUS_USER_ERROR;
		} else if (content.contains(PASSWORD_ERROR)) {
			return LOGIN_STATUS_PW_ERROR;
		}
		return LOGIN_STATUS_SEVICE_BUSY;
	}

	
	public static final int LOGIN_STATUS_OK = 0x00;
	public static final int LOGIN_STATUS_CHECKCODE_ERROR = 0x01;
	public static final int LOGIN_STATUS_USER_ERROR = 0x02;
	public static final int LOGIN_STATUS_PW_ERROR = 0x03;
	public static final int LOGIN_STATUS_SEVICE_BUSY = 0x04;

	public static final String CHECKCODE_ERROR = "验证码不正确";
	public static final String USER_ERROR = "用户名不存在";
	public static final String PASSWORD_ERROR = "密码错误";
	public static final String SEVICE_BUSY = "服务器繁忙";

	public static String getLoginResult(String xh) throws IOException {
		String url = String.format(URLUtil.JWGL_MAIN_URL, LoginActivity.randomUrl, xh);
		HttpResponse response = HttpUtils.getHttpGetResponse(httpClient, url);
		return EntityUtils.toString(HttpUtils.getEntity(response));
	}
	
}

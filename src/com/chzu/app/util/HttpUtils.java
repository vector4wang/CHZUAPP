/**
 * 
 */
package com.chzu.app.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerPNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.AndroidHttpClient;
import android.util.Log;

import com.chzu.app.activity.LoginActivity;
import com.chzu.app.app.AppConfig;

/**
 * @Description Http工具辅助类,包装了一些常用的方法功能
 * @author act262
 * @version 1.0
 * @since 2014-4-29 下午10:28:28
 * 
 */
public class HttpUtils {
	private static AndroidHttpClient sHttpClient;
	/** Httpclient从连接池中取出的超时时间 ,long类型的 */
	private static final long HTTP_CLIENT_TIMEOUT = 10 * 1000;
	/** 连接超时时间 */
	private static final int HTTP_CONNECT_TIMEOUT = 300 * 1000;
	/** 请求超时时间 */
	private static final int HTTP_CONNECT_SO_TIMEOUT = 100 * 1000;
	/** 编码格式 */
	private static final String HTTP_CHARSET = "gb2312";

	/**
	 * 使用AndroidHttpClient,使用完后记得close释放资源,避免内存泄露 可以多线程使用 可以自定义一些参数...
	 * 
	 * @return AndroidHttpClient的单例
	 */
	public static synchronized AndroidHttpClient getHttpClient() {
		return getHttpClient(null);
	}

	/**
	 * 使用AndroidHttpClient,使用完后记得close释放资源,避免内存泄露
	 * 
	 * @param context
	 *            上下文,用户保存SSL,cookie等
	 * @return AndroidHttpClient的单例
	 */
	public static synchronized AndroidHttpClient getHttpClient(Context context) {
		// 获取AndroidHttpClient的单例
		if (null == sHttpClient) {
			sHttpClient = AndroidHttpClient.newInstance(AppConfig.USER_AGENT, context);
			// AndroidHttpClient 默认不会自动处理重定向,需要手动设置为自动重定向
			// sHttpClient.getParams().setBooleanParameter(ClientPNames.HANDLE_REDIRECTS,
			// true);

			// 连接超时,默认是60s
			sHttpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, HTTP_CONNECT_TIMEOUT);
			// 请求超时,默认60s
			sHttpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, HTTP_CONNECT_SO_TIMEOUT);
			// sHttpClient.getParams().setParameter(CoreConnectionPNames.SOCKET_BUFFER_SIZE,
			// 1024);

			// 设置编码格式
			sHttpClient.getParams().setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, HTTP_CHARSET);
			// 连接池取出超时
			sHttpClient.getParams().setParameter(ConnManagerPNames.TIMEOUT, HTTP_CLIENT_TIMEOUT);
		}
		return sHttpClient;
	}

	/**
	 * 封装Post请求的参数
	 * 
	 * @param paramsMap
	 *            每个参数对应使用的 key/value
	 * @return
	 */
	public static List<BasicNameValuePair> parcelParams(Map<String, String> paramsMap) {
		List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
		if (null != paramsMap) {
			for (Map.Entry<String, String> p : paramsMap.entrySet()) {
				list.add(new BasicNameValuePair(p.getKey(), p.getValue()));// 封装参数
			}
		}
		return list;
	}

	/**
	 * 获取response包装的内容
	 * 
	 * @param response
	 * @return
	 */
	public static HttpEntity getEntity(HttpResponse response) {
		if (null == response) {
			return null;
		}
		return response.getEntity();
	}

	/**
	 * 获取HttpPost的请求响应内容
	 * 
	 * @param httpClient
	 * @param url
	 * @param params
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public static HttpResponse getHttpPostResponse(AndroidHttpClient httpClient, String url, List<BasicNameValuePair> params)
			throws UnsupportedEncodingException, IOException {
		HttpPost httpPost = new HttpPost(url);
		httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP_CHARSET));

		for (Header header : httpPost.getAllHeaders()) {
			L.d("post header", "" + header.toString());
		}
		L.d("getHttpPostResponse", " post参数 :" + params.toString());
		return httpClient.execute(httpPost);
	}

	/**
	 * 获取httpGet的请求响应内容
	 * 
	 * @param httpClient
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static HttpResponse getHttpGetResponse(AndroidHttpClient httpClient, String url) throws IOException {
		HttpGet httpGet = new HttpGet(url);

		return httpClient.execute(httpGet);
	}

	public static InputStream getCheckCodeStream(AndroidHttpClient client) {
		if (null == client) {
			return null;
		}

		// 第一次连接后使用指定的url地址

		HttpGet httpGet = new HttpGet(String.format(URLUtil.JWGL_CHECKCODE_URL, ""));
		HttpResponse response = null;
		try {

			response = client.execute(httpGet);

			if (null != response) {
				// 重定向后取得中间的一段代码
				int statusCode = response.getStatusLine().getStatusCode();
				if (HttpStatus.SC_MOVED_TEMPORARILY == statusCode || HttpStatus.SC_MOVED_PERMANENTLY == statusCode) {
					LoginActivity.randomUrl = response.getFirstHeader("Location").getValue().substring(0, 27);
					//
					for (Header header : response.getAllHeaders()) {
						Log.d("为获取随机码请求", "" + header.toString());
					}
					//
					Log.i("", "randomUrl " + LoginActivity.randomUrl);

					httpGet.setURI(URI.create(String.format(URLUtil.JWGL_CHECKCODE_URL, LoginActivity.randomUrl)));
					response = client.execute(httpGet);

					for (Header header : response.getAllHeaders()) {
						Log.d("再次请求获取验证码", "" + header.toString());
					}
					if (null != response) {
						L.d("response != null");
						return response.getEntity().getContent();
					}

				}
			}

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 释放连接资源
			// httpGet.abort();
		}
		return null;

	}

	/**
	 * 获取登录验证结果
	 * 
	 * @param client
	 * @param info
	 *            第一个参数:账号 ,第二个参数密码 ,第三个参数:验证码 ,第四个参数:登录类型
	 * @return
	 */
	public static String getLoginResult(AndroidHttpClient client, String... info) {
		if (null == client) {
			return null;
		}

		// 登录时请求post
		String url = String.format(URLUtil.JWGL_LOGIN_URL, LoginActivity.randomUrl);
		L.d(url);
		HttpPost httpPost = new HttpPost(url);
		// 登录成功后页面跳转 get
		HttpGet httpGet = null;
		HttpResponse response = null;

		// 这是正方教务系统的登录表单参数
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("__VIEWSTATE", "dDwyODE2NTM0OTg7Oz7uS1X7FLbhl4SUBe7Gm6dFut0yKQ=="));
		params.add(new BasicNameValuePair("txtUserName", info[0]));// 账号
		params.add(new BasicNameValuePair("TextBox2", info[1]));// 密码
		params.add(new BasicNameValuePair("txtSecretCode", info[2]));// 验证码
		params.add(new BasicNameValuePair("RadioButtonList1", info[3]));// 学生?教师
		params.add(new BasicNameValuePair("Button1", ""));
		params.add(new BasicNameValuePair("lbLanguage", ""));
		params.add(new BasicNameValuePair("hidPdrs", ""));
		params.add(new BasicNameValuePair("hidsc", ""));

		httpPost.setHeader("Referer", url);
		try {
			// 注意编码使用,否则乱码
			httpPost.setEntity(new UrlEncodedFormEntity(params, "gb2312"));
			response = client.execute(httpPost);

			L.d(params.toString());
			for (Header header : response.getAllHeaders()) {
				L.d("getLoginResult 响应头", header.toString());
			}
			L.d(EntityUtils.toString(response.getEntity()));

			int statusCode = response.getStatusLine().getStatusCode();
			L.d("" + statusCode);
			// 重定向到登录成功后的页面
			if (HttpStatus.SC_MOVED_TEMPORARILY == statusCode || HttpStatus.SC_MOVED_PERMANENTLY == statusCode) {
				L.d("getLoginResult", "重定向了");
				L.d("重定向执行开始:  " + URLUtil.JWGL_URL + response.getFirstHeader("Location").getValue());
				httpGet = new HttpGet(URLUtil.JWGL_URL + response.getFirstHeader("Location").getValue());
				response = client.execute(httpGet);
				L.d("重定向响应结果了");

				// 响应成功
				if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
					String result = EntityUtils.toString(response.getEntity());
					L.d("result", result);
					// 转换成字符串返回
					return result;
				}
			} else {
				L.d("没有重定向啦？？");
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			L.d("abort");
			if (null != httpGet) {
				// httpGet.abort();
			}
			// httpPost.abort();
		}
		return null;
	}

	/**
	 * @param client
	 * @param info
	 *            第一个参数:安全码 ,第二个参数:学号
	 * @return
	 */
	public static InputStream getHeadStream(AndroidHttpClient client, String... info) {
		if (null == client) {
			return null;
		}

		HttpGet httpGet = new HttpGet(String.format(URLUtil.JWGL_HEAD_ICON_URL, LoginActivity.randomUrl, info[0]));
		httpGet.setHeader("Referer", String.format(URLUtil.JWGL_USER_INFO_URL, LoginActivity.randomUrl, info[0], info[1]));
		HttpResponse response = null;

		try {
			response = client.execute(httpGet);
			if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
				HttpEntity entity = response.getEntity();
				if (null != entity) {
					L.d("获取有内容");
					return entity.getContent();
				}
			}
			return null;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpGet.abort();
		}

		return null;
	}

	/**
	 * 使用get方法请求数据通用方法,指定不同格式的url和参数,返回特定的网页数据
	 * 
	 * @param client
	 *            android http client
	 * @param url
	 *            在URLUtil下的基础url,用于填充个数数据
	 * @param info
	 *           参数1:学号 ; 参数2:姓名
	 * @return 返回访问服务器后返回的字符串数据
	 */
	public static String getHttpResponse(AndroidHttpClient client, String url, String... info) {
		if (null == client) {
			return null;
		}
		
		// 使用get请求获取数据
		HttpGet httpGet = null;
		// 使用get请求的带参数的url
		String targetUrl = "";
		try {
			targetUrl = String.format(url, LoginActivity.randomUrl, info[0], URLEncoder.encode(info[1], "gb2312"));
			L.d("getHttpResponse", targetUrl);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
			return null;
		}

		// 注意使用编码转换,否则请求参数不正确 ;参数1:安全码,参数2:学号,参数3:姓名
		httpGet = new HttpGet(targetUrl);
		// 网站内容跳转,加上引用页的头信息,否则将强制跳转至首页
		httpGet.setHeader("Referer", String.format(URLUtil.JWGL_MAIN_URL, LoginActivity.randomUrl, info[0]));

		HttpResponse response = null;

		try {
			response = client.execute(httpGet);

			if (HttpStatus.SC_OK != response.getStatusLine().getStatusCode()) {
				L.d("getHttpResponse", "not ok " + response.getStatusLine());
			}

			HttpEntity entity = response.getEntity();
			if (null != entity) {
				return EntityUtils.toString(entity);
			}

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			// 关闭连接,释放资源
			httpGet.abort();
			L.d("getHttpResponse", "http get abort");
		}
		return null;
	}
	
	/**
	 * 
	 * @param 使用post方法请求数据通用方法,指定不同格式的url和参数,返回特定的网页数据
	 * @param url
	 * @param info参数1:学号 ; 参数2:姓名 ; 参数3:学年制 ; 参数4：学期
	 * @return
	 */
	public static String getHttpResponsePost(AndroidHttpClient client, String url, String... info){
		if (null == client) {
			return null;
		}
		String targetUrl = null;
		try {
			targetUrl = String.format(url, LoginActivity.randomUrl, info[0], URLEncoder.encode(info[1], "gb2312"));
			L.d("getHttpResponse", targetUrl);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
			return null;
		}
		HttpPost httpPost = new HttpPost(targetUrl);
		HttpResponse response = null;
		//获取课表POST表单
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("__EVENTTARGET", "xnd"));
		params.add(new BasicNameValuePair("__EVENTARGUMENT",""));
		params.add(new BasicNameValuePair("__VIEWSTATE","dDwtMTY3ODA2Njg2OTt0PDtsPGk8MT47PjtsPHQ8O2w8aTwxPjtpPDI+O2k8ND4" +
				"7aTw3PjtpPDk+O2k8MTE+O2k8MTM+O2k8MTU+O2k8MjE+O2k8MjM+O2k8MjU+O2k8Mjc+O2k8Mjk+O2k8MzE+Oz47bDx0PHA8cDxsPF" +
				"RleHQ7PjtsPFxlOz4+Oz47Oz47dDx0PHA8cDxsPERhdGFUZXh0RmllbGQ7RGF0YVZhbHVlRmllbGQ7PjtsPHhuO3huOz4+Oz47dDxpP" +
				"DQ+O0A8MjAxNC0yMDE1OzIwMTMtMjAxNDsyMDEyLTIwMTM7MjAxMS0yMDEyOz47QDwyMDE0LTIwMTU7MjAxMy0yMDE0OzIwMTItMjAx" +
				"MzsyMDExLTIwMTI7Pj47bDxpPDI+Oz4+Ozs+O3Q8dDw7O2w8aTwyPjs+Pjs7Pjt0PHA8cDxsPFRleHQ7PjtsPOWtpuWPt++8mjIwMTE" +
				"yMTEzMTE7Pj47Pjs7Pjt0PHA8cDxsPFRleHQ7PjtsPOWnk+WQje+8mueOi+WFtOi2hTs+Pjs+Ozs+O3Q8cDxwPGw8VGV4dDs+O2w85a" +
				"2m6Zmi77ya6K6h566X5py65LiO5L+h5oGv5bel56iL5a2m6ZmiOz4+Oz47Oz47dDxwPHA8bDxUZXh0Oz47bDzkuJPkuJrvvJrnvZHnu" +
				"5zlt6XnqIs7Pj47Pjs7Pjt0PHA8cDxsPFRleHQ7PjtsPOihjOaUv+ePre+8mue9keW3pTExMjs+Pjs+Ozs+O3Q8O2w8aTwxPjs+O2w8" +
				"dDxAMDw7Ozs7Ozs7Ozs7Pjs7Pjs+Pjt0PHA8bDxWaXNpYmxlOz47bDxvPGY+Oz4+O2w8aTwxPjs+O2w8dDxAMDw7Ozs7Ozs7Ozs7Pjs" +
				"7Pjs+Pjt0PEAwPHA8cDxsPFBhZ2VDb3VudDtfIUl0ZW1Db3VudDtfIURhdGFTb3VyY2VJdGVtQ291bnQ7RGF0YUtleXM7PjtsPGk8MT" +
				"47aTwwPjtpPDA+O2w8Pjs+Pjs+Ozs7Ozs7Ozs7Oz47Oz47dDxAMDxwPHA8bDxQYWdlQ291bnQ7XyFJdGVtQ291bnQ7XyFEYXRhU291c" +
				"mNlSXRlbUNvdW50O0RhdGFLZXlzOz47bDxpPDE+O2k8MD47aTwwPjtsPD47Pj47Pjs7Ozs7Ozs7Ozs+Ozs+O3Q8QDA8cDxwPGw8UGFn" +
				"ZUNvdW50O18hSXRlbUNvdW50O18hRGF0YVNvdXJjZUl0ZW1Db3VudDtEYXRhS2V5czs+O2w8aTwxPjtpPDA+O2k8MD47bDw+Oz4+Oz4" +
				"7Ozs7Ozs7Ozs7Pjs7Pjt0PEAwPHA8cDxsPFBhZ2VDb3VudDtfIUl0ZW1Db3VudDtfIURhdGFTb3VyY2VJdGVtQ291bnQ7RGF0YUtleX" +
				"M7PjtsPGk8MT47aTwwPjtpPDA+O2w8Pjs+Pjs+Ozs7Ozs7Ozs7Oz47Oz47Pj47Pj47Pm8cpkCXGxafEcKegJXoPIoXf6cW"));
		params.add(new BasicNameValuePair("__VIEWSTATEGENERATOR","55530A43"));
		params.add(new BasicNameValuePair("xnd",info[2]));
		params.add(new BasicNameValuePair("xqd",info[3]));
		httpPost.setHeader("Referer", targetUrl);
		try {
			// 注意编码使用,否则乱码
			httpPost.setEntity(new UrlEncodedFormEntity(params, "gb2312"));
			response = client.execute(httpPost);
			int statusCode = response.getStatusLine().getStatusCode();
			if(response.getStatusLine().getStatusCode() == statusCode){
				String content = EntityUtils.toString(response.getEntity());
				return content;
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			L.d("abort");
			if (null != httpPost) {
				httpPost.abort();
			}
		}
		return null;
	}
	
	
	
	public static Bitmap HttpGetBmp(String url)
	{
		HttpGet httpget = new HttpGet(url);
		BasicHttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 3000);
		HttpConnectionParams.setSoTimeout(httpParams, 3000);
		Bitmap bitmap = null;
		try
		{
			HttpClient httpclient = new DefaultHttpClient(httpParams);
			HttpResponse response = httpclient.execute(httpget);
			InputStream is = response.getEntity().getContent();
			byte[] bytes = new byte[1024];
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			int count = 0;
			while ((count = is.read(bytes)) != -1)
			{
				System.out.println("readBitmap");
				bos.write(bytes, 0, count);
			}
			byte[] byteArray = bos.toByteArray();
			bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
			is.close();
			bos.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return bitmap;
	}
	
}

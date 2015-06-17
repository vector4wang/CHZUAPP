/**
 * 
 */
package com.chzu.app.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.net.http.AndroidHttpClient;

import com.chzu.app.activity.LoginActivity;
import com.chzu.app.bean.Score;
import com.chzu.app.bean.ScoreInfo;

public class ScoreUtils {
	/**
	 * 获取历年成绩
	 * 
	 * @param client
	 * @param info
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static String getResponse(AndroidHttpClient client, Object... info)  {
		if (null == client) {
			return null;
		}

		HttpPost httpPost = null;
		// 使用post 请求的带参数的url
		String targetUrl = "";
		try {
			//
			targetUrl = String.format(URLUtil.JWGL_USER_SCORE_URL, LoginActivity.randomUrl, info[0].toString(), URLEncoder.encode(info[1].toString(), "gb2312"));
			L.d("getHttpResponse", targetUrl);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
			return null;
		}

		// 注意使用编码转换,否则请求参数不正确
		httpPost = new HttpPost(targetUrl);
		// 网站内容跳转,加上引用页的头信息,否则将强制跳转至首页
		httpPost.setHeader("Referer", targetUrl);

		// 查询成绩使用的参数
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("__EVENTTARGET", ""));
		params.add(new BasicNameValuePair("__EVENTARGUMENT", ""));
		params.add(new BasicNameValuePair(
				"__VIEWSTATE",
				getViewState(client,targetUrl,info[0].toString())));
		params.add(new BasicNameValuePair("__VIEWSTATEGENERATOR", "9727EB43"));
		params.add(new BasicNameValuePair("hidLanguage", ""));
		params.add(new BasicNameValuePair("ddlXN", ""));
		params.add(new BasicNameValuePair("ddlXQ", ""));
		params.add(new BasicNameValuePair("ddl_kcxz", ""));
		try {
			params.add(new BasicNameValuePair("btn_zcj", URLEncoder.encode("历年成绩", "GBK")));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		HttpResponse response = null;

		try {
			httpPost.setEntity(new UrlEncodedFormEntity(params, "gb2312"));
			response = client.execute(httpPost);

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
			httpPost.abort();
			L.d("getResponse", "http post abort");
		}
		return null;
	}

	/**
	 * 指定学年,学期获取数据
	 * 
	 * @param client
	 * @param info
	 *            参数1:学号 ;参数2:姓名;参数3:学年;参数4:学期
	 * @return
	 */
	private static String getResponseByParams(AndroidHttpClient client, String... info) {
		if (null == client) {
			return null;
		}

		HttpPost httpPost = null;
		// 使用post 请求的带参数的url
		String targetUrl = "";
		try {
			//
			targetUrl = String.format(URLUtil.JWGL_USER_SCORE_URL, LoginActivity.randomUrl, info[0], URLEncoder.encode(info[1], "gb2312"));
			L.d("getHttpResponse", targetUrl);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
			return null;
		}
		// 注意使用编码转换,否则请求参数不正确
		httpPost = new HttpPost(targetUrl);
		// 网站内容跳转,加上引用页的头信息,否则将强制跳转至首页
		httpPost.setHeader("Referer", targetUrl);

		// 查询成绩使用的参数
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("__EVENTTARGET", ""));
		params.add(new BasicNameValuePair("__EVENTARGUMENT", ""));
		params.add(new BasicNameValuePair(
				"__VIEWSTATE",
				"dDwyODE2NTM0OTg7Oz7bY/CYFuo3fHha6sypPxuBqKw0zw=="));
		params.add(new BasicNameValuePair("hidLanguage", ""));
		params.add(new BasicNameValuePair("ddlXN", info[2])); // 学年
		params.add(new BasicNameValuePair("ddlXQ", info[3])); // 学期
		params.add(new BasicNameValuePair("ddl_kcxz", ""));
		params.add(new BasicNameValuePair("btn_xq", "学期成绩"));

		HttpResponse response = null;

		try {
			httpPost.setEntity(new UrlEncodedFormEntity(params, "gb2312"));
			response = client.execute(httpPost);

			for (Header header : response.getAllHeaders()) {
				L.d("getHttpResponse", header.toString());
			}

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
			httpPost.abort();
			L.d("getResponse", "http post abort");
		}
		return null;
	}

	/**
	 * 解析内容
	 * 
	 * @param content
	 * @return
	 */
	public static List<Score> getScore(String content) {
		List<Score> scoreList = new ArrayList<Score>();

		// 获取到每行数据的选择器
		String rowRegex = "div.main_box div.mid_box span.formbox table#Datagrid1.datelist tbody tr";

		// 每行的数据元素
		Elements rowElements = Jsoup.parse(content).select(rowRegex);

		for (int i = 1; i < rowElements.size(); i++) {
			Score score = new Score();
			Elements elements = rowElements.get(i).select("td");

			score.setAcademicYear(elements.get(0).text());
			score.setSemester(elements.get(1).text());
			score.setCourseCode(elements.get(2).text());
			score.setCourseName(elements.get(3).text());
			score.setCourseProperty(elements.get(4).text());
			score.setCourseBelong(elements.get(5).text());
			score.setXuefen(elements.get(6).text());
			score.setJidian(elements.get(7).text());
			score.setScore(elements.get(8).text());
			score.setFxbj(elements.get(9).text());
			score.setBkcj(elements.get(10).text());
			score.setCxcj(elements.get(11).text());
			score.setBeginCollege(elements.get(12).text());
			score.setComment(elements.get(13).text());
			score.setCxbj(elements.get(14).text());

			scoreList.add(score);
		}
		return scoreList;
	}


	//获取状态码
	private static String getViewState(AndroidHttpClient client , String url , String xh){
		Document doc;
		String viewState = null;
		if (null == client) {
			return null;
		}
		String content = null;
		HttpGet httpGet = null;
		httpGet = new HttpGet(url);
		httpGet.setHeader("Referer", String.format(URLUtil.JWGL_MAIN_URL, LoginActivity.randomUrl, xh));
		HttpResponse response = null;
		try {
			response = client.execute(httpGet);

			for (Header header : response.getAllHeaders()) {
				L.d("getHttpResponse", header.toString());
			}

			if (HttpStatus.SC_OK != response.getStatusLine().getStatusCode()) {
				L.d("getHttpResponse", "not ok " + response.getStatusLine());
			}

			HttpEntity entity = response.getEntity();
			if (null != entity) {
				content =  EntityUtils.toString(entity);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭连接,释放资源
			httpGet.abort();
			L.d("getHttpResponse", "http get abort");
		}

		if(content != null){
			doc = Jsoup.parse(content);
			String rex = "input[name=__VIEWSTATE]";
			Element rowElements = doc.select(rex).get(0);
			viewState = rowElements.attr("value");
		}

		return viewState;
	}


	/**
	 * 成绩统计
	 * @param mHttpClient
	 * @param params
	 * @return
	 */
	public static ScoreInfo getScoreInfo(AndroidHttpClient mHttpClient,
			Object[] info) {

		if (null == mHttpClient) {
			return null;
		}

		HttpPost httpPost = null;
		// 使用post 请求的带参数的url
		String targetUrl = "";
		try {
			//
			targetUrl = String.format(URLUtil.JWGL_USER_SCORE_URL, LoginActivity.randomUrl, info[0].toString(), URLEncoder.encode(info[1].toString(), "gb2312"));
			L.d("getHttpResponse", targetUrl);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
			return null;
		}

		// 注意使用编码转换,否则请求参数不正确
		httpPost = new HttpPost(targetUrl);
		// 网站内容跳转,加上引用页的头信息,否则将强制跳转至首页
		httpPost.setHeader("Referer", targetUrl);

		// 查询成绩使用的参数
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("__EVENTTARGET", ""));
		params.add(new BasicNameValuePair("__EVENTARGUMENT", ""));
		params.add(new BasicNameValuePair(
				"__VIEWSTATE",
				getViewState(mHttpClient,targetUrl,info[0].toString())));
		params.add(new BasicNameValuePair("__VIEWSTATEGENERATOR", "9727EB43"));
		params.add(new BasicNameValuePair("hidLanguage", ""));
		params.add(new BasicNameValuePair("ddlXN", ""));
		params.add(new BasicNameValuePair("ddlXQ", ""));
		params.add(new BasicNameValuePair("ddl_kcxz", ""));
		try {
			params.add(new BasicNameValuePair("Button1", URLEncoder.encode("成绩统计", "GBK")));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		HttpResponse response = null;

		try {
			httpPost.setEntity(new UrlEncodedFormEntity(params, "gb2312"));
			response = mHttpClient.execute(httpPost);

			if (HttpStatus.SC_OK != response.getStatusLine().getStatusCode()) {
				L.d("getHttpResponse", "not ok " + response.getStatusLine());
			}

			HttpEntity entity = response.getEntity();
			if (null != entity) {
				return getScoreInfo(EntityUtils.toString(entity));
			}

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			// 关闭连接,释放资源
			httpPost.abort();
			L.d("getResponse", "http post abort");
		}
		return null;
	}

	private static ScoreInfo getScoreInfo(String string) {
		Document doc;
		String rex1 = "span#xftj";
		String rex2 = "span#zyzrs";
		String rex3 = "span#pjxfjd";
		String rex4 = "span#xfjdzh";
		//span#zyzrs span#pjxfjd span#xfjdzh
		//String rex2 = 
		doc = Jsoup.parse(string,"gb2312");
		Elements elements1 = doc.select(rex1);
		Elements elements2 = doc.select(rex2);
		Elements elements3 = doc.select(rex3);
		Elements elements4 = doc.select(rex4);

		ScoreInfo scoreInfo = new ScoreInfo();

		scoreInfo.setTopString(elements1.get(0).text());
		scoreInfo.setFootString(elements2.get(0).text()+""+elements3.get(0).text()+""+elements4.get(0).text());

		String table[][] = new String[6][6];
		String tableRes = "div#divNotPs table.datelist tr";
		Elements elements = doc.select(tableRes);
		for(int i=0;i<elements.size()-2;i++){
			Elements tds = elements.get(i).select("td");
			for(int j=0;j<tds.size();j++){
				table[i][j] = tds.get(j).text();
			}
		}
		scoreInfo.setTableString(table);
		return scoreInfo;
	}

	/**
	 * 未通过成绩
	 * @param mHttpClient
	 * @param params
	 * @return
	 */
	public static List<Score> getWtgcjScore(AndroidHttpClient mHttpClient,
			Object[] info) {
		if (null == mHttpClient) {
			return null;
		}

		HttpPost httpPost = null;
		// 使用post 请求的带参数的url
		String targetUrl = "";
		try {
			//
			targetUrl = String.format(URLUtil.JWGL_USER_SCORE_URL, LoginActivity.randomUrl, info[0].toString(), URLEncoder.encode(info[1].toString(), "gb2312"));
			L.d("getHttpResponse", targetUrl);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
			return null;
		}

		// 注意使用编码转换,否则请求参数不正确
		httpPost = new HttpPost(targetUrl);
		// 网站内容跳转,加上引用页的头信息,否则将强制跳转至首页
		httpPost.setHeader("Referer", targetUrl);

		// 查询成绩使用的参数
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("__EVENTTARGET", ""));
		params.add(new BasicNameValuePair("__EVENTARGUMENT", ""));
		params.add(new BasicNameValuePair(
				"__VIEWSTATE",
				getViewState(mHttpClient,targetUrl,info[0].toString())));
		params.add(new BasicNameValuePair("__VIEWSTATEGENERATOR", "9727EB43"));
		params.add(new BasicNameValuePair("hidLanguage", ""));
		params.add(new BasicNameValuePair("ddlXN", ""));
		params.add(new BasicNameValuePair("ddlXQ", ""));
		params.add(new BasicNameValuePair("ddl_kcxz", ""));
		try {
			params.add(new BasicNameValuePair("Button2", URLEncoder.encode("未通过成绩", "GBK")));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		HttpResponse response = null;

		try {
			httpPost.setEntity(new UrlEncodedFormEntity(params, "gb2312"));
			response = mHttpClient.execute(httpPost);

			if (HttpStatus.SC_OK != response.getStatusLine().getStatusCode()) {
				L.d("getHttpResponse", "not ok " + response.getStatusLine());
			}

			HttpEntity entity = response.getEntity();
			if (null != entity) {
				return getWtgScore(EntityUtils.toString(entity));
			}

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			// 关闭连接,释放资源
			httpPost.abort();
			L.d("getResponse", "http post abort");
		}
		return null;
	}

	/**
	 * 未通过成绩
	 * @param string
	 * @return
	 */
	private static List<Score> getWtgScore(String string) {

		Document doc;

		doc = Jsoup.parse(string,"gb2312");
		String rex = "table#Datagrid3 tr";
		List<Score> scores = new ArrayList<Score>();
		Elements eles = doc.select(rex);
		for(int i=1;i<eles.size();i++)
		{
			Elements td = eles.get(i).select("td");
			Score score = new Score();

			score.setCourseCode(td.get(0).text());
			score.setCourseName(td.get(1).text());
			score.setCourseProperty(td.get(2).text());
			score.setXuefen(td.get(3).text());
			score.setZgcjz(td.get(4).text());
			score.setKcgs(td.get(5).text());

			scores.add(score);
		}
		return scores;
	}

	public static List<Score> getZgcjScore(AndroidHttpClient mHttpClient,
			Object[] info) {

		if (null == mHttpClient) {
			return null;
		}

		HttpPost httpPost = null;
		// 使用post 请求的带参数的url
		String targetUrl = "";
		try {
			//
			targetUrl = String.format(URLUtil.JWGL_USER_SCORE_URL, LoginActivity.randomUrl, info[0].toString(), URLEncoder.encode(info[1].toString(), "gb2312"));
			L.d("getHttpResponse", targetUrl);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
			return null;
		}

		// 注意使用编码转换,否则请求参数不正确
		httpPost = new HttpPost(targetUrl);
		// 网站内容跳转,加上引用页的头信息,否则将强制跳转至首页
		httpPost.setHeader("Referer", targetUrl);

		// 查询成绩使用的参数
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("__EVENTTARGET", ""));
		params.add(new BasicNameValuePair("__EVENTARGUMENT", ""));
		params.add(new BasicNameValuePair(
				"__VIEWSTATE",
				getViewState(mHttpClient,targetUrl,info[0].toString())));
		params.add(new BasicNameValuePair("__VIEWSTATEGENERATOR", "9727EB43"));
		params.add(new BasicNameValuePair("hidLanguage", ""));
		params.add(new BasicNameValuePair("ddlXN", ""));
		params.add(new BasicNameValuePair("ddlXQ", ""));
		params.add(new BasicNameValuePair("ddl_kcxz", ""));
		try {
			params.add(new BasicNameValuePair("btn_zg", URLEncoder.encode("课程最高成绩", "GBK")));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		HttpResponse response = null;

		try {
			httpPost.setEntity(new UrlEncodedFormEntity(params, "gb2312"));
			response = mHttpClient.execute(httpPost);

			if (HttpStatus.SC_OK != response.getStatusLine().getStatusCode()) {
				L.d("getHttpResponse", "not ok " + response.getStatusLine());
			}

			HttpEntity entity = response.getEntity();
			if (null != entity) {
				return getZgScore(EntityUtils.toString(entity));
			}

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			// 关闭连接,释放资源
			httpPost.abort();
			L.d("getResponse", "http post abort");
		}
		return null;
	}

	private static List<Score> getZgScore(String string) {
		Document doc;
		doc = Jsoup.parse(string,"gb2312");
		String rex = "table.datelist tr";
		Elements rowElements = doc.select(rex);
		List<Score> scores = new ArrayList<Score>();
		
		for(int i = 0;i<rowElements.size();i++){
			
			Elements eles = rowElements.get(i).select("td");
			Score score = new Score();
			score.setCourseCode(eles.get(0).text());
			score.setCourseName(eles.get(1).text());
			score.setCourseProperty(eles.get(2).text());
			score.setXuefen(eles.get(3).text());
			score.setZgcjz(eles.get(4).text());
			score.setKcgs(eles.get(5).text());
			
			scores.add(score);
		}
		return scores;
	}
}

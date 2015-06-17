package com.chzu.app.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
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
import com.chzu.app.bean.ClassRoom;

/**
 * 
 * @ClassName: EmptyClassRoomUtil 
 * @Description: 空教室查询工具类
 * @date 2015-4-28 上午9:50:01 
 *
 */
public class EmptyClassRoomUtil {

	/**
	 * 获取日期
	 * @param content
	 * @return
	 */
	public static LinkedHashMap<String, String> getYear(String content){
		Document doc;
		doc = Jsoup.parse(content,"gb2312");
		String rex = "select#kssj option";
		Elements eles = doc.select(rex);
		LinkedHashMap<String,String> map = new LinkedHashMap<String,String>();
		for(int i=0;i<eles.size();i++)
		{
			map.put(eles.get(i).text(), eles.get(i).attr("value"));
		}
		return map;
	}
	
	public static List<ClassRoom> getClassRoom(String content){
		Document doc;
		doc = Jsoup.parse(content,"gb2312");
		String rex = "table.datelist tr";
		List<ClassRoom> cRooms = new ArrayList<ClassRoom>();
		Elements eles = doc.select(rex);
		for(int i=1;i<eles.size();i++)
		{
			Elements tEles = eles.get(i).select("td");
			ClassRoom cRoom  = new ClassRoom();
			cRoom.setClassId(tEles.get(0).text());
			cRoom.setClassName(tEles.get(1).text());
			cRoom.setClassType(tEles.get(2).text());
			cRoom.setCampus(tEles.get(3).text());
			cRoom.setSeats(tEles.get(4).text());
			cRoom.setDepartment(tEles.get(5).text());
			cRoom.setExamSeats(tEles.get(6).text());
			cRoom.setSubscribe(tEles.get(7).text());
			cRooms.add(cRoom);
		}
		return cRooms;
	}
	

	/**
	 * 获取页面
	 * @param client
	 * @param info
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static String getResponse(AndroidHttpClient client, String...info) throws UnsupportedEncodingException  {
		if (null == client) {
			return null;
		}

		HttpPost httpPost = null;
		// 使用post 请求的带参数的url
		String targetUrl = "";
		try {
			//
			targetUrl = String.format(URLUtil.JWGL_EMPTY_CLASSROOM, LoginActivity.randomUrl, info[0], URLEncoder.encode(info[1], "gb2312"));
			L.d("getHttpResponse", targetUrl);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
			return null;
		}

		// 注意使用编码转换,否则请求参数不正确
		httpPost = new HttpPost(targetUrl);
		// 网站内容跳转,加上引用页的头信息,否则将强制跳转至首页
		httpPost.setHeader("Referer", targetUrl);
		httpPost.setHeader("Cookie","tabId=ext-comp-1002");

		// 查询成绩使用的参数
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("__EVENTTARGET",""));
		params.add(new BasicNameValuePair("__EVENTARGUMENT",""));
		params.add(new BasicNameValuePair(
				"__VIEWSTATE",
				getViewState(client,targetUrl,info[0].toString())));
		params.add(new BasicNameValuePair("__VIEWSTATEGENERATOR","2C2440D4"));
		params.add(new BasicNameValuePair("xiaoq",info[2]));//校区
		params.add(new BasicNameValuePair("jslb",URLEncoder.encode(info[3], "GBK")));//教室类别
		params.add(new BasicNameValuePair("min_zws",info[4]));//最小座位数
		params.add(new BasicNameValuePair("max_zws",info[5]));//最大座位数
		params.add(new BasicNameValuePair("kssj",info[6]));//开始时间
		params.add(new BasicNameValuePair("jssj",info[6]));//结束时间
		params.add(new BasicNameValuePair("xqj",info[7]));//星期
		params.add(new BasicNameValuePair("ddlDsz",URLEncoder.encode(info[8], "GBK")));//单双周
		params.add(new BasicNameValuePair("sjd",URLEncoder.encode(info[9], "GBK")));//节次
		params.add(new BasicNameValuePair("Button2",URLEncoder.encode("空教室查询", "GBK")));//按钮类型
		params.add(new BasicNameValuePair("xn","2014-2015"));//学年
		params.add(new BasicNameValuePair("xq","2"));//学期
		params.add(new BasicNameValuePair("ddlSyXn","2014-2015"));//学年
		params.add(new BasicNameValuePair("ddlSyxq","2"));//学年
		params.add(new BasicNameValuePair("dpDataGrid1:txtChoosePage","1"));//页数
		params.add(new BasicNameValuePair("dpDataGrid1:txtPageSize","100"));//每页显示

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
		//http://210.45.160.115/(pciafx45ansenb55pclmbu45)/xxjsjy.aspx?xh=2011211311&xm=%CD%F5%D0%CB%B3%AC&gnmkdm=N121611
		httpGet.setHeader("Referer", String.format(URLUtil.JWGL_MAIN_URL, LoginActivity.randomUrl, xh));
		httpGet.setHeader("Cookie","tabId=ext-comp-1002");
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

}

/**
 * 
 */
package com.chzu.app.util;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.net.http.AndroidHttpClient;
import android.util.Log;

import com.chzu.app.activity.LoginActivity;
import com.chzu.app.bean.User;
import com.google.gson.Gson;

/**
 * 
 * @ClassName: UserUtils 
 * @Description: 用户工具类
 * @date 2015-4-18 下午12:11:43 
 *
 */
public class UserUtils {
	/**
	 * @param client
	 *            android http client
	 * @param info
	 *            参数1:学号 ;参数2:姓名
	 * @return
	 */
	public static User getUser(AndroidHttpClient client, String... info) {
		String response = HttpUtils.getHttpResponse(client, URLUtil.JWGL_USER_INFO_URL, info);
		if (null == response) {
			return null;
		} else {
			// System.out.println(response);

			return parse2User(response);
		}
	}

	/**
	 * 根据输入的内容来生成一个User对象
	 * 
	 * @param content
	 *            输入内容,从用户登陆后的页面的用户信息页面下获取内容
	 * @return 如果正常登陆的内容返回一个User,否则返回null
	 */
	private static User parse2User(String content) {
		if (null == content) {
			return null;
		}

		User user = new User();
		Document document = Jsoup.parse(content);

		user.setName(document.getElementById("xm").text());
		user.setPassword("");
		user.setStudentId(document.getElementById("xh").text());
		/**曾用名*/
		user.setFormerName(document.getElementById("zym").attr("value"));
		/**性别*/
		user.setGender(document.getElementById("lbl_xb").text());
		/**民族*/
		user.setNation(document.getElementById("lbl_mz").text());
		/**籍贯*/
		user.setNativePlace(document.getElementById("txtjg").attr("value"));
		/**政治面貌*/
		user.setPoliticalStatus(document.getElementById("zzmm").select("option").get(0).text());
		/**学院*/
		user.setAcademy(document.getElementById("lbl_xy").text());
		/**入学年份*/
		user.setJoinSchool(document.getElementById("lbl_dqszj").text());
		/**专业名称*/
		user.setSpecialty(document.getElementById("lbl_zymc").text());
		/**学年制*/
		user.setExecutiveCourses(document.getElementById("lbl_xz").text());
		/**行政班*/
		user.setExecutiveCourses(document.getElementById("lbl_xzb").text());
		/**学制*/
		user.setAcademicYear(document.getElementById("lbl_xz").text());
		/**准考证号*/
		user.setExaminationNumber(document.getElementById("lbl_zkzh").text());
		/**身份证号*/
		user.setID(document.getElementById("lbl_sfzh").text());

		/**来源地区*/
		user.setAddress(document.getElementById("lbl_lydq").text());
		/**毕业学校*/
		user.setBySchool(document.getElementById("byzx").attr("value"));
		/**宿舍号*/
		user.setShh(document.getElementById("ssh").attr("value"));
		/**联系电话*/
		user.setPhone(document.getElementById("lxdh").attr("value"));
		/**学历层次*/
		user.setXl(document.getElementById("lbl_CC").text());
		/**父亲姓名*/
		user.setfName(document.getElementById("fqxm").attr("value"));
		/**父亲单位*/
		user.setfWork(document.getElementById("fqdw").attr("value"));
		/**母亲姓名*/
		user.setmName(document.getElementById("mqxm").attr("value"));
		/**母亲单位*/
		user.setmWork(document.getElementById("mqdw").attr("value"));
		/**父亲电话*/
		user.setfPhone(document.getElementById("lbl_fqdwdh").text());
		/**母亲电话*/
		user.setmPhone(document.getElementById("lbl_mqdwdh").text());
		/** 头像地址*/
		user.setHeadIcon(String.format(URLUtil.JWGL_HEAD_ICON_URL, LoginActivity.randomUrl, user.getStudentId()));

		return user;
	}

	/**
	 * 获取输入文本内容的学号,用户名
	 * 
	 * @param content
	 * @return 第一个结果:学号 , 第二个结果用户名
	 */
	public static String[] getUserInfo(String content) {

		if (null != content) {
			String[] result = Jsoup.parse(content).getElementById("xhxm").text().split(" ");

			// 返回结果不能为空,且分割后为两个字符串:学号+姓名
			if (null != result && 2 == result.length) {
				result[1] = result[1].replace("同学", "").trim();
				return result;
			}
		}
		return null;
	}

	//not hstc
	public static String getUserName(String content) {
		return Jsoup.parse(content).getElementById("xhxm").text();
	}

	public static boolean updateHeadIcon(User user, File file) {

		return true;
	}

	/**
	 * 
	 * 描述 向后台发送user数据
	 * @param user
	 */
	public static void SaveDataToPhp(User user){
		Gson gson = new Gson();
		String url = "http://bmhjqs.sinaapp.com/ChzuAppDate/chzu_user_save.php";  
		HttpPost httpRequest = new HttpPost(url);  

		List<NameValuePair> params = new ArrayList<NameValuePair>();  
		params.add(new BasicNameValuePair("userJson", gson.toJson(user)));  
		try {  
			HttpEntity httpEntity = new UrlEncodedFormEntity(params,"utf-8");  
			httpRequest.setEntity(httpEntity);  
			HttpClient httpClient = new DefaultHttpClient();  
			HttpResponse httpResponse = httpClient.execute(httpRequest);  
			if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){  
				String result = EntityUtils.toString(httpResponse.getEntity());  
				Log.i("save", result);
			}else{  
			}  
		} catch (UnsupportedEncodingException e) {  
			e.printStackTrace();  
		} catch (ClientProtocolException e) {  
			e.printStackTrace();  
		} catch (IOException e) {  
			e.printStackTrace();  
		}  
	}

}

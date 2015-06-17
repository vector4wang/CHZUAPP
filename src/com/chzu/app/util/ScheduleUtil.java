package com.chzu.app.util;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.chzu.app.bean.Course;
import com.chzu.app.bean.DayEnum;
import com.chzu.app.bean.TimeEnum;

/**
 * 
 * @ClassName: ScheduleUtil 
 * @Description: 课表工具类
 * @date 2015-4-18 下午12:12:39 
 *
 */
public class ScheduleUtil {

	//获取年份
	public static ArrayList<String> getYear(String content){
		ArrayList<String> data = new ArrayList<String>();
		if(null != content){
			String[] resultString = Jsoup.parse(content).getElementById("xnd").text().split(" ");
			for(int i=0;i<resultString.length;i++){
				data.add(resultString[i]);
			}
			return data;
		}
		return null;
	}
	//获取学期
	public static ArrayList<String> getTerm(String content){
		ArrayList<String> data = new ArrayList<String>();
		if(null != content){
			String[] resultString = Jsoup.parse(content).getElementById("xqd").text().split(" ");
			for(int i=0;i<resultString.length;i++){
				data.add(resultString[i]);
			}
			return data;
		}
		return null;
	}

	public static List<Course> getCourse(String content){
		if(null == content){
			return null;
		}
		Document doc;
		List<Course> courses = new ArrayList<Course>();
		try {
			doc = Jsoup.parse(content);
			Elements elements = doc.select("table#Table1");
			Elements trElements = elements.get(0).select("tr");
			Elements td1 = trElements.get(0).select("td");
			// i=1为空跳过
			//i=2和i=3为上午第一二节课，合二为一
			Elements td2 = trElements.get(2).select("td");
			for(int i=2; i<td2.size();i++){
				Course course = new Course(td2.get(i).text(), DayEnum.getDay(i-1), TimeEnum.AMF);
				courses.add(course);
			}
			//i=4和i=5为上午三四节课，合二为一
			Elements td3 = trElements.get(4).select("td");
			for(int i=1; i<td3.size();i++){
				Course course = new Course(td3.get(i).text(), DayEnum.getDay(i), TimeEnum.AMS);
				courses.add(course);
			}
			//i=6和i=7为下午第一节课
			Elements td4 = trElements.get(6).select("td");
			for(int i=2; i<td4.size();i++){
				Course course = new Course(td4.get(i).text(), DayEnum.getDay(i-1), TimeEnum.PMF);
				courses.add(course);
			}
			//i=8和i=9为下午第二节课
			Elements td5 = trElements.get(8).select("td");
			for(int i=1; i<td5.size();i++){
				Course course = new Course(td5.get(i).text(), DayEnum.getDay(i), TimeEnum.PMS);
				courses.add(course);
			}
			//i=10和i=11为晚自习
			Elements td6 = trElements.get(10).select("td");
			for(int i=2; i<td6.size();i++){
				Course course = new Course(td6.get(i).text(), DayEnum.getDay(i-1), TimeEnum.YES);
				courses.add(course);
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return courses;
	}
}

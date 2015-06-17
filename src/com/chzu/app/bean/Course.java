package com.chzu.app.bean;

/**
 *  
 * @author wangxingchao    
 * @version 1.0  
 * @created 2015-5-26 下午4:01:24
 */
public class Course extends BaseBean{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3913151583606713954L;
	public Course(String content, DayEnum cDay, TimeEnum cTime) {
		this.content = content;
		this.cDay = cDay;
		this.cTime = cTime;
	}
	private String content;//�γ�����
	private DayEnum cDay;//�ܼ�
	private TimeEnum cTime;//�ڼ��ڿ�
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public DayEnum getcDay() {
		return cDay;
	}
	public void setcDay(DayEnum cDay) {
		this.cDay = cDay;
	}
	public TimeEnum getcTime() {
		return cTime;
	}
	public void setcTime(TimeEnum cTime) {
		this.cTime = cTime;
	}
	@Override
	public String toString() {
		return "Course["+cDay+","+cTime+","+content+"]";
	}
}

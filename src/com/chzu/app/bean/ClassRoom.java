package com.chzu.app.bean;

/**
 *  
 * @author wangxingchao    
 * @version 1.0  
 * @created 2015-5-26 下午4:01:18
 */
public class ClassRoom extends BaseBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3001109404225109042L;
	
	/**
	 * 教室编号
	 */
	private String classId;
	
	/**
	 * 教室名称
	 */
	private String className;
	
	/**
	 * 教室类别
	 */
	private String classType;
	
	/**
	 * 校区
	 */
	private String campus;
	
	/**
	 * 上课座位数
	 */
	private String seats;
	
	/**
	 * 使用部门
	 */
	private String department;
	
	/**
	 * 考试座位数
	 */
	private String examSeats;
	
	/**
	 * 预约情况
	 */
	private String subscribe;

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getClassType() {
		return classType;
	}

	public void setClassType(String classType) {
		this.classType = classType;
	}

	public String getCampus() {
		return campus;
	}

	public void setCampus(String campus) {
		this.campus = campus;
	}

	public String getSeats() {
		return seats;
	}

	public void setSeats(String seats) {
		this.seats = seats;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getExamSeats() {
		return examSeats;
	}

	public void setExamSeats(String examSeats) {
		this.examSeats = examSeats;
	}

	public String getSubscribe() {
		return subscribe;
	}

	public void setSubscribe(String subscribe) {
		this.subscribe = subscribe;
	}
}

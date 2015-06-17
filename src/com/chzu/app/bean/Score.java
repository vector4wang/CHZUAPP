/**
 * 
 */
package com.chzu.app.bean;

/**
 *  
 * @author wangxingchao    
 * @version 1.0  
 * @created 2015-5-26 下午4:02:35
 */
public class Score extends BaseBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4173476399693824258L;
	/** 学年 */
	String academicYear;
	/** 学期 */
	String semester;
	/** 课程代码 */
	String courseCode;
	/** 课程名称 */
	String courseName;
	/** 课程性质 */
	String courseProperty;
	/** 课程归属 */
	String courseBelong;
	/** 学分 */
	String xuefen;
	/** 绩点 */
	String jidian;
	/** 平时成绩*/
	String pscj;
	/** 成绩 */
	String score;
	/** 辅修标记 */
	String fxbj;
	/** 补考成绩 */
	String bkcj;
	/** 重修成绩 */
	String cxcj;
	/** 开课学院 */
	String beginCollege;
	/** 备注 */
	String comment;
	/** 重修标记 */
	String cxbj;

	/**
	 * 用于未通过成绩的属性
	 * @return
	 */
	/**最高成绩值*/
	private String zgcjz;
	
	/**课程归属*/
	private String kcgs;
	
	public String getAcademicYear() {
		return academicYear;
	}

	public void setAcademicYear(String academicYear) {
		this.academicYear = academicYear;
	}

	public String getSemester() {
		return semester;
	}

	public void setSemester(String semester) {
		this.semester = semester;
	}

	public String getCourseCode() {
		return courseCode;
	}

	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getCourseProperty() {
		return courseProperty;
	}

	public void setCourseProperty(String courseProperty) {
		this.courseProperty = courseProperty;
	}

	public String getCourseBelong() {
		return courseBelong;
	}

	public void setCourseBelong(String courseBelong) {
		this.courseBelong = courseBelong;
	}

	public String getXuefen() {
		return xuefen;
	}

	public void setXuefen(String xuefen) {
		this.xuefen = xuefen;
	}

	public String getJidian() {
		return jidian;
	}

	public void setJidian(String jidian) {
		this.jidian = jidian;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getFxbj() {
		return fxbj;
	}

	public void setFxbj(String fxbj) {
		this.fxbj = fxbj;
	}

	public String getBkcj() {
		return bkcj;
	}

	public void setBkcj(String bkcj) {
		this.bkcj = bkcj;
	}

	public String getCxcj() {
		return cxcj;
	}

	public void setCxcj(String cxcj) {
		this.cxcj = cxcj;
	}

	public String getBeginCollege() {
		return beginCollege;
	}

	public void setBeginCollege(String beginCollege) {
		this.beginCollege = beginCollege;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getCxbj() {
		return cxbj;
	}

	public void setCxbj(String cxbj) {
		this.cxbj = cxbj;
	}

	public String getZgcjz() {
		return zgcjz;
	}

	public void setZgcjz(String zgcjz) {
		this.zgcjz = zgcjz;
	}

	public String getKcgs() {
		return kcgs;
	}

	public void setKcgs(String kcgs) {
		this.kcgs = kcgs;
	}

	@Override
	public String toString() {
		return "Score [academicYear=" + academicYear + ", semester=" + semester + ", courseCode=" + courseCode + ", courseName=" + courseName
				+ ", courseProperty=" + courseProperty + ", courseBelong=" + courseBelong + ", xuefen=" + xuefen + ", jidian=" + jidian
				+ ", score=" + score + ", fxbj=" + fxbj + ", bkcj=" + bkcj + ", cxcj=" + cxcj + ", beginCollege=" + beginCollege + ", comment="
				+ comment + ", cxbj=" + cxbj + "]";
	}
}

/**
 * 
 */
package com.chzu.app.bean;


/**
 *  
 * @author wangxingchao    
 * @version 1.0  
 * @created 2015-5-26 下午4:03:00
 */
public class User extends BaseBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4326462634943330134L;

	/** 学号 */
	String studentId;
	/** 密码 */
	String password;
	/** 姓名 */
	String name;
	/** 头像 */
	String headIcon;
	/** 曾用名 */
	String formerName;
	/** 性别 */
	String gender;
	/** 民族 */
	String nation;
	/** 籍贯 */
	String nativePlace;
	/** 政治面貌 */
	String politicalStatus;
	/** 学院 */
	String academy;
	/**入学年份*/
	String joinSchool;
	/** 专业名称 */
	String specialty;
	/** 行政班 */
	String executiveCourses;
	/** 学制 */
	String academicYear;
	/** 身份证号 */
	String ID;
	/** 准考证号 */
	String examinationNumber;
	/**来源地区*/
	String address;
	/**毕业学校*/
	String bySchool;
	/**宿舍号*/
	String shh;
	/**联系电话*/
	String phone;
	/**学历层次*/
	String xl;
	/**父亲姓名*/
	String fName;
	/**父亲单位*/
	String fWork;
	/**母亲姓名*/
	String mName;
	/**母亲单位*/
	String mWork;
	/**父亲电话*/
	String fPhone;
	/**母亲电话*/
	String mPhone;
	/** 默认空构造器 */
	public User() {

	}

	public User(String id, String name) {
		this.studentId = id;
		this.name = name;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHeadIcon() {
		return headIcon;
	}

	public void setHeadIcon(String headIcon) {
		this.headIcon = headIcon;
	}

	public String getFormerName() {
		return formerName;
	}

	public void setFormerName(String formerName) {
		this.formerName = formerName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}


	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public String getNativePlace() {
		return nativePlace;
	}

	public void setNativePlace(String nativePlace) {
		this.nativePlace = nativePlace;
	}

	public String getPoliticalStatus() {
		return politicalStatus;
	}

	/** 政治面貌 */
	public void setPoliticalStatus(String politicalStatus) {
		this.politicalStatus = politicalStatus;
	}

	public String getAcademy() {
		return academy;
	}

	public void setAcademy(String academy) {
		this.academy = academy;
	}

	public String getJoinSchool() {
		return joinSchool;
	}

	public void setJoinSchool(String joinSchool) {
		this.joinSchool = joinSchool;
	}


	public String getSpecialty() {
		return specialty;
	}

	public void setSpecialty(String specialty) {
		this.specialty = specialty;
	}

	public String getExecutiveCourses() {
		return executiveCourses;
	}

	/** 行政班 */
	public void setExecutiveCourses(String executiveCourses) {
		this.executiveCourses = executiveCourses;
	}

	public String getAcademicYear() {
		return academicYear;
	}

	/** 学制 */
	public void setAcademicYear(String academicYear) {
		this.academicYear = academicYear;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getExaminationNumber() {
		return examinationNumber;
	}

	/** 准考证号 */
	public void setExaminationNumber(String examinationNumber) {
		this.examinationNumber = examinationNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getBySchool() {
		return bySchool;
	}

	public void setBySchool(String bySchool) {
		this.bySchool = bySchool;
	}

	public String getShh() {
		return shh;
	}

	public void setShh(String shh) {
		this.shh = shh;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getXl() {
		return xl;
	}

	public void setXl(String xl) {
		this.xl = xl;
	}

	public String getfName() {
		return fName;
	}

	public void setfName(String fName) {
		this.fName = fName;
	}

	public String getfWork() {
		return fWork;
	}

	public void setfWork(String fWork) {
		this.fWork = fWork;
	}

	public String getmName() {
		return mName;
	}

	public void setmName(String mName) {
		this.mName = mName;
	}

	public String getmWork() {
		return mWork;
	}

	public void setmWork(String mWork) {
		this.mWork = mWork;
	}

	public String getfPhone() {
		return fPhone;
	}

	public void setfPhone(String fPhone) {
		this.fPhone = fPhone;
	}

	public String getmPhone() {
		return mPhone;
	}

	public void setmPhone(String mPhone) {
		this.mPhone = mPhone;
	}

	@Override
	public String toString() {
		return "User [studentId=" + studentId + ", password=" + password
				+ ", name=" + name + ", headIcon=" + headIcon + ", formerName="
				+ formerName + ", gender=" + gender + ", nation=" + nation
				+ ", nativePlace=" + nativePlace + ", politicalStatus="
				+ politicalStatus + ", academy=" + academy + ", joinSchool="
				+ joinSchool + ", specialty=" + specialty
				+ ", executiveCourses=" + executiveCourses + ", academicYear="
				+ academicYear + ", ID=" + ID + ", examinationNumber="
				+ examinationNumber + ", address=" + address + ", bySchool="
				+ bySchool + ", shh=" + shh + ", phone=" + phone + ", xl=" + xl
				+ ", fName=" + fName + ", fWork=" + fWork + ", mName=" + mName
				+ ", mWork=" + mWork + ", fPhone=" + fPhone + ", mPhone="
				+ mPhone + "]";
	}
}

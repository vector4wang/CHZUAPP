package com.chzu.app.bean;

/**
 *   
 *  
 * @author wangxingchao    
 * @version 1.0  
 * @created 2015-5-26 下午4:01:29
 */
public enum DayEnum {
	Sunday(7),
	Monday(1),
	Tuesday(2),
	Wednesday(3),
	Thursday(4),
	Friday(5),
	Saturday(6);

	private int nCode;

	public int getnCode() {
		return nCode;
	}

	private DayEnum(int _nCode) {
		this.nCode = _nCode;
	}

	public static DayEnum getDay(int value) {
		for (DayEnum temp : DayEnum.values()) {
			if (value == temp.getnCode()) {
				return temp;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return String.valueOf (this.nCode );
	} 
}

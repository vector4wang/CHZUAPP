package com.chzu.app.bean;

/**
 *  
 * @author wangxingchao    
 * @version 1.0  
 * @created 2015-5-26 下午4:02:53
 */
public enum TimeEnum {
	AMF(1),
	AMS(2),
	PMF(3),
	PMS(4),
	YES(5);

	private int nCode;

	private TimeEnum(int _nCode) {
		this . nCode = _nCode;
	}
	@Override
	public String toString() {
		return String.valueOf (this . nCode );

	} 
}

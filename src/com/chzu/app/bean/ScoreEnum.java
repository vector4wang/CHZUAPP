package com.chzu.app.bean;

/**
 *  
 * @author wangxingchao    
 * @version 1.0  
 * @created 2015-5-26 下午4:02:41
 */
public enum ScoreEnum {
	/**
	 * 成绩统计
	 */
	CJTJ(0),
	
	/**
	 * 未通过成绩
	 */
	WTGJCJ(1),
	
	/**
	 * 历年成绩
	 */
	LNCHJ(2),
	
	/**
	 * 成绩查询
	 */
	ZGCJ(3);
	
	private int nCode;
	
	public int getnCode() {
		return nCode;
	}

	private ScoreEnum(int nCode){
		this.nCode = nCode;
	}
	public static ScoreEnum getScoreEnum(int value) {
		for (ScoreEnum temp : ScoreEnum.values()) {
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

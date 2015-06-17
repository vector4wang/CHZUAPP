package com.chzu.app.bean;


/**
 *  
 * @author wangxingchao    
 * @version 1.0  
 * @created 2015-5-26 下午4:02:47
 */
public class ScoreInfo extends BaseBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8928317165877885582L;
	
	//所选学分163；获得学分161；重修学分0；正考未通过学分 2。
	private String topString;
	
	private String[][] tableString;
	
	//本专业共175人 	平均学分绩点：3.02 	学分绩点总和：492.55
	private String footString;

	public String getTopString() {
		return topString;
	}

	public void setTopString(String topString) {
		this.topString = topString;
	}

	public String[][] getTableString() {
		return tableString;
	}

	public void setTableString(String[][] tableString) {
		this.tableString = tableString;
	}

	public String getFootString() {
		return footString;
	}

	public void setFootString(String footString) {
		this.footString = footString;
	}
}

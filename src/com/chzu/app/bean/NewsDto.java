package com.chzu.app.bean;

import java.util.List;
/**
 *  
 * @author wangxingchao    
 * @version 1.0  
 * @created 2015-5-26 下午4:01:39
 */
public class NewsDto {
	private List<NewsDetail> newses;

	public List<NewsDetail> getNewses() {
		return newses;
	}

	public void setNewses(List<NewsDetail> newses) {
		this.newses = newses;
	}   
}

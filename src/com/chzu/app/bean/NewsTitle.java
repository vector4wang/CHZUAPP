package com.chzu.app.bean;
/**
 *  
 * @author wangxingchao    
 * @version 1.0  
 * @created 2015-5-26 下午4:02:15
 */
public class NewsTitle extends BaseBean{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6920918128477773590L;

	/**
	 * 标题
	 */
	private String title;
	
	/**
	 * 发布日期
	 */
	private String pDate;
	
	/**
	 * 浏览次数
	 */
	private int amount;
	
	/**
	 * 链接地址
	 */
	private String link;
	
	/**
	 *类型 
	 */
	private Integer newsType;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	

	public String getpDate() {
		return pDate;
	}

	public void setpDate(String pDate) {
		this.pDate = pDate;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public Integer getNewsType() {
		return newsType;
	}

	public void setNewsType(Integer newsType) {
		this.newsType = newsType;
	}

	@Override
	public String toString()
	{
		return "News [title=" + title + ", Link=" + link + ", publishTime=" + pDate + "]";
	}
}

package com.chzu.app.util;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.chzu.app.bean.NewsDetail;
import com.chzu.app.bean.NewsDetail.NewsType;
import com.chzu.app.bean.NewsDto;
import com.chzu.app.bean.NewsTitle;

public class NewsDetailUtil {
	
	
	public List<NewsTitle> getNewsList(int newsType) throws Exception{
		
		String html = NewsUtil.doGet(URLUtil.gengrateURL(newsType));
		Document doc = Jsoup.parse(html, "UTF-8");
		List<NewsTitle> list = new ArrayList<NewsTitle>();
		NewsTitle newsTitle = null;
		Elements ListDiv = doc.getElementsByClass("columnStyle");
		
		for(int i=0;i<ListDiv.size();i++){
			newsTitle = new NewsTitle();
			//新闻类型
			newsTitle.setNewsType(newsType);
			Element column_ele = ListDiv.get(i);
			Element td_ele = column_ele.getElementsByTag("a").get(0);
			//标题
			newsTitle.setTitle(td_ele.text().trim());
			//连接
			newsTitle.setLink("http://www.chzu.edu.cn"+td_ele.attr("href"));
			Element time_ele = column_ele.getElementsByClass("postTime").get(0);
			//出版时间
			newsTitle.setpDate(time_ele.text());
			list.add(newsTitle);
		}
		return list;
	}
	
	/**
	 * 返回网页内容
	 * @param newsUrl
	 * @return
	 * @throws Exception
	 */
	public NewsDto getNewsDetail(String newsUrl) throws Exception{
		String html = NewsUtil.doGet(newsUrl);
		Document doc = Jsoup.parse(html,"UTF-8");
		NewsDto newsDto = new NewsDto();
		List<NewsDetail> newes = new ArrayList<NewsDetail>();
		NewsDetail newsDetail = null;
		Element title = doc.getElementsByClass("boaoti3").get(0);
		//标题
		newsDetail = new NewsDetail();
		newsDetail.setTitle(title.text());
		newsDetail.setType(NewsType.TITLE);
		newes.add(newsDetail);
		
		Element main = doc.getElementById("main");
		Element infotit = main.getElementById("infotit");
		Element publish = infotit.getElementsByTag("table").get(3);
		//出处
		newsDetail = new NewsDetail();
		newsDetail.setPublish(publish.text());
		newes.add(newsDetail);
		
		Elements cons = main.getElementsByTag("p");
		for(Element con : cons){
			
			Elements imgs = con.getElementsByTag("img");
			if(imgs.size() > 0){
				for(Element img : imgs){
					String imageLink = "http://www.chzu.edu.cn"+img.attr("src");
					newsDetail = new NewsDetail();
					newsDetail.setImageLink(imageLink);
					newes.add(newsDetail);
				}
			}
			
			newsDetail = new NewsDetail();
			newsDetail.setType(NewsType.CONTENT);
			//加粗标题
			Elements bold = con.getElementsByTag("strong");
			if(bold.size()>0){
				newsDetail.setType(NewsType.BOLD_TITLE);
			}
			newsDetail.setContent(con.outerHtml());
			newes.add(newsDetail);
		}
		newsDto.setNewses(newes);
		return newsDto;
	}
	public static void main(String[] args) {
		NewsDetailUtil util = new NewsDetailUtil();
		try {
			List<NewsTitle> list= util.getNewsList(0);
			for(NewsTitle temp : list){
				System.out.println(temp.toString());
			}
			
		} catch (Exception e) {
			System.out.println("出错");
		}
	}
}

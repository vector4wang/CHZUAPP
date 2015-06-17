package com.chzu.app.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chzu.app.activity.R;
import com.chzu.app.bean.NewsTitle;
/**
 *  
 * @author wangxingchao    
 * @version 1.0  
 * @created 2015-5-26 下午3:57:19
 */
public class NewsListAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<NewsTitle> mDatas;
	
	public NewsListAdapter(Context context, List<NewsTitle> datas){
		this.mDatas = datas;
		mInflater = LayoutInflater.from(context);
		
	}
	
	public void addAll(List<NewsTitle> mDatas){
		this.mDatas.addAll(mDatas);
	}
	
	public void setDatas(List<NewsTitle> mDatas){
		this.mDatas.clear();
		this.mDatas.addAll(mDatas);
	}
	
	@Override
	public int getCount() {
		return mDatas.size();
	}

	@Override
	public Object getItem(int position) {
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.news_item_temp, null);
			holder = new ViewHolder();
			
			holder.mTitle = (TextView) convertView.findViewById(R.id.id_title);
			holder.mContent = (TextView) convertView.findViewById(R.id.id_pDate);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		NewsTitle newsDetail = mDatas.get(position);
		holder.mTitle.setText(newsDetail.getTitle());
		holder.mContent.setText("发布时间:"+newsDetail.getpDate());
		return convertView;
	}
	
	private final class ViewHolder
	{
		TextView mTitle;
		TextView mContent;
	}

}

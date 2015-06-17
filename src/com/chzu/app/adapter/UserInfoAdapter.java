package com.chzu.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chzu.app.activity.R;
import com.chzu.app.bean.User;

/**
 *  
 * @author wangxingchao    
 * @version 1.0  
 * @created 2015-5-26 下午3:59:08
 */
public class UserInfoAdapter extends BaseAdapter {
	
	private LayoutInflater mInflater;
	private User mUser;
	
	public UserInfoAdapter(Context context, User mUser){
		this.mUser = mUser;
		this.mInflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		return 1;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.layout_login_peruserinfo, null);
			holder = new ViewHolder();
			
			holder.userInfo_key = (TextView) convertView.findViewById(R.id.info_Key);
			holder.userInfo_value = (TextView) convertView.findViewById(R.id.info_Value);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		holder.userInfo_key.setText("");
		holder.userInfo_value.setText(mUser.getName());
		return convertView;
	}
	
	private final class ViewHolder{
		TextView userInfo_key;
		TextView userInfo_value;
	}

}

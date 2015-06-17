package com.chzu.app.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chzu.app.activity.R;

/**
 *  
 * @author wangxingchao    
 * @version 1.0  
 * @created 2015-5-26 下午3:57:01
 */
public class MyItemsAdapter extends BaseAdapter {
	private Context context;
	private List<Map<String,Object>> listItems;
	private LayoutInflater listContainer;

	public MyItemsAdapter(Context context,List<Map<String,Object>> listItems){
		this.context = context;
		listContainer = LayoutInflater.from(context);
		this.listItems = listItems;
	}
	
	public final class ListItemView{
		public TextView value;
	}
	
	@Override
	public int getCount() {
		return listItems.size();
	}

	@Override
	public Object getItem(int position) {
		if(position < listItems.size()){
			return listItems.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ListItemView listItemView = new ListItemView();
		if (convertView == null) {
			convertView = listContainer.inflate(R.layout.frag_empty_room_type_item,
					null);
			listItemView.value = (TextView) convertView
					.findViewById(R.id.id_select_type);

			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}
		listItemView.value.setText((String) listItems.get(position)
				.get("value"));
		return convertView;
	}
}

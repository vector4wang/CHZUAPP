package com.chzu.app.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 *  
 * @author wangxingchao    
 * @version 1.0  
 * @created 2015-5-26 下午3:57:26
 */
public class TableAdapter extends BaseAdapter {

	private Context context;   
	private List<TableRow> table;  
	public TableAdapter(Context context, List<TableRow> table) {  
		this.context = context;  
		this.table = table;  
	}  
	@Override  
	public int getCount() {  
		return table.size();  
	}  
	@Override  
	public long getItemId(int position) {  
		return position;  
	}  
	public TableRow getItem(int position) {  
		return table.get(position);  
	}  
	public View getView(int position, View convertView, ViewGroup parent) {  
		TableRow tableRow = table.get(position);  
		return new TableRowView(this.context, tableRow);  
	}  
	class TableRowView extends LinearLayout {  
		public TableRowView(Context context, TableRow tableRow) {  
			super(context);  

			this.setOrientation(LinearLayout.HORIZONTAL);  
			for (int i = 0; i < tableRow.getSize(); i++) {//行数
				TableCell tableCell = tableRow.getCellValue(i);  
				LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(  
						tableCell.width, tableCell.height);//高和宽
				layoutParams.setMargins(1, 1, 1, 1);//边距
				if (tableCell.type == TableCell.STRING) {//字符类型  
					TextView textCell = new TextView(context);  
					textCell.setGravity(Gravity.CENTER);  
					textCell.setTextSize(14);
					textCell.setBackgroundColor(Color.WHITE);//颜色  
					textCell.setText(String.valueOf(tableCell.value));  
					textCell.setTextColor(Color.BLACK);
					addView(textCell, layoutParams);  
				} else if (tableCell.type == TableCell.IMAGE) {//图片
					ImageView imgCell = new ImageView(context);  
					imgCell.setBackgroundColor(Color.BLACK);  
					imgCell.setImageResource((Integer) tableCell.value);  
					addView(imgCell, layoutParams);  
				} else if(tableCell.type == TableCell.TITLE){
					TextView textCell = new TextView(context);  
					textCell.setTextSize(18);//字体大小
					textCell.setTextColor(Color.WHITE);
					textCell.setGravity(Gravity.CENTER);  
					textCell.setBackgroundColor(Color.rgb(92, 172, 238));//颜色
					textCell.setText(String.valueOf(tableCell.value));  
					addView(textCell, layoutParams);  
				}
			}  
			this.setBackgroundColor(Color.GRAY);//背景颜色
		}  
	}  
	static public class TableRow {  
		private TableCell[] cell;  
		public TableRow(TableCell[] cell) {  
			this.cell = cell;  
		}  
		public int getSize() {  
			return cell.length;  
		}  
		public TableCell getCellValue(int index) {  
			if (index >= cell.length)  
				return null;  
			return cell[index];  
		}  
	}  
	static public class TableCell {  
		static public final int STRING = 0;  
		static public final int IMAGE = 1;
		static public final int TITLE = 2;
		public Object value;  
		public int width;  
		public int height;  
		private int type;  
		public TableCell(Object value, int width, int height, int type) {  
			this.value = value;  
			this.width = width;  
			this.height = height;  
			this.type = type;  
		}  
	}  

}

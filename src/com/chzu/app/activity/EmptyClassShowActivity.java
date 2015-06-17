package com.chzu.app.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;

import com.chzu.app.adapter.TableAdapter;
import com.chzu.app.adapter.TableAdapter.TableCell;
import com.chzu.app.adapter.TableAdapter.TableRow;
import com.chzu.app.bean.ClassRoom;

/**
 *  
 * @author wangxingchao    
 * @version 1.0  
 * @created 2015-5-26 下午3:54:45
 */
public class EmptyClassShowActivity extends Activity{

	String[] heads = {"教室编号","教室名称","教室类别","校区","上课座位数","使用部门","考试座位数","预约情况"};

	private ListView title;
	private ListView ec_listView;

	private final static int MARGIN_VALUE = 20;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.frag_empty_room_show);
		title = (ListView) findViewById(R.id.title);
		ec_listView = (ListView) findViewById(R.id.ec_listView);

		showTitle();
		showEmptyClassRoom();

	}

	private void showTitle() {
		ArrayList<TableRow> table = new ArrayList<TableRow>(); 
		TableCell[] titles = new TableCell[heads.length];// 每行5个单元  
		WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth()/4;
		// 定义标题  
		for (int i = 0; i < heads.length; i++) {  
			titles[i] = new TableCell(heads[i],   
					width + MARGIN_VALUE * i,  
					LayoutParams.MATCH_PARENT,   
					TableCell.TITLE);  
		}  
		table.add(new TableRow(titles));
		
		TableAdapter tableAdapter = new TableAdapter(getApplicationContext(),table);
		title.setAdapter(tableAdapter);
	}

	protected void showEmptyClassRoom() {
		ArrayList<TableRow> table = new ArrayList<TableRow>(); 

		WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth()/4;
		Intent intent = getIntent();
		List<ClassRoom> cRooms = (List<ClassRoom>)intent.getSerializableExtra("crooms");
		//每行的数据  
		for(int i=0 ;i<cRooms.size();i++){
			TableCell[] cells = new TableCell[heads.length];

			cells[0] = new TableCell(cRooms.get(i).getClassId(), width + MARGIN_VALUE * 0, LayoutParams.MATCH_PARENT, TableCell.STRING);
			cells[1] = new TableCell(cRooms.get(i).getClassName(), width + MARGIN_VALUE * 1, LayoutParams.MATCH_PARENT, TableCell.STRING);
			cells[2] = new TableCell(cRooms.get(i).getClassType(), width + MARGIN_VALUE * 2, LayoutParams.MATCH_PARENT, TableCell.STRING);
			cells[3] = new TableCell(cRooms.get(i).getCampus(), width + MARGIN_VALUE * 3, LayoutParams.MATCH_PARENT, TableCell.STRING);
			cells[4] = new TableCell(cRooms.get(i).getSeats(), width + MARGIN_VALUE * 4, LayoutParams.MATCH_PARENT, TableCell.STRING);
			cells[5] = new TableCell(cRooms.get(i).getDepartment(), width + MARGIN_VALUE * 5, LayoutParams.MATCH_PARENT, TableCell.STRING);
			cells[6] = new TableCell(cRooms.get(i).getExamSeats(), width + MARGIN_VALUE * 6, LayoutParams.MATCH_PARENT, TableCell.STRING);
			cells[7] = new TableCell(cRooms.get(i).getSubscribe(), width + MARGIN_VALUE * 7, LayoutParams.MATCH_PARENT, TableCell.STRING);

			table.add(new TableRow(cells));
		}
		TableAdapter tableAdapter = new TableAdapter(getApplicationContext(),table);
		ec_listView.setAdapter(tableAdapter);
	}
}

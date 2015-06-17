package com.chzu.app.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.chzu.app.adapter.TableAdapter;
import com.chzu.app.adapter.TableAdapter.TableCell;
import com.chzu.app.adapter.TableAdapter.TableRow;
import com.chzu.app.bean.Score;
import com.chzu.app.bean.ScoreInfo;

/**
 *  
 * @author wangxingchao    
 * @version 1.0  
 * @created 2015-5-26 下午3:56:26
 */
public class ScoreShowActivity extends Activity {

	//用于历年成绩的表头
	private String titleDay[] = {"学年","学期","课程代码","课程名称","课程性质","课程归属","学分","绩点","平时成绩","期中成绩","期末成绩","实验成绩","成绩","辅修标记","补考成绩"};
	//用于成绩统计的表头
	private String titleCjtj[] = {"课程性质名称","学分要求","获得学分","未通过学分","还需学分"};
	//用于未通过成绩表头
	private String titleWtgcj[] = {"课程代码","课程名称","课程性质","学分","最高成绩值","课程归属"};
	
	private final static int MARGIN_VALUE = 20;

	//历年成绩Table
	private ListView lncj_listView;
	private HorizontalScrollView lncj_hsv;
	
	//未通过成绩
	private ListView wtgcj_listView;
	private HorizontalScrollView wtgcj_hsv;

	//成绩统计
	private TextView cjtj_textView1;
	private TextView cjtj_textView2;
	private ListView cjtj_listView;
	private HorizontalScrollView cjtj_hsv;
	
	//最高成绩
	private ListView zgcj_listView;
	private HorizontalScrollView zgch_hsv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		int type = intent.getIntExtra("type", 4);
		//数据渲染
		renderData(type);
	}

	private void renderData(int type) {
		switch (type) {
		case 0:
			//历年成绩
			showLncj();
			break;
		case 1:
			//未通过成绩
			showWtgcj();
			break;
		case 2:
			//成绩统计
			showCjtj();
			break;
		case 3:
			showZgcj();
			break;
		case 4:
			//错误界面
			showErr();
			break;

		default:
			break;
		}
	}

	/**
	 * 错误界面
	 */
	private void showErr() {
		setContentView(R.layout.activity_err);
	}

	/**
	 * 最高成绩
	 */
	private void showZgcj() {
		setContentView(R.layout.layout_score_zgcj);
		zgcj_listView = (ListView) findViewById(R.id.zgcj_listView);
		zgch_hsv = (HorizontalScrollView) findViewById(R.id.zgcj_hsView);
		
		ArrayList<TableRow> table = new ArrayList<TableRow>();  
		TableCell[] titles = new TableCell[titleWtgcj.length];// 每行5个单元  
		WindowManager wm = (WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth()/4;
		// 定义标题  
		for (int i = 0; i < titleWtgcj.length; i++) {  
			titles[i] = new TableCell(titleWtgcj[i],   
					width + MARGIN_VALUE * i,  
					LayoutParams.MATCH_PARENT,   
					TableCell.TITLE);  
		}  
		table.add(new TableRow(titles));

		//获取未通过成绩
		Intent intent = getIntent();
		List<Score> scores = (List<Score>)intent.getSerializableExtra("zgScore");

		//每行的数据  
		for(int i=0 ;i<scores.size();i++){
			TableCell[] cells = new TableCell[titleWtgcj.length];

			cells[0] = new TableCell(scores.get(i).getCourseCode(), width + MARGIN_VALUE * 0, LayoutParams.MATCH_PARENT, TableCell.STRING);
			cells[1] = new TableCell(scores.get(i).getCourseName(), width + MARGIN_VALUE * 1, LayoutParams.MATCH_PARENT, TableCell.STRING);
			cells[2] = new TableCell(scores.get(i).getCourseProperty(), width + MARGIN_VALUE * 2, LayoutParams.MATCH_PARENT, TableCell.STRING);
			cells[3] = new TableCell(scores.get(i).getXuefen(), width + MARGIN_VALUE * 3, LayoutParams.MATCH_PARENT, TableCell.STRING);
			cells[4] = new TableCell(scores.get(i).getZgcjz(), width + MARGIN_VALUE * 4, LayoutParams.MATCH_PARENT, TableCell.STRING);
			cells[5] = new TableCell(scores.get(i).getKcgs(), width + MARGIN_VALUE * 5, LayoutParams.MATCH_PARENT, TableCell.STRING);

			table.add(new TableRow(cells));
		}
		TableAdapter tableAdapter = new TableAdapter(this,table);
		zgcj_listView.setAdapter(tableAdapter);
	}

	private void showCjtj() {
		setContentView(R.layout.layout_score_cjtj);
		
		cjtj_textView1 = (TextView) findViewById(R.id.cjtj_textView1);
		cjtj_textView2 = (TextView) findViewById(R.id.cjtj_textView2);
		cjtj_listView = (ListView)findViewById(R.id.cjtj_listView);
		cjtj_hsv = (HorizontalScrollView)findViewById(R.id.cjtj_hsView);
		
		Intent intent = getIntent();
		ScoreInfo scoreInfo = (ScoreInfo) intent.getSerializableExtra("scoreInfo");
		cjtj_textView1.setText(scoreInfo.getTopString());
		cjtj_textView2.setText(scoreInfo.getFootString());
		
		ArrayList<TableRow> table = new ArrayList<TableRow>();  
		TableCell[] titles = new TableCell[titleCjtj.length];
		WindowManager wm = (WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth()/4;
		// 定义标题  
		for (int i = 0; i < titleCjtj.length; i++) {  
			titles[i] = new TableCell(titleCjtj[i],   
					width + MARGIN_VALUE * i,  
					LayoutParams.MATCH_PARENT,   
					TableCell.TITLE);  
		}  
		table.add(new TableRow(titles));
		
		for(int i=1;i<6;i++){
			TableCell[] cells = new TableCell[titleCjtj.length];
			cells[0] = new TableCell(scoreInfo.getTableString()[i][0], width + MARGIN_VALUE * 0, LayoutParams.MATCH_PARENT, TableCell.STRING);
			cells[1] = new TableCell(scoreInfo.getTableString()[i][1], width + MARGIN_VALUE * 1, LayoutParams.MATCH_PARENT, TableCell.STRING);
			cells[2] = new TableCell(scoreInfo.getTableString()[i][2], width + MARGIN_VALUE * 2, LayoutParams.MATCH_PARENT, TableCell.STRING);
			cells[3] = new TableCell(scoreInfo.getTableString()[i][3], width + MARGIN_VALUE * 3, LayoutParams.MATCH_PARENT, TableCell.STRING);
			cells[4] = new TableCell(scoreInfo.getTableString()[i][4], width + MARGIN_VALUE * 4, LayoutParams.MATCH_PARENT, TableCell.STRING);
			
			table.add(new TableRow(cells));
		}
		TableAdapter tableAdapter = new TableAdapter(this,table);
		cjtj_listView.setAdapter(tableAdapter);
	}

	/**
	 * 未通过成绩
	 */
	private void showWtgcj() {
		setContentView(R.layout.layout_score_wtgcj);
		
		wtgcj_listView = (ListView)findViewById(R.id.wtgcj_listView);
		wtgcj_hsv = (HorizontalScrollView)findViewById(R.id.wtgcj_hsView);
		
		ArrayList<TableRow> table = new ArrayList<TableRow>();  
		TableCell[] titles = new TableCell[titleWtgcj.length];// 每行5个单元  
		WindowManager wm = (WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth()/4;
		// 定义标题  
		for (int i = 0; i < titleWtgcj.length; i++) {  
			titles[i] = new TableCell(titleWtgcj[i],   
					width + MARGIN_VALUE * i,  
					LayoutParams.MATCH_PARENT,   
					TableCell.TITLE);  
		}  
		table.add(new TableRow(titles));

		//获取未通过成绩
		Intent intent = getIntent();
		List<Score> scores = (List<Score>)intent.getSerializableExtra("wtgScore");

		//每行的数据  
		for(int i=0 ;i<scores.size();i++){
			TableCell[] cells = new TableCell[titleWtgcj.length];

			cells[0] = new TableCell(scores.get(i).getCourseCode(), width + MARGIN_VALUE * 0, LayoutParams.MATCH_PARENT, TableCell.STRING);
			cells[1] = new TableCell(scores.get(i).getCourseName(), width + MARGIN_VALUE * 1, LayoutParams.MATCH_PARENT, TableCell.STRING);
			cells[2] = new TableCell(scores.get(i).getCourseProperty(), width + MARGIN_VALUE * 2, LayoutParams.MATCH_PARENT, TableCell.STRING);
			cells[3] = new TableCell(scores.get(i).getXuefen(), width + MARGIN_VALUE * 3, LayoutParams.MATCH_PARENT, TableCell.STRING);
			cells[4] = new TableCell(scores.get(i).getZgcjz(), width + MARGIN_VALUE * 4, LayoutParams.MATCH_PARENT, TableCell.STRING);
			cells[5] = new TableCell(scores.get(i).getKcgs(), width + MARGIN_VALUE * 5, LayoutParams.MATCH_PARENT, TableCell.STRING);

			table.add(new TableRow(cells));
		}
		TableAdapter tableAdapter = new TableAdapter(this,table);
		wtgcj_listView.setAdapter(tableAdapter);
		
		
	}

	@SuppressWarnings("unchecked")
	private void showLncj() {
		setContentView(R.layout.layout_score_lncj);
		
		lncj_listView = (ListView)findViewById(R.id.lncj_listView);
		lncj_hsv = (HorizontalScrollView)findViewById(R.id.lncj_hsView);
		
		ArrayList<TableRow> table = new ArrayList<TableRow>();  
		TableCell[] titles = new TableCell[titleDay.length];// 每行5个单元  
		WindowManager wm = (WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth()/4;
		// 定义标题  
		for (int i = 0; i < titleDay.length; i++) {  
			titles[i] = new TableCell(titleDay[i],   
					width + MARGIN_VALUE * i,  
					LayoutParams.MATCH_PARENT,   
					TableCell.TITLE);  
		}  
		table.add(new TableRow(titles));

		//获取历年成绩
		Intent intent = getIntent();
		List<Score> scores = (List<Score>)intent.getSerializableExtra("scoreList");

		//每行的数据  
		for(int i=0 ;i<scores.size();i++){
			TableCell[] cells = new TableCell[titleDay.length];

			cells[0] = new TableCell(scores.get(i).getAcademicYear(), width + MARGIN_VALUE * 0, LayoutParams.MATCH_PARENT, TableCell.STRING);
			cells[1] = new TableCell(scores.get(i).getSemester(), width + MARGIN_VALUE * 1, LayoutParams.MATCH_PARENT, TableCell.STRING);
			cells[2] = new TableCell(scores.get(i).getCourseCode(), width + MARGIN_VALUE * 2, LayoutParams.MATCH_PARENT, TableCell.STRING);
			cells[3] = new TableCell(scores.get(i).getCourseName(), width + MARGIN_VALUE * 3, LayoutParams.MATCH_PARENT, TableCell.STRING);
			cells[4] = new TableCell(scores.get(i).getCourseProperty(), width + MARGIN_VALUE * 4, LayoutParams.MATCH_PARENT, TableCell.STRING);
			cells[5] = new TableCell(scores.get(i).getCourseBelong(), width + MARGIN_VALUE * 5, LayoutParams.MATCH_PARENT, TableCell.STRING);
			cells[6] = new TableCell(scores.get(i).getXuefen(), width + MARGIN_VALUE * 6, LayoutParams.MATCH_PARENT, TableCell.STRING);
			cells[7] = new TableCell(scores.get(i).getJidian(), width + MARGIN_VALUE * 7, LayoutParams.MATCH_PARENT, TableCell.STRING);
			cells[8] = new TableCell(scores.get(i).getScore(), width + MARGIN_VALUE * 8, LayoutParams.MATCH_PARENT, TableCell.STRING);
			cells[9] = new TableCell(scores.get(i).getFxbj(), width + MARGIN_VALUE * 9, LayoutParams.MATCH_PARENT, TableCell.STRING);
			cells[10] = new TableCell(scores.get(i).getBkcj(), width + MARGIN_VALUE * 10, LayoutParams.MATCH_PARENT, TableCell.STRING);
			cells[11] = new TableCell(scores.get(i).getCxcj(), width + MARGIN_VALUE * 11, LayoutParams.MATCH_PARENT, TableCell.STRING);
			cells[12] = new TableCell(scores.get(i).getBeginCollege(), width + MARGIN_VALUE * 12, LayoutParams.MATCH_PARENT, TableCell.STRING);
			cells[13] = new TableCell(scores.get(i).getComment(), width + MARGIN_VALUE * 13, LayoutParams.WRAP_CONTENT, TableCell.STRING);
			cells[14] = new TableCell(scores.get(i).getCxbj(), width + MARGIN_VALUE * 14, LayoutParams.MATCH_PARENT, TableCell.STRING);

			table.add(new TableRow(cells));
		}
		TableAdapter tableAdapter = new TableAdapter(this,table);
		lncj_listView.setAdapter(tableAdapter);
	}
}

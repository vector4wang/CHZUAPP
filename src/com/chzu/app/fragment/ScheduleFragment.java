package com.chzu.app.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.http.AndroidHttpClient;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chzu.app.activity.PickYearTermActivity;
import com.chzu.app.activity.R;
import com.chzu.app.adapter.TableAdapter;
import com.chzu.app.adapter.TableAdapter.TableCell;
import com.chzu.app.adapter.TableAdapter.TableRow;
import com.chzu.app.app.App;
import com.chzu.app.bean.Course;
import com.chzu.app.util.HttpUtils;
import com.chzu.app.util.L;
import com.chzu.app.util.ScheduleUtil;
import com.chzu.app.util.URLUtil;

/**
 * @author wangxingchao
 * @date 2015-3-22 下午3:06:09 
 * @Description: 课表查询Fragment
 */
public class ScheduleFragment extends Fragment {
	public static String scheduleUrl;
	
	private ProgressDialog  progress;

	private RelativeLayout layout1;
	private RelativeLayout layout2;
	private TextView show_schedule;
	private ListView listView;
	private HorizontalScrollView hsv;

	private Handler mHandler;

	private static ArrayList<String> years;
	private static ArrayList<String> terms;

	private static String yearSelected;
	private static String termSelected;

	private static final int REQUESTCODE = 1;

	// AndroidHttpClient 自动管理线程池 
	AndroidHttpClient mHttpClient = null;

	//获取课表状态结果
	private final int GET_SCHEDULE_OK = 0x10;
	private final int GET_SCHEDULE_FAIL = 0x11;

	private final int GET_SCHEDULECONTENT_OK = 0x12;
	private final int GET_SCHEDULECONTENT_FAIL = 0x13;


	private String titleDay[] = {"星期一","星期二","星期三","星期四","星期五","星期六","星期日"};

	private List<Course> courses;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_view_schedule, null);

		initHandler();
		initConn();

		layout1 = (RelativeLayout)view.findViewById(R.id.select_schedule_layout);
		layout2 = (RelativeLayout)view.findViewById(R.id.get_schedule_layouts);
		show_schedule = (TextView) view.findViewById(R.id.show_schedule);
		listView = (ListView)view.findViewById(R.id.sch_listView);
		hsv = (HorizontalScrollView) view.findViewById(R.id.sch_hsView);
		layout1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(App.getUser() == null){
					Toast.makeText(getActivity(), "请登录！！！", Toast.LENGTH_SHORT).show();
				}else{

					new GetScheduleYearAndTermThread(App.getUser().getStudentId(),App.getUser().getName()).start();

				}
			}
		});

		layout2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				progress = new ProgressDialog(getActivity());
				progress.setTitle("获取课表中");
				progress.show();
				progress.setCancelable(false);
				new GetSchedule(App.getUser().getStudentId(),App.getUser().getName(),yearSelected,termSelected).start();
			}
		});

		return view;
	}


	private void initConn() {
		mHttpClient = HttpUtils.getHttpClient();
	}

	private void initHandler() {
		mHandler =new Handler(){
			public void handleMessage(Message msg){
				switch (msg.what) {
				case GET_SCHEDULE_OK:

					//跳转到选择学年界面
					Intent intent = new Intent();
					intent.putStringArrayListExtra("years", years);
					intent.putStringArrayListExtra("terms",terms);
					intent.setClass(getActivity(), PickYearTermActivity.class);
					startActivityForResult(intent, REQUESTCODE);
					break;
				case GET_SCHEDULE_FAIL:
					Toast.makeText(getActivity(), "获取课表失败", Toast.LENGTH_SHORT).show();
					break;
				case GET_SCHEDULECONTENT_OK:
					Toast.makeText(getActivity(), "获取课表成功", Toast.LENGTH_SHORT).show();
					String content = msg.obj.toString();
					courses = ScheduleUtil.getCourse(content);
					progress.cancel();
					initSchedule();
					
				default:
					break;
				}
			}

			private void initSchedule() {
				hsv.setVisibility(View.VISIBLE);
				ArrayList<TableRow> table = new ArrayList<TableRow>();  
				TableCell[] titles = new TableCell[7];// 每行5个单元  
				WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
				int width = wm.getDefaultDisplay().getWidth()/4;
				// 定义标题  
				for (int i = 0; i < titleDay.length; i++) {  
					titles[i] = new TableCell(titleDay[i],   
							width + 8 * i,  
							LayoutParams.MATCH_PARENT,   
							TableCell.TITLE);  
				}  
				table.add(new TableRow(titles));  
				// 每行的数据  

				TableCell[] cells1 = new TableCell[7];// 每行5个单元 一二节课
				TableCell[] cells2 = new TableCell[7];// 每行5个单元  三四节课
				TableCell[] cells3 = new TableCell[7];// 每行5个单元  五六节课
				TableCell[] cells4 = new TableCell[7];// 每行5个单元  七八节课
				TableCell[] cells5 = new TableCell[7];// 每行5个单元  晚自习

				for(int i=0,j=0 ; i<7 ; i++,j++){
					cells1[j] = new TableCell(courses.get(i).getContent(),width + 8 * j,LayoutParams.MATCH_PARENT,TableCell.STRING); 
				}
				table.add(new TableRow(cells1));
				for(int i=7,j=0 ; i<14 ; i++,j++){
					cells2[j] = new TableCell(courses.get(i).getContent(),width + 8 * j,LayoutParams.MATCH_PARENT,TableCell.STRING); 
				}
				table.add(new TableRow(cells2));
				for(int i=14,j=0 ; i<21 ; i++,j++){
					cells3[j] = new TableCell(courses.get(i).getContent(),width + 8 * j,LayoutParams.MATCH_PARENT,TableCell.STRING); 
				}
				table.add(new TableRow(cells3));
				for(int i=21,j=0 ; i<28 ; i++,j++){
					cells4[j] = new TableCell(courses.get(i).getContent(),width + 8 * j,LayoutParams.MATCH_PARENT,TableCell.STRING); 
				}
				table.add(new TableRow(cells4));
				for(int i=28,j=0 ; i<35 ; i++,j++){
					cells5[j] = new TableCell(courses.get(i).getContent(),width + 8 * j,LayoutParams.MATCH_PARENT,TableCell.STRING); 
				}
				table.add(new TableRow(cells5));
					
				TableAdapter tableAdapter = new TableAdapter(getActivity(), table);  
				listView.setAdapter(tableAdapter); 
			}
		};
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == PickYearTermActivity.RESULTCODE){
			Toast.makeText(getActivity(), data.getStringExtra("yearandterm"), Toast.LENGTH_SHORT).show();
			yearSelected = data.getStringExtra("yearSelected");
			termSelected = data.getStringExtra("termSelected");
			show_schedule.setText(data.getStringExtra("yearandterm"));
			layout2.setVisibility(View.VISIBLE);
		}
	}



	private final class GetScheduleYearAndTermThread extends Thread implements Runnable{
		String[] params;

		public GetScheduleYearAndTermThread(String... params){
			this.params = params;
		}
		@Override
		public void run() {
			//获取课表页面HTML
			String response = HttpUtils.getHttpResponse(mHttpClient, URLUtil.JWGL_USER_SCHEDULE_URL, params);
			Message msg = new Message();
			msg.what = GET_SCHEDULE_OK;
			msg.obj = response;

			years = ScheduleUtil.getYear(response);
			terms = ScheduleUtil.getTerm(response);

			mHandler.sendMessage(msg);
		}
	}

	private final class GetSchedule extends Thread implements Runnable{
		String[] params;

		public GetSchedule(String... params){
			this.params = params;	
		}

		@Override
		public void run() {
			//获取指定的学期课表html
			String response = HttpUtils.getHttpResponsePost(mHttpClient, URLUtil.JWGL_USER_SCHEDULE_URL, params);
			Message msg = new Message();
			msg.what = GET_SCHEDULECONTENT_OK;
			msg.obj = response;
			mHandler.sendMessage(msg);
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	public void onPause() {
		super.onPause();
		L.d("暂停");
	}
	@Override
	public void onResume() {
		super.onResume();
		L.d("重新");
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}

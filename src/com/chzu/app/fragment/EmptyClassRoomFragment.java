package com.chzu.app.fragment;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chzu.app.activity.EmptyClassShowActivity;
import com.chzu.app.activity.R;
import com.chzu.app.adapter.MyItemsAdapter;
import com.chzu.app.app.App;
import com.chzu.app.bean.ClassRoom;
import com.chzu.app.util.EmptyClassRoomUtil;
import com.chzu.app.util.HttpUtils;
import com.chzu.app.util.URLUtil;
/**
 *  
 * @author wangxingchao    
 * @version 1.0  
 * @created 2015-5-26 下午4:03:42
 */
@SuppressLint("HandlerLeak")
public class EmptyClassRoomFragment extends Fragment implements OnClickListener{

	
	private RelativeLayout btn_xiaoqu;
	private RelativeLayout btn_jiaoshilb;
	private RelativeLayout btn_zuowei_s;
	private RelativeLayout btn_zuowei_e;
	private RelativeLayout btn_riqi;
	private RelativeLayout btn_dsz;
	private RelativeLayout btn_jieci;
	private RelativeLayout btn_submit;
	
	private ProgressDialog progress;
	
	private TextView  id_xiaoqu;
	private TextView  id_jiaoshileibie;
	private TextView  id_zuoweishu_s;
	private TextView  id_zuoweishu_e;
	private TextView  id_riqi;
	private TextView  id_zhou;
	private TextView  id_jieci;


	private final int GET_EMPTYROOM_OK = 0x10;//获取html内容成功
	private final int Get_EMPTYROOMHTML_OK = 0x11;//获取空教室Html内容成功

	// AndroidHttpClient 自动管理线程池 
	AndroidHttpClient mHttpClient = null;

	private Handler mHandler;

	private static LinkedHashMap<String,String> map;


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
		View view = inflater.inflate(R.layout.frag_empty_room, null);

		initHandler();
		initCon();
		initView(view);

		return view;
	}

	private void initCon() {
		mHttpClient = HttpUtils.getHttpClient();
	}

	private void initHandler() {
		mHandler = new Handler(){
			@SuppressWarnings("rawtypes")
			public void handleMessage(Message msg){
				switch (msg.what) {
				case GET_EMPTYROOM_OK:
					//获取html成功
					String title = "选择日期";
					map = EmptyClassRoomUtil.getYear(msg.obj.toString());
					String[] items = new String[map.size()];
					int i=0;
					for (Map.Entry MapString : map.entrySet()) { 
						//String key=(String) MapString.getKey();//次方法获取键值对的名称 
						items[i] =(String) MapString.getKey();//次方法获取键值对的值 
						i++;
					} 
					alertDialog(id_riqi, items, title);
					break;
				case Get_EMPTYROOMHTML_OK:
					//获取空教室Html
					List<ClassRoom> cRooms = EmptyClassRoomUtil.getClassRoom(msg.obj.toString());
					progress.cancel();
					Intent intent = new Intent();
					intent.putExtra("crooms", (Serializable)cRooms);
					intent.setClass(getActivity(), EmptyClassShowActivity.class);
					startActivity(intent);
					break;
				default:
					break;
				}
			}
		};
	}


	

	private void initView(View view) {
		btn_xiaoqu = (RelativeLayout) view.findViewById(R.id.select_xiaoqu_layout);
		btn_jiaoshilb = (RelativeLayout) view.findViewById(R.id.select_jiaoshi_layout);
		btn_zuowei_s = (RelativeLayout) view.findViewById(R.id.select_zuowei_s_layout);
		btn_zuowei_e = (RelativeLayout) view.findViewById(R.id.select_zuowei_e_layout);
		btn_riqi = (RelativeLayout) view.findViewById(R.id.select_riqi_layout);
		btn_jieci = (RelativeLayout) view.findViewById(R.id.select_jieci_layout);
		btn_dsz = (RelativeLayout) view.findViewById(R.id.select_zhou_layout);
		btn_submit = (RelativeLayout) view.findViewById(R.id.id_submit);
		
		id_xiaoqu = (TextView) view.findViewById(R.id.id_xiaoqu);
		id_jiaoshileibie = (TextView) view.findViewById(R.id.id_jiaoshileibie);
		id_zuoweishu_s = (TextView) view.findViewById(R.id.id_zuoweishu_s);
		id_zuoweishu_e = (TextView) view.findViewById(R.id.id_zuoweishu_e);
		id_riqi = (TextView) view.findViewById(R.id.id_riqi);
		id_zhou = (TextView) view.findViewById(R.id.id_zhou);
		id_jieci = (TextView) view.findViewById(R.id.id_jieci);
		
		
		btn_xiaoqu.setOnClickListener(this);
		btn_jiaoshilb.setOnClickListener(this);
		btn_zuowei_s.setOnClickListener(this);
		btn_zuowei_e.setOnClickListener(this);
		btn_riqi.setOnClickListener(this);
		btn_jieci.setOnClickListener(this);
		btn_dsz.setOnClickListener(this);
		btn_submit.setOnClickListener(this);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}


	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		if(App.getUser() == null){
			Toast.makeText(getActivity(), "请先登录!!!", Toast.LENGTH_SHORT).show();
		}else{
			switch (v.getId()) {
			case R.id.select_xiaoqu_layout:
				showXiaoQu();
				break;
			case R.id.select_jiaoshi_layout:
				showJiaoShiType();
				break;
			case R.id.select_zuowei_s_layout:
				showZuoWeiS();
				break;
			case R.id.select_zuowei_e_layout:
				showZuoWeiE();
				break;
			case R.id.select_riqi_layout:
				showRiQi();
				break;
			case R.id.select_jieci_layout:
				showJieCi();
				break;
			case R.id.select_zhou_layout:
				shouZhou();
				break;
			case R.id.id_submit:
				progress = new ProgressDialog(getActivity());
				progress.setTitle("查询中...");
				progress.show();
				progress.setCancelable(false);
				show();
			default:
				break;
			}
		}
	}

	private void show() {

		Map<String,String> jMap = new HashMap<String,String>();
		jMap.put("第1,2节", "'1'|'1','0','0','0','0','0','0','0','0'");
		jMap.put("第3,4节", "'2'|'0','3','0','0','0','0','0','0','0'");
		jMap.put("第5,6节", "'3'|'0','0','5','0','0','0','0','0','0'");
		jMap.put("第7,8节", "'4'|'0','0','0','7','0','0','0','0','0'");
		jMap.put("第9,10节", "'5'|'0','0','0','0','9','0','0','0','0'");
		jMap.put("上午", "'6'|'1','3','0','0','0','0','0','0','0'");
		jMap.put("下午", "'7'|'0','0','5','7','0','0','0','0','0'");
		jMap.put("晚上", "'8'|'0','0','0','0','9','0','0','0','0'");
		jMap.put("白天", "'9'|'1','3','5','7','0','0','0','0','0'");
		jMap.put("整天", "'10'|'1','3','5','7','9','0','0','0','0'");

		String tXiaoqu = id_xiaoqu.getText().toString();
		String tJiaoshi = id_jiaoshileibie.getText().toString();
		String tQishizuowei = id_zuoweishu_s.getText().toString();
		String tJieshuzuowei = id_zuoweishu_e.getText().toString();
		String tRiji = id_riqi.getText().toString();
		String tZhou = id_zhou.getText().toString();
		String tJieci = id_jieci.getText().toString();

		if(tXiaoqu.equals(getResources().getString(R.string.xiaoqu))||tXiaoqu.equals("南校区")){
			tXiaoqu = "8";
		}else if(tXiaoqu.equals("北校区")){
			tXiaoqu = "9";
		}
		if(tJiaoshi.equals(getResources().getString(R.string.jiaoshi))){
			tJiaoshi = "";
		}
		if(tQishizuowei.equals(getResources().getString(R.string.zuowei_s))){
			tQishizuowei = "0";
		}else{
			tQishizuowei = tQishizuowei.replace("人", "");
		}
		if(tJieshuzuowei.equals(getResources().getString(R.string.zuowei_e))){
			tJieshuzuowei = "";
		}else{
			tJieshuzuowei = tJieshuzuowei.replace("人", "");
		}
		if(tRiji.equals(getResources().getString(R.string.riqi))){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			tRiji = map.get(format.format(new Date()));
		}else{
			tRiji = map.get(tRiji);
		}
		if(tZhou.equals(getResources().getString(R.string.zhou))){
			tZhou = "";
		}
		if(tJieci.equals(getResources().getString(R.string.jieci))){
			tJieci = "";
		}else{
			tJieci = jMap.get(tJieci);
		}
		int tXqj = 0;
		try {
			if(id_riqi.getText().toString().equals("日期")){
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				tXqj = dayForWeek(format.format(new Date()));
			}else{
				tXqj = dayForWeek(id_riqi.getText().toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {

		} catch (Exception e) {
			e.printStackTrace();
		}

		Toast.makeText(getActivity(), tXiaoqu+"--"+tJiaoshi+"--"+tQishizuowei+"--"+tJieshuzuowei+"--"+tRiji+"--"+tZhou+"--"+tJieci, Toast.LENGTH_SHORT).show();
		new GetClassRoom(
				App.getUser().getStudentId(),//0
				App.getUser().getName(),//1
				tXiaoqu,//2
				tJiaoshi,//3
				tQishizuowei,//4
				tJieshuzuowei,//5
				tRiji,//6
				tXqj+"",//7
				tZhou,//8
				tJieci//9
				).start();

	}

	/**
	 * 选择单双周
	 */
	private void shouZhou() {
		String items[] = {"单","周"};
		String title = "请选择单双周";
		alertDialog(id_zhou, items, title);
	}

	/**
	 * 选择日期
	 */
	private void showRiQi() {
		new GetRiQiItems(App.getUser().getStudentId(),App.getUser().getName()).start();
	}

	/**
	 * 选择节次
	 */
	private void showJieCi() {
		String items[] = {"第1,2节","第3,4节","第5,6节","第7,8节","第9,10节","上午","下午","晚上","白天","整天"};
		String title = "请选择节次";
		alertDialog(id_jieci, items, title);

	}

	/**
	 * 终止座位
	 */
	private void showZuoWeiE() {
		LayoutInflater inflater = getActivity().getLayoutInflater();  
		View dialog_view = inflater.inflate(R.layout.frag_empty_room_zuowei, null);   
		Button submit = (Button) dialog_view.findViewById(R.id.id_zuowei_submit);  
		final EditText zuowei = (EditText) dialog_view.findViewById(R.id.id_zuowei_shu);
		final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();  
		alertDialog.setView(dialog_view);
		alertDialog.setTitle("请选择结束人数");
		alertDialog.show();
		submit.setOnClickListener(new OnClickListener() {  
			public void onClick(View v) {    
				id_zuoweishu_e.setText(zuowei.getText()+"人");
				alertDialog.cancel();
			} 
		});   
	}

	/**
	 * 座位起始
	 */
	private void showZuoWeiS() {
		LayoutInflater inflater = getActivity().getLayoutInflater();  
		View dialog_view = inflater.inflate(R.layout.frag_empty_room_zuowei, null);   
		Button submit = (Button) dialog_view.findViewById(R.id.id_zuowei_submit);  
		final EditText zuowei = (EditText) dialog_view.findViewById(R.id.id_zuowei_shu);
		final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();  
		alertDialog.setView(dialog_view); 
		alertDialog.setTitle("请选择起始人数");
		alertDialog.show();
		submit.setOnClickListener(new OnClickListener() {  
			public void onClick(View v) {    
				id_zuoweishu_s.setText(zuowei.getText()+"人");
				alertDialog.cancel();
			} 
		});

	}

	/**
	 * 选择校区
	 */
	private void showXiaoQu() {
		final String items[] = {"南校区","北校区"};
		String title = "请选择校区";

		alertDialog(id_xiaoqu,items,title);
	}

	/**
	 * 选择教室类型
	 */
	private void showJiaoShiType() {
		final String items[] = {"报告厅","多媒体教室","机房","美术系教室","普通教室","实训基地","实验室","体育","音乐系教室","语音室"};
		String title = "请选择教室类别";
		alertDialog(id_jiaoshileibie, items, title);
	}

	/**
	 * 对话框
	 * @param rLayout
	 * @param items
	 * @param title
	 */
	private void alertDialog(final TextView textView,
			String[] items, String title) {

		List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();

		for(int i = 0;i<items.length;i++){
			Map<String,Object> item = new HashMap<String,Object>();
			item.put("value",items[i]);
			data.add(item);
		}
		ListView listView= new ListView(getActivity());

		//构造listview对象。
		MyItemsAdapter adapter = new MyItemsAdapter(
				getActivity(), data); 
		listView.setAdapter(adapter);

		final AlertDialog alertDialog = new AlertDialog.Builder(
				getActivity()).create();
		alertDialog.setCanceledOnTouchOutside(false);//使除了dialog以外的地方不能被点击  
		alertDialog.setTitle(title);
		alertDialog.setView(listView);
		alertDialog.show();

		listView.setOnItemClickListener(new OnItemClickListener() {//响应listview中的item的点击事件  
			@Override  
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,  
					long arg3) {  
				TextView tv = (TextView) arg1  
						.findViewById(R.id.id_select_type);//取得每条item中的textview控件  
				textView.setText(tv.getText().toString());  
				alertDialog.cancel();  
			}  
		});

	}



	class GetRiQiItems extends Thread implements Runnable{
		String[] params;

		public GetRiQiItems(String...params){
			this.params = params;
		}
		@Override
		public void run() {
			String response = HttpUtils.getHttpResponse(mHttpClient, URLUtil.JWGL_EMPTY_CLASSROOM, params);
			Message msg = new Message();
			msg.what = GET_EMPTYROOM_OK;
			msg.obj = response;

			mHandler.sendMessage(msg);
		}
	}

	class GetClassRoom extends Thread implements Runnable{
		String[] params;

		public GetClassRoom(String...params){
			this.params = params;
		}

		@Override
		public void run() {
			String response = null;
			try {
				response = EmptyClassRoomUtil.getResponse(mHttpClient, params);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			Message msg = new Message();
			msg.what = Get_EMPTYROOMHTML_OK;
			msg.obj = response;
			mHandler.sendMessage(msg);
		}
	}

	public static int dayForWeek(String pTime) throws Exception {  
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");  
		Calendar c = Calendar.getInstance();  
		c.setTime(format.parse(pTime));  
		int dayForWeek = 0;  
		if(c.get(Calendar.DAY_OF_WEEK) == 1){  
			dayForWeek = 7;  
		}else{  
			dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;  
		}  
		return dayForWeek;  
	}  

}

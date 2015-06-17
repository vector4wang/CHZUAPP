package com.chzu.app.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.chzu.app.activity.LoginActivity;
import com.chzu.app.activity.R;
import com.chzu.app.bean.User;
/**
 * 
 * @author wangxingchao
 * @date 2015-3-22 下午2:55:45 
 * @Description: 右侧菜单,用来显示个人的信息(默认用户未登录，登录成功便会显示个人的信息)
 */
public class RightFragment extends Fragment{

	
	
	private ImageButton btn_IsLogin;
	private LinearLayout unlogin_info_layout;
	private TextView wel_show;
	private ListView listView;
	private User mUser;


	private String[] from = {"info_key","info_value"};
	private int[] to = {R.id.info_Key,R.id.info_Value};

	private static final int code = 1;
	private final String TAG = RightFragment.class.getSimpleName();


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.layout_login, null);

		btn_IsLogin = (ImageButton)view.findViewById(R.id.btn_IsLogin);
		unlogin_info_layout = (LinearLayout) view.findViewById(R.id.unlogin_info_layout);
		wel_show = (TextView) view.findViewById(R.id.wel_show);
		listView = (ListView)view.findViewById(R.id.userInfo_ListView);
		btn_IsLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), LoginActivity.class);
				startActivityForResult(intent, code);
			}
		});

		
		return view;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		//这里判断code
		if(resultCode == LoginActivity.resultCode){
			mUser = (User)data.getSerializableExtra("user");
		}

		if(mUser != null){
			unlogin_info_layout.setVisibility(View.GONE);

			wel_show.setText("欢迎"+mUser.getName()+"同学");
			List<Map<String,Object>> mData;
			mData = new ArrayList<Map<String,Object>>();

			Map<String,Object> preLine1 = new HashMap<String,Object>();
			preLine1.put("info_key", "姓名:");
			preLine1.put("info_value", mUser.getName());
			mData.add(preLine1);

			Map<String,Object> preLine2 = new HashMap<String,Object>();
			preLine2.put("info_key", "学号:");
			preLine2.put("info_value", mUser.getStudentId());
			mData.add(preLine2);

			Map<String,Object> preLine3 = new HashMap<String,Object>();
			preLine3.put("info_key", "曾用名:");
			preLine3.put("info_value", mUser.getFormerName());
			mData.add(preLine3);

			Map<String,Object> preLine4 = new HashMap<String,Object>();
			preLine4.put("info_key", "性别:");
			preLine4.put("info_value", mUser.getGender());
			mData.add(preLine4);

			Map<String,Object> preLine5 = new HashMap<String,Object>();
			preLine5.put("info_key", "民族:");
			preLine5.put("info_value", mUser.getNation());
			mData.add(preLine5);

			Map<String,Object> preLine6 = new HashMap<String,Object>();
			preLine6.put("info_key", "籍贯:");
			preLine6.put("info_value", mUser.getNativePlace());
			mData.add(preLine6);

			Map<String,Object> preLine7 = new HashMap<String,Object>();
			preLine7.put("info_key", "政治面貌:");
			preLine7.put("info_value", mUser.getPoliticalStatus());
			mData.add(preLine7);

			Map<String,Object> preLine8 = new HashMap<String,Object>();
			preLine8.put("info_key", "学院:");
			preLine8.put("info_value", mUser.getAcademy());
			mData.add(preLine8);

			Map<String,Object> preLine9 = new HashMap<String,Object>();
			preLine9.put("info_key", "入学年份:");
			preLine9.put("info_value", mUser.getJoinSchool());
			mData.add(preLine9);

			Map<String,Object> preLine10 = new HashMap<String,Object>();
			preLine10.put("info_key", "专业名称:");
			preLine10.put("info_value", mUser.getSpecialty());
			mData.add(preLine10);

			Map<String,Object> preLine11 = new HashMap<String,Object>();
			preLine11.put("info_key", "行政班:");
			preLine11.put("info_value", mUser.getExecutiveCourses());
			mData.add(preLine11);

			Map<String,Object> preLine12 = new HashMap<String,Object>();
			preLine12.put("info_key", "学制:");
			preLine12.put("info_value", mUser.getAcademicYear());
			mData.add(preLine12);

			Map<String,Object> preLine13 = new HashMap<String,Object>();
			preLine13.put("info_key", "身份证号:");
			preLine13.put("info_value", mUser.getID());
			mData.add(preLine13);

			Map<String,Object> preLine14 = new HashMap<String,Object>();
			preLine14.put("info_key", "准考证号:");
			preLine14.put("info_value", mUser.getExaminationNumber());
			mData.add(preLine14);

			MyAdapter adapter = new MyAdapter(getActivity(),mData,R.layout.layout_login_peruserinfo,from,to);
			listView.setAdapter(adapter);
		}
	}

	
	
	
	class MyAdapter extends SimpleAdapter{

		public MyAdapter(Context context, List<? extends Map<String, ?>> data,
				int resource, String[] from, int[] to) {
			super(context, data, resource, from, to);
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = super.getView(position, convertView, parent);
			TextView info_key = (TextView) view.findViewById(R.id.info_Key);
			TextView info_Value = (TextView) view.findViewById(R.id.info_Value);
			return view;
		}
	}
}

package com.chzu.app.fragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.chzu.app.activity.R;
import com.chzu.app.activity.ScoreShowActivity;
import com.chzu.app.app.App;
import com.chzu.app.bean.Score;
import com.chzu.app.bean.ScoreEnum;
import com.chzu.app.bean.ScoreInfo;
import com.chzu.app.util.HttpUtils;
import com.chzu.app.util.ScoreUtils;
/**
 * 
 * @author wangxingchao
 * @date 2015-3-22 下午2:55:14 
 * @Description: 成绩查看Fragmeng
 */
public class GradeFragment extends Fragment {

	private ImageButton btn_cjtj;//成绩统计
	private ImageButton btn_wtgcj;//未通过成绩
	private ImageButton btn_lncj;//历年成绩
	private ImageButton btn_zgcj;//成绩查询

	private ProgressDialog progress;
	
	private static final int GET_SCORE_OK = 0x10;
	private static final int GET_SCORE_CJTJ_OK = 0x11;
	private static final int GET_SCORE_WTGCJ_OK = 0x12;
	private static final int GET_SCORE_LNCJ_OK = 0x13;
	private static final int GET_SCORE_ZGCJ_OK = 0x14;
	private static final int GET_SCORE_FAIL = 0x15;

	private Handler mHandler;

	// AndroidHttpClient 自动管理线程池 
	AndroidHttpClient mHttpClient = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_view_grade, container,false);
		
		initView(view);
		initConn();
		initHandle();
		return view;
	}


	private void initView(View view) {
		btn_cjtj = (ImageButton) view.findViewById(R.id.imageButton3);
		btn_wtgcj = (ImageButton) view.findViewById(R.id.imageButton2);
		btn_lncj = (ImageButton) view.findViewById(R.id.imageButton1);
		btn_zgcj = (ImageButton) view.findViewById(R.id.imageButton4);
		
		btn_lncj.setOnClickListener(new MyClickListener());
		btn_wtgcj.setOnClickListener(new MyClickListener());
		btn_cjtj.setOnClickListener(new MyClickListener());
		btn_zgcj.setOnClickListener(new MyClickListener());

	}


	class MyClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			if(App.getUser() == null){
				Toast.makeText(getActivity(), "请先登录!!!", Toast.LENGTH_SHORT).show();
			}else{
				progress = new ProgressDialog(getActivity());
				progress.setTitle("获取数据中...");
				progress.show();
				progress.setCancelable(false);
				switch (v.getId()) {
				case R.id.imageButton3:
					//成绩统计
					new GetScoreThread(App.getUser().getStudentId(),App.getUser().getName(),ScoreEnum.CJTJ).start();
					break;
				case R.id.imageButton2:
					//未通过成绩
					new GetScoreThread(App.getUser().getStudentId(),App.getUser().getName(),ScoreEnum.WTGJCJ).start();
					break;
				case R.id.imageButton1:
					//跳转到历年成绩界面
					new GetScoreThread(App.getUser().getStudentId(),App.getUser().getName(),ScoreEnum.LNCHJ).start();
					break;
				case R.id.imageButton4:
					//跳转到最高成绩界面
					new GetScoreThread(App.getUser().getStudentId(),App.getUser().getName(),ScoreEnum.ZGCJ).start();
					break;

				default:
					break;
				}
			}
			
		}
	}

	private void initConn() {
		mHttpClient = HttpUtils.getHttpClient();
	}


	private void initHandle() {
		mHandler = new Handler(){
			public void handleMessage(Message msg){
				switch (msg.what) {
				case GET_SCORE_OK:
					Toast.makeText(getActivity(), "获取成绩成功", Toast.LENGTH_SHORT).show();
					break;
				case GET_SCORE_FAIL:
					Toast.makeText(getActivity(), "获取成绩失败", Toast.LENGTH_SHORT).show();
					break;
				case GET_SCORE_LNCJ_OK:
					Toast.makeText(getActivity(), "获取历年成绩成功", Toast.LENGTH_SHORT).show();
					
					new GetLncjAsyncTask().execute(msg.obj.toString());
					break;
				case GET_SCORE_CJTJ_OK:
					progress.cancel();
					Toast.makeText(getActivity(), "获取成绩统计成功", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent();
					Bundle bundle = new Bundle();
					bundle.putSerializable("scoreInfo", (ScoreInfo)msg.obj);
					intent.putExtra("type", 2);
					intent.putExtras(bundle);
					intent.setClass(getActivity(), ScoreShowActivity.class);
					startActivity(intent);
					break;
				case GET_SCORE_WTGCJ_OK:
					progress.cancel();
					Toast.makeText(getActivity(), "获取未通过成绩成功", Toast.LENGTH_SHORT).show();
					Intent wIntent = new Intent();
					wIntent.putExtra("type", 1);
					wIntent.putExtra("wtgScore", (Serializable)msg.obj);
					wIntent.setClass(getActivity(), ScoreShowActivity.class);
					startActivity(wIntent);
					break;
				case GET_SCORE_ZGCJ_OK:
					progress.cancel();
					Toast.makeText(getActivity(), "获取最高成绩成功", Toast.LENGTH_SHORT).show();
					Intent zIntent = new Intent();
					zIntent.putExtra("type", 3);
					zIntent.putExtra("zgScore", (Serializable)msg.obj);
					zIntent.setClass(getActivity(), ScoreShowActivity.class);
					startActivity(zIntent);
				default:
					break;
				}
			}
		};
	}

	/**
	 * 
	 * @ClassName: GetScoreThread 
	 * @Description: params:[学号,姓名,跳转类型]
	 * @date 2015-4-20 下午4:49:07 
	 *
	 */
	class GetScoreThread extends Thread implements Runnable{

		Object params[];

		public GetScoreThread(Object...params){
			this.params = params;
		}

		@Override
		public void run() {
			
			if(params[2]==ScoreEnum.CJTJ){
				//成绩统计
				Message msgCjtj = new Message();
				ScoreInfo scoreInfo = ScoreUtils.getScoreInfo(mHttpClient, params);
				if(scoreInfo != null){
					msgCjtj.what = GET_SCORE_CJTJ_OK;
					msgCjtj.obj = scoreInfo;
				}
				mHandler.sendMessage(msgCjtj);
			}
			if(params[2]==ScoreEnum.WTGJCJ){
				Message msgWtgcj = new Message();
				List<Score> score = ScoreUtils.getWtgcjScore(mHttpClient, params);
				if(score != null){
					msgWtgcj.what = GET_SCORE_WTGCJ_OK;
					msgWtgcj.obj = score;
				}
				mHandler.sendMessage(msgWtgcj);
			}
			if(params[2]==ScoreEnum.LNCHJ){
				//获取历年成绩的Html
				Message msgLncj = new Message();
				String response = ScoreUtils.getResponse(mHttpClient, params);
				if(response != null){
					msgLncj.what = GET_SCORE_LNCJ_OK;
					msgLncj.obj = response;
				}
				mHandler.sendMessage(msgLncj);
				
			}
			if(params[2]==ScoreEnum.ZGCJ){
				Message msgZgcj = new Message();
				List<Score> scores = ScoreUtils.getZgcjScore(mHttpClient, params);
				if(scores != null){
					msgZgcj.what = GET_SCORE_ZGCJ_OK;
					msgZgcj.obj = scores;
				}
				mHandler.sendMessage(msgZgcj);
			}
			
		}
	}
	
	class GetLncjAsyncTask extends AsyncTask<String, Void, Void>{

		List<Score> scores = new ArrayList<Score>();
		
		@Override
		protected Void doInBackground(String... params) {
			scores = ScoreUtils.getScore(params[0]);
			return null;
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Toast.makeText(getActivity(), "正在获取中...", Toast.LENGTH_SHORT).show();
		}
		@Override
		protected void onPostExecute(Void result) {
			progress.cancel();
			Intent intent = new Intent();
			intent.putExtra("scoreList", (Serializable)scores);
			intent.putExtra("type", 0);
			intent.setClass(getActivity(), ScoreShowActivity.class);
			startActivity(intent);
		}
	}


	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}


	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}

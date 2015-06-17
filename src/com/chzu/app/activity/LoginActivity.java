package com.chzu.app.activity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.chzu.app.app.App;
import com.chzu.app.bean.User;
import com.chzu.app.util.AppUtil;
import com.chzu.app.util.HttpUtils;
import com.chzu.app.util.HttpUtils4Jwgl;
import com.chzu.app.util.L;
import com.chzu.app.util.StringUtils;
import com.chzu.app.util.UserUtils;

/**
 * @ClassName: LoginActivity 
 * @Description: 用户登录界面
 * @date 2015-4-15 下午7:46:18 
 *
 */
public class LoginActivity extends Activity implements OnClickListener{

	public static final int resultCode = 1;

	private User mUser;

	public static final String ACCOUNT = "account";
	public static final String PASSWD = "password";
	public static final String REMEMBER = "remember";

	private ProgressDialog progress;

	// 第一次连接教务系统后产生的一段随机安全码(24位)
	public static String randomUrl = "";

	
	// AndroidHttpClient 自动管理线程池 
	AndroidHttpClient mHttpClient = null;

	// 输入账号,输入密码,输入验证码 
	private EditText accountText, passwordText, checkcodeText;
	// 显示的验证码 
	private ImageView checkcodeView;

	// 登录与重置按钮
	private Button btnLogin;

	// 记住用户复选框 
	private CheckBox remenberUser;
	
	private Handler mHandler;
	private final int CHECKCODE_STATUS_GET_FAIL = 0x00; // 获取失败
	private final int CHECKCODE_STATUS_PARSE_FAIL = 0x01; // 解析失败
	private final int CHECKCODE_STATUS_OK = 0x02; // 获取成功并解析

	// 登录状态结果 
	private final int LOGIN_RESULT_OK = 0x10;
	private final int LOGIN_RESULT_FAIL = 0x11;
	private final int LOGIN_RESULT_FAIL_CHECKCODE = 0x12;
	private final int LOGIN_RESULT_FAIL_ACCOUNT = 0x13;
	private final int LOGIN_RESULT_FAIL_PASSWORD = 0x14;
	private final int LOGIN_RESULT_FAIL_SERVICE_BUSY = 0x15;
	private final int LOGIN_GET_USERINFO_OK = 0x16;
	private final int LOGIN_GET_USERINFO_FAIL = 0x17;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		initHandler();
		initView();
		initConn();
		initInfo();
	}


	private void initHandler() {
		mHandler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				// 获取验证码失败
				case CHECKCODE_STATUS_GET_FAIL:
					showToast("请重新获取验证码");
					break;
					// 解析失败
				case CHECKCODE_STATUS_PARSE_FAIL:
					showToast("解析验证码失败啦。。。");
					break;
					// 获取验证码成功
				case CHECKCODE_STATUS_OK:
					checkcodeView.setImageBitmap((Bitmap) msg.obj);
					break;

					// 登录成功
				case LOGIN_RESULT_OK:
					/**
					 * 登录成功需按照以下几个步骤一步一步进行
					 * 1、提示用户登录成功，显示"欢迎XXX";
					 * 2、获取Use信息;
					 * 3、将User对象返回右侧菜单栏，显示所有信息。
					 * */
					//第一步
					progress.cancel();
					showToast("欢迎" + msg.obj);
					new GetUserInfoThread(accountText.getText().toString(), msg.obj.toString()).start();
					break;

					// 验证码错误
				case LOGIN_RESULT_FAIL_CHECKCODE:
					progress.cancel();
					showToast("验证码错误");
					changeCheckCode();
					break;

					// 用户名错误
				case LOGIN_RESULT_FAIL_ACCOUNT:
					progress.cancel();
					showToast("不存在该用户");
					passwordText.setText("");
					changeCheckCode();
					break;

					// 密码错误
				case LOGIN_RESULT_FAIL_PASSWORD:
					progress.cancel();
					showToast("密码错误");
					passwordText.setText("");
					changeCheckCode();
					break;

					// 服务器
				case LOGIN_RESULT_FAIL_SERVICE_BUSY:
					showToast("服务器繁忙");
					changeCheckCode();
					break;

				case LOGIN_RESULT_FAIL:
					showToast("登录失败,可能网络出问题了");
					changeCheckCode();
					break;
				case LOGIN_GET_USERINFO_OK://登录成功获取用户信息
					//保存用户信息
					App.setUser(mUser);
					//Log.i("UserDetail", ((User)msg.obj).toString());
					
					
					//将信息发送给PHP后台
					new PostUserTOPHP().execute((User)msg.obj);
					
					Intent intent = getIntent();

					//存放获取到的数据
					Bundle bundle = new Bundle();
					bundle.putSerializable("user", (User)msg.obj);
					intent.putExtras(bundle);
					setResult(resultCode,intent);
					progress.cancel();
					finish();
					break;
				case LOGIN_GET_USERINFO_FAIL:
					showToast("获取用户信息失败!请稍后再试");
					break;
				default:
					break;
				}
			}
			// 刷新验证码
			private void changeCheckCode() {
				checkcodeText.setText("");
				flushCheckCode(null);
			}
		};
	}

	private void initView() {
		accountText = (EditText) findViewById(R.id.et_login_jwgl_account);
		passwordText = (EditText) findViewById(R.id.et_login_jwgl_password);
		checkcodeText = (EditText) findViewById(R.id.et_login_jwgl_checkcode);
		checkcodeView = (ImageView) findViewById(R.id.iv_login_jwgl_checkcode);

		btnLogin = (Button)findViewById(R.id.ib_login_jwgl_login);

		btnLogin.setOnClickListener(this);
		remenberUser = (CheckBox) findViewById(R.id.cb_login_jwgl_remember_pw);
	}

	private void initConn() {
		mHttpClient = HttpUtils.getHttpClient();
		// 第一次进入获取验证码
		flushCheckCode(null);
	}

	private void initInfo() {
		// 如果记住了用户,则直接从配置文件中读取上次保存的信息,不保证是正确的
		if (AppUtil.getBoolFromConf(getApplicationContext(), REMEMBER)) {
			accountText.setText(AppUtil.getStrFromConf(getApplicationContext(), ACCOUNT));
			passwordText.setText(AppUtil.getStrFromConf(getApplicationContext(), PASSWD));
		} else {
			// 不记住用户
			remenberUser.setChecked(false);
		}		
	}

	private void showToast(String string) {
		Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT).show();				
	}
	// 刷新获取验证码
	public void flushCheckCode(View view) {
		new GetCheckCodeThread().start();
	}

	// 获取验证码线程 
	private final class GetCheckCodeThread extends Thread implements Runnable {
		@Override
		public void run() {
			InputStream inputStream = null;
			try {
				inputStream = HttpUtils4Jwgl.getCheckCodeInputStream();
				if (null == inputStream) {
					// 获取失败了
					mHandler.sendEmptyMessage(CHECKCODE_STATUS_GET_FAIL);
				}

				// 获取到了验证码的输入流
				else {
					// 这种写法可能导致解析失败
					// Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
					byte[] data = readStream(inputStream);
					if (null != data) {
						Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
						if (null == bitmap) {
							// 解析失败
							mHandler.sendEmptyMessage(CHECKCODE_STATUS_PARSE_FAIL);
						} else {
							// 解析输入流为bitmap成功
							Message message = new Message();
							message.what = CHECKCODE_STATUS_OK;
							message.obj = bitmap; //
							mHandler.sendMessage(message);
						}
					} else {
						// 解析失败
						mHandler.sendEmptyMessage(CHECKCODE_STATUS_PARSE_FAIL);
					}
				}

			} catch (IOException e1) {
				e1.printStackTrace();
				mHandler.sendEmptyMessage(CHECKCODE_STATUS_GET_FAIL);
			}

		}
	}

	/**
	 * 输入流转换为字节数组
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public byte[] readStream(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		// 缓冲区
		byte[] buffer = new byte[1024];
		// 每次读取字节的个数
		int len = 0;
		// 没有读到流的末尾就继续读取到缓冲区,将缓冲区的字节写到out中,直到读到流末尾,Len=-1,结束
		while ((len = in.read(buffer)) != -1) {
			out.write(buffer, 0, len);
			L.d("writing ...  " + len);
		}
		L.d("write ok ");
		in.close();
		out.close();
		return out.toByteArray();
	}

	// 登录 
	public void login(View view) {



		String account = accountText.getText().toString().trim();
		String password = passwordText.getText().toString().trim();
		String checkCode = checkcodeText.getText().toString().trim();

		// 写入配置文件,保存用户名和密码
		if (remenberUser.isChecked()) {
			AppUtil.putStr2Conf(getApplicationContext(), ACCOUNT, account);
			AppUtil.putStr2Conf(getApplicationContext(), PASSWD, password);
			AppUtil.putBool2Conf(getApplicationContext(), REMEMBER, true);
		}

		String matchResult = StringUtils.loginMatch(account, password, checkCode);
		// 不能完全匹配登录
		if (null != matchResult) {
			showToast(matchResult);
			return;
		}

		progress = new ProgressDialog(getApplicationContext());
		progress.setTitle("登录中...");
		progress.setCancelable(false);

		// 执行登录任务
		new LoginThread(account, password, checkCode).start();
	}

	// 登录线程
	private final class LoginThread extends Thread implements Runnable {

		String[] params;

		public LoginThread(String... params) {
			this.params = params;
		}

		@Override
		public void run() {

			try {
				int loginStatus = HttpUtils4Jwgl.login(params);
				switch (loginStatus) {
				case HttpUtils4Jwgl.LOGIN_STATUS_OK:
					login(params);
					break;
				case HttpUtils4Jwgl.LOGIN_STATUS_CHECKCODE_ERROR:
					mHandler.sendEmptyMessage(LOGIN_RESULT_FAIL_CHECKCODE);
					break;
				case HttpUtils4Jwgl.LOGIN_STATUS_USER_ERROR:
					mHandler.sendEmptyMessage(LOGIN_RESULT_FAIL_ACCOUNT);
					break;
				case HttpUtils4Jwgl.LOGIN_STATUS_PW_ERROR:
					mHandler.sendEmptyMessage(LOGIN_RESULT_FAIL_PASSWORD);
					break;
				case HttpUtils4Jwgl.LOGIN_STATUS_SEVICE_BUSY:
					mHandler.sendEmptyMessage(LOGIN_RESULT_FAIL_SERVICE_BUSY);
					break;

				default:
					break;
				}

			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
				mHandler.sendEmptyMessage(LOGIN_RESULT_FAIL);
			}

		}

		private void login(String[] params) {
			try {
				String result = HttpUtils4Jwgl.getLoginResult(AppUtil.getStrFromConf(getApplicationContext(), ACCOUNT));

				if (result.contains("退出")) {
					Message msg = new Message();
					// msg.obj = UserUtils.getUserInfo(result);
					msg.obj = UserUtils.getUserName(result);
					msg.what = LOGIN_RESULT_OK;
					mHandler.sendMessage(msg);
				} else {
					mHandler.sendEmptyMessage(LOGIN_RESULT_FAIL);
				}

			} catch (IOException e) {
				e.printStackTrace();
				mHandler.sendEmptyMessage(LOGIN_RESULT_FAIL);
			}
		}
	}

	//获取用户信息线程
	private final class GetUserInfoThread extends Thread implements Runnable{
		String[] params;

		public GetUserInfoThread(String... params) {
			this.params = params;
		}
		@Override
		public void run() {
			getUser(params);
		}

		private void getUser(String[] params) {
			mUser = UserUtils.getUser(mHttpClient, params);
			if(mUser!=null){
				Message msg = new Message();
				msg.obj = mUser;
				msg.what = LOGIN_GET_USERINFO_OK;
				mHandler.sendMessage(msg);
			}else{
				mHandler.sendEmptyMessage(LOGIN_GET_USERINFO_FAIL);
			}

		}
	}
	
	class PostUserTOPHP extends AsyncTask<User, Void, Void>
	{

		@Override
		protected Void doInBackground(User... params) {
			
			User user = params[0];
			UserUtils.SaveDataToPhp(user);
			return null;
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		int viewId = v.getId();
		switch (viewId) {
		case R.id.ib_login_jwgl_login:
			login(v);
			break;
		default:
			break;
		}
	}
}

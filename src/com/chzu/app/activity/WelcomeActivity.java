package com.chzu.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

/**
 *  
 * @author wangxingchao    
 * @version 1.0  
 * @created 2015-5-26 下午3:56:36
 */
public class WelcomeActivity extends Activity{
	
	private ImageView welcomeIme; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		
		welcomeIme = (ImageView) findViewById(R.id.welcome_img);
		
		AlphaAnimation anima = new AlphaAnimation(0.3f, 1.0f);
		anima.setDuration(3000);//设置动画显示时间
		welcomeIme.startAnimation(anima);
		anima.setAnimationListener(new AnimationImpl());
		
	}
	
	private class AnimationImpl implements AnimationListener{

		@Override
		public void onAnimationStart(Animation animation) {
			welcomeIme.setBackgroundResource(R.drawable.init_pic);
		}

		@Override
		public void onAnimationEnd(Animation animation) {
			skip();//动画结束之后跳转到别的页面
		}
		
		@Override
		public void onAnimationRepeat(Animation animation) {
			
		}
	}
	private void skip() {
		startActivity(new Intent(this, MainActivity.class));  
        finish();  
	}

}

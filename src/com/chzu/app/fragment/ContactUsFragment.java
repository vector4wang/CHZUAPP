package com.chzu.app.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chzu.app.activity.R;

/**
 * 
 * @author wangxingchao
 * @date 2015-3-22 下午2:54:43 
 * @Description: 教学评价Fragment
 */
public class ContactUsFragment extends Fragment {
	
	private TextView contast_author;
	private TextView contast_xxzx;
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_contast_us, null);
		contast_author = (TextView) view.findViewById(R.id.contast_author);
		contast_xxzx = (TextView)view.findViewById(R.id.contast_xxzx);
		
		contast_author.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				sendMailByIntent();
			}
		});
		
		contast_xxzx.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:3510512"));
				startActivity(intent);
			}
		});
		return view;
	}
	public int sendMailByIntent() {  
        String[] reciver = new String[] { "wdc43101289217@126.com" };  
        String[] mySbuject = new String[] { "邮件" };  
        String myCc = "cc";  
        String mybody = "您想说点什么呢......";  
        Intent myIntent = new Intent(android.content.Intent.ACTION_SEND);  
        myIntent.setType("plain/text");  
        myIntent.putExtra(android.content.Intent.EXTRA_EMAIL, reciver);  
        myIntent.putExtra(android.content.Intent.EXTRA_CC, myCc);  
        myIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, mySbuject);  
        myIntent.putExtra(android.content.Intent.EXTRA_TEXT, mybody);  
        startActivity(Intent.createChooser(myIntent, "选择您所要发送邮件的类型"));  
  
        return 1;  
  
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

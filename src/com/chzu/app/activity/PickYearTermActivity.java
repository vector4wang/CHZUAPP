package com.chzu.app.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.chzu.app.view.PickerView;
import com.chzu.app.view.PickerView.onSelectListener;

/**
 *  
 * @author wangxingchao    
 * @version 1.0  
 * @created 2015-5-26 下午3:56:18
 */
public class PickYearTermActivity extends Activity {

	private Button button;
	private PickerView minute_pv;
	private PickerView second_pv;
	
	private String yearText;
	private String termText;
	
	public static final int RESULTCODE = 2; 

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.year_term_picker);
		minute_pv = (PickerView) findViewById(R.id.minute_pv);
		second_pv = (PickerView) findViewById(R.id.second_pv);
		button = (Button) findViewById(R.id.button);
		

		Intent intent = getIntent();
		ArrayList<String> years = intent.getStringArrayListExtra("years");
		ArrayList<String> terms = intent.getStringArrayListExtra("terms");
		
		//默认选择
		yearText = years.get(years.size()/2);
		termText = terms.get(terms.size()/2);
		
		
		minute_pv.setData(years);
		
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent bIntent = getIntent();
				String backString = yearText+"年度第"+termText+"学期";
				bIntent.putExtra("yearSelected", yearText);
				bIntent.putExtra("termSelected", termText);
				bIntent.putExtra("yearandterm", backString);
				setResult(RESULTCODE, bIntent);
				finish();
			}
		});
		minute_pv.setOnSelectListener(new onSelectListener()
		{

			@Override
			public void onSelect(String text)
			{
				yearText = text;
			}
		});
		second_pv.setData(terms);
		second_pv.setOnSelectListener(new onSelectListener()
		{

			@Override
			public void onSelect(String text)
			{
				termText = text;
			}
		});
		
	}
}

package edu.xidian.mti1001.virty.activity4th;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import edu.xidian.mti1001.virty.welcome.R;
import edu.xidian.mti1001.virtyandroid.MainTabs;
import edu.xidian.mti1001.virtyandroid.SysApplication;

public class Activity4thSettingDefaultUnit extends Activity implements OnClickListener {
	// 上部“返回”按钮
	private Button buttonBack;
	//“kg”按钮、“Lb”按钮（选择模式）
	private Button buttonKg, buttonLb;
	//显示选择的单位
	private TextView textViewUnit;

	//静态变量
	//默认单位，true代表Kg，false代表Lb
	public static boolean unit = true;
	
	// 记录按钮状态
	private SharedPreferences sp;
	// key常量
	private static final String KEY_UNIT_BUTTON_PRESSED = "unitButtonPressed";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//绑定“设备连接设置”布局
		setContentView(R.layout.activity_4th_setting_default_unit);
		
		//获得控件
		buttonBack = (Button) findViewById(R.id.button4thDefaultUnitBack);
		buttonKg = (Button) findViewById(R.id.button4thDefaultUnitKg);
		buttonLb = (Button) findViewById(R.id.button4thDefaultUnitLb);
		textViewUnit = (TextView) findViewById(R.id.textView4thDefaultUnit);
		
		//添加监听器
		buttonBack.setOnClickListener(this);
		buttonKg.setOnClickListener(this);
		buttonLb.setOnClickListener(this);
		
		//设置默认单位为Kg
		buttonKg.setSelected(true);
		buttonLb.setSelected(false);
		
		// 获得SharedPrerences中，是不是第一次使用，并且根据返回值进行相应的操作
        sp = getSharedPreferences("virtySP", Context.MODE_PRIVATE);
		// 设置默认单位为Kg
		unit = sp.getBoolean(KEY_UNIT_BUTTON_PRESSED, true);
		setUnit(unit);
		
		SysApplication.getInstance().addActivity(this); 
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		//返回按钮
		case R.id.button4thDefaultUnitBack:
			// 跳转到主4“设置”页面
			MainTabs.mTabHost.setCurrentTab(3);
			break;
			
		//“Kg”按钮，更改unit值
		case R.id.button4thDefaultUnitKg:
			setUnit(true);
			break;
			
		//“Lb”按钮，更改unit值
		case R.id.button4thDefaultUnitLb:
			setUnit(false);
			break;

		default:
			break;
		}
	}
	
	// 用户选择模式，程序更改模式并将选择的值保存，true为BMI，false为Fat
	public void setUnit(boolean flag) {
		Activity4thSettingDefaultUnit.unit = flag;
		// 利用SharedPrefrece记录用户选择了哪一种模式
		Editor e = sp.edit();
		e.putBoolean(KEY_UNIT_BUTTON_PRESSED, flag); // BMI为true
		// 必须提交，某则只是表示将要存储
		e.commit();
//		String l = Locale.getDefault().getLanguage().toLowerCase();
//		boolean f = true;
//		if (l.contains("zh")) {
//			// 简体中文
//			f = true;
//		}else if (l.contains("en")) {
//			// 英文
//			f = false;
//		}
		// 更改按钮状态
		if (flag) {
			//kg
			//更改textView的内容
			textViewUnit.setText(getResources().getString(R.string.Activity4SettingDefaultUnitKg));
			buttonKg.setSelected(true);
			buttonLb.setSelected(false);
		} else {
			//更改textView的内容
			textViewUnit.setText(getResources().getString(R.string.Activity4SettingDefaultUnitLb));
			buttonKg.setSelected(false);
			buttonLb.setSelected(true);
		}
	}
	@Override
	public void onBackPressed() {
//		super.onBackPressed();
		// 跳转到主4“设置”页面
		MainTabs.mTabHost.setCurrentTab(3);
	}
}

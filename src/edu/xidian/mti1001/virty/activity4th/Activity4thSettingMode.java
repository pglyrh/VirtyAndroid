package edu.xidian.mti1001.virty.activity4th;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import edu.xidian.mti1001.virty.bluetooth.BluetoothDeviceScan;
import edu.xidian.mti1001.virty.bluetooth.BluetoothLeClass;
import edu.xidian.mti1001.virty.welcome.R;
import edu.xidian.mti1001.virtyandroid.MainTabs;
import edu.xidian.mti1001.virtyandroid.SysApplication;

public class Activity4thSettingMode extends Activity implements OnClickListener {
	// 上部“返回”按钮
	private Button buttonBack;
	//“BMI”按钮、“Fat”按钮（选择健康秤种类）
	private Button buttonBmi, buttonFat;

	//静态变量
	//默认单位，true代表BMI，false代表Fat
	//默认为true
	public static boolean mode = true;
	
	//记录按钮状态
	private SharedPreferences sp;
	//key常量
	private static final String KEY_MODE_BUTTON_PRESSED = "modeButtonPressed";	
	
	// 强制更改
	public static boolean force = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//绑定“设备连接设置”布局
		setContentView(R.layout.activity_4th_setting_mode);
		
		//获得控件
		buttonBack = (Button) findViewById(R.id.button4thModeBack);
		buttonBmi = (Button) findViewById(R.id.button4thModeBmi);
		buttonFat = (Button) findViewById(R.id.button4thModeFat);
		
		//添加监听器
		buttonBack.setOnClickListener(this);
		buttonBmi.setOnClickListener(this);
		buttonFat.setOnClickListener(this);
	
		SysApplication.getInstance().addActivity(this); 
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		// 获得SharedPrerences中
		super.onResume();
		sp = getSharedPreferences("virtySP", Context.MODE_PRIVATE);
		if (!force) {
			// 设置默认模式为BMI
			mode = sp.getBoolean(KEY_MODE_BUTTON_PRESSED, true);
		}
		setMode(mode);			
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		//返回按钮
		case R.id.button4thModeBack:
			// 跳转到主4“设置”页面
			MainTabs.mTabHost.setCurrentTab(3);
			break;
			
		//“Bmi”按钮，更改mode值
		case R.id.button4thModeBmi:
			// 脂肪秤断开
			if (Activity4thSettingDeviceConnect.find) {
				BluetoothLeClass bluetoothLeClass = BluetoothDeviceScan.getBluetoothLeClass();
				bluetoothLeClass.disconnect();
				Activity4thSettingDeviceConnect.find = false;
				force = false;
			}
			setMode(true);
			break;
			
		//“Fat”按钮，更改mode值
		case R.id.button4thModeFat:
			setMode(false);
			break;
		default:
			break;
		}
	}
	
	// 用户选择模式，程序更改模式并将选择的值保存，true为BMI，false为Fat
	public void setMode(boolean flag){
		Activity4thSettingMode.mode = flag;	
		//利用SharedPrefrece记录用户选择了哪一种模式
		Editor e = sp.edit();
		e.putBoolean(KEY_MODE_BUTTON_PRESSED, flag);	//BMI为true		
		//必须提交，某则只是表示将要存储
		e.commit();

		//更改按钮状态
		if (flag) {
			buttonBmi.setSelected(true);
			buttonFat.setSelected(false);
		}else {
			buttonFat.setSelected(true);
			buttonBmi.setSelected(false);
		}		
	}
	
	@Override
	public void onBackPressed() {
//		super.onBackPressed();
		// 跳转到主4“设置”页面
		MainTabs.mTabHost.setCurrentTab(3);
	}
}

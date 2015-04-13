package edu.xidian.mti1001.virtyandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import edu.xidian.mti1001.virty.welcome.R;

public class ActivityDeviceSetting extends Activity implements OnClickListener {
	//选择设备连接设备的方式（蓝牙、手动）
	private Button buttonBluetooth, buttonManual;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//绑定“设备连接”布局
		setContentView(R.layout.activity_device_setting);
		
		//获得控件
		buttonBluetooth = (Button) findViewById(R.id.buttonBluetooth);
		buttonManual = (Button) findViewById(R.id.buttonManual);
		
		//添加监听器
		buttonBluetooth.setOnClickListener(this);
		buttonManual.setOnClickListener(this);
		
		SysApplication.getInstance().addActivity(this); 
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		//开启蓝牙连接模式
		case R.id.buttonBluetooth:
			
			break;
			
		//进入手动输入模式
		case R.id.buttonManual:
			startActivity(new Intent(this, MainTabs.class));
			break;

		default:
			break;
		}
	}
}

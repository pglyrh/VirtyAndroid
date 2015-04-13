package edu.xidian.mti1001.virty.activity4th;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import edu.xidian.mti1001.virty.bluetooth.BluetoothDeviceScan;
import edu.xidian.mti1001.virty.bluetooth.BluetoothLeClass;
import edu.xidian.mti1001.virty.welcome.R;
import edu.xidian.mti1001.virtyandroid.MainTabs;
import edu.xidian.mti1001.virtyandroid.SysApplication;

public class Activity4thSettingDeviceConnect extends Activity implements OnClickListener {
	// 上部“返回”按钮
	private Button buttonBack;
	//“开启蓝牙连接模式”按钮、“进入手动输入模式”按钮
	private Button buttonBluetooth, buttonManual;
	BluetoothDeviceScan bluetoothDeviceScan;
	//连接模式，true为手动，false为连接蓝牙
	boolean deviceMode = true;	//默认为true
	//记录按钮状态
	private SharedPreferences sp;
	// key常量
	private static final String KEY_MODE_BUTTON_PRESSED = "modeButtonPressed";
	
	// 是否为连接第一次
	boolean firstConnect = true;
	public static boolean find = false;
	// Textview
	private static TextView textViewConnectDetail;
	private static TextView textViewDeviceName;
	private String detail;
	private String name;
	private static final int COMPLETED = 0;  
	private Handler searchHandler = new Handler();
	private static Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				System.out.println(".......0");
//				System.out.println("detail: "+detail);
//				System.out.println("name: "+name);
//				textViewConnectDetail.setText(detail);
//				textViewDeviceName.setText(name);
//				bmiView.invalidate();
			}else if(msg.what == 1){
				// 正在搜索
				System.out.println(".......1");
				textViewConnectDetail.setText(R.string.Activity4SettingDeviceConnectSearch);
				textViewDeviceName.setText("");
			}else if ((msg.what == 2)) {
				// 搜索成功
				System.out.println(".......2");
				// 将体重秤模式更改为脂肪秤模式
				Activity4thSettingMode.mode = false;
				Activity4thSettingMode.force = true;
				MainTabs.setButtonSelected(1);
				MainTabs.mTabHost.setCurrentTab(1);
				//利用SharedPrefrece记录用户选择了哪一种模式
//				Editor e = sp.edit();
//				e.putBoolean(KEY_MODE_BUTTON_PRESSED, false);
//				e.commit();
				textViewConnectDetail.setText(R.string.Activity4SettingDeviceConnectSuccess);
				textViewDeviceName.setText(R.string.Activity4SettingDeviceConnectDeviceName);
			}else if ((msg.what == 3)){
				// 未找到
				System.out.println(".......3");
				textViewConnectDetail.setText(R.string.Activity4SettingDeviceConnect);
				textViewDeviceName.setText("");
			}
		}
	};  

	public static int getCompeted(){
		return COMPLETED;
	}
	
	public static Handler getHandler(){
		return handler;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//绑定“设备连接设置”布局
		setContentView(R.layout.activity_4th_setting_device_connect);
		
		//获得控件
		buttonBack = (Button) findViewById(R.id.button4thDeviceConnectBack);
		buttonBluetooth = (Button) findViewById(R.id.button4thDeviceConnectBluetooth);
		buttonManual = (Button) findViewById(R.id.button4thDeviceConnectManual);
		textViewConnectDetail = (TextView) findViewById(R.id.textView4thDeviceConnect);
		textViewDeviceName = (TextView) findViewById(R.id.textView4thDeviceConnectDeviceName);
		
		//添加监听器
		buttonBack.setOnClickListener(this);
		buttonBluetooth.setOnClickListener(this);
		buttonManual.setOnClickListener(this);
		
		SysApplication.getInstance().addActivity(this); 
	}

	// 连接
//	public void connect(){
//		// 连接蓝牙。。。。。
//		bluetoothDeviceScan = new BluetoothDeviceScan(this);
//		Boolean connected = bluetoothDeviceScan.connectToScale();
//		//	必须阻塞
//		if (connected) {
//			//	连接成功
//			this.deviceMode = false;
////		System.out.println("连接成功.。。。。。。。。");
//			textViewConnectDetail.setText(R.string.Activity4SettingDeviceConnectSuccess);
//			textViewDeviceName.setText(R.string.Activity4SettingDeviceConnectDeviceName);
//			// 将体重秤模式更改为脂肪秤模式
//			Activity4thSettingMode.mode = false;
//			sp = getSharedPreferences("virtySP", Context.MODE_PRIVATE);
//			//利用SharedPrefrece记录用户选择了哪一种模式
//			Editor e = sp.edit();
//			e.putBoolean(KEY_MODE_BUTTON_PRESSED, false);
//			e.commit();
//			firstConnect = false;
////		Activity4thSettingMode.setMode(false);
//		}
//		else {
//			//	未连接成功
//			System.out.println("未连接成功.。。。。。。。。");
//			textViewConnectDetail.setText(R.string.Activity4SettingDeviceConnect);
//			textViewDeviceName.setText("");
//		}
//		
//	}
	
	// 等待搜索线程
		private class SearchThread implements Runnable{			
			
			public boolean getFind(){
				return find;
			}
			
			@Override
			public void run() {
				int number = 0;
				textViewConnectDetail.setText(""+R.string.Activity4SettingDeviceConnectSearch);
		        number ++;
		        if(number >= 20){
		        	textViewConnectDetail.setText(""+R.string.Activity4SettingDeviceConnect);
		        	return;
		        }
		        
		        if (!find) {
		        	textViewConnectDetail.setText(""+R.string.Activity4SettingDeviceConnectSuccess);
		        	return;
				}
		  
//		        bluetoothDeviceScan = new BluetoothDeviceScan(Activity4thSettingDeviceConnect.this);
		        
		        searchHandler.postDelayed(this, 1000);			
			}
			
		}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		//返回按钮
		case R.id.button4thDeviceConnectBack:
			// 跳转到主4“设置”页面
			MainTabs.mTabHost.setCurrentTab(3);
			break;
			
		//“开启蓝牙”按钮
		case R.id.button4thDeviceConnectBluetooth:
			// 不够严谨。。。。
			if (!find) {			
				textViewConnectDetail.setText(R.string.Activity4SettingDeviceConnectSearch);
				textViewDeviceName.setText("");
//				
				// 连接蓝牙。。。。。
				bluetoothDeviceScan = new BluetoothDeviceScan(this);
				bluetoothDeviceScan.connectToScale();
				
//				Boolean connected = bluetoothDeviceScan.connectToScale();
				//	必须阻塞
//				if (connected) {
//					//	连接成功
//					this.deviceMode = false;
////				System.out.println("连接成功.。。。。。。。。");
//					textViewConnectDetail.setText(R.string.Activity4SettingDeviceConnectSuccess);
//					textViewDeviceName.setText(R.string.Activity4SettingDeviceConnectDeviceName);
//					// 将体重秤模式更改为脂肪秤模式
//					Activity4thSettingMode.mode = false;
//					sp = getSharedPreferences("virtySP", Context.MODE_PRIVATE);
//					//利用SharedPrefrece记录用户选择了哪一种模式
//					Editor e = sp.edit();
//					e.putBoolean(KEY_MODE_BUTTON_PRESSED, false);
//					e.commit();
//					firstConnect = false;
////				Activity4thSettingMode.setMode(false);
//				}
//				else {
//					//	未连接成功
//					System.out.println("未连接成功.。。。。。。。。");
//					textViewConnectDetail.setText(R.string.Activity4SettingDeviceConnect);
//					textViewDeviceName.setText("");
//				}
			}else {
				textViewConnectDetail.setText(R.string.Activity4SettingDeviceConnectDeviceConnected);
				textViewDeviceName.setText("");
			}
			break;
		//“手动模式”按钮
		case R.id.button4thDeviceConnectManual:
			// 断开蓝牙
			if (find) {
				BluetoothLeClass bluetoothLeClass = BluetoothDeviceScan.getBluetoothLeClass();
				bluetoothLeClass.disconnect();
				find = false;
			}
			// 变更连接方式
			this.deviceMode = true;
			// 更改显示内容
			textViewConnectDetail.setText(R.string.Activity4SettingDeviceConnect);
			textViewDeviceName.setText("");
			// 体重秤模式强制解除
			Activity4thSettingMode.force = false;
			// 跳转到主2页面
			MainTabs.mTabHost.setCurrentTab(1);
			MainTabs.setButtonSelected(1);
			break;

		default:
			break;
		}
	}
	
	@Override
	public void onBackPressed() {
//		super.onBackPressed();
		// 跳转到主4“设置”页面
		MainTabs.mTabHost.setCurrentTab(3);
	}
}

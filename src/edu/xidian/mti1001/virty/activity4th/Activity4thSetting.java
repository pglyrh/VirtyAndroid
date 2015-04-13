package edu.xidian.mti1001.virty.activity4th;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import edu.xidian.mti1001.virty.welcome.R;
import edu.xidian.mti1001.virtyandroid.MainTabs;
import edu.xidian.mti1001.virtyandroid.SysApplication;

public class Activity4thSetting extends Activity {
	// 图片按钮
	private ImageView imageView1Device, imageView2Unit, imageView3Mode,
			imageView4Help, imageView5Buy;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_4th_setting);

		// 获得图片按钮
		imageView1Device = (ImageView) findViewById(R.id.imageViewSetting1Device);
		imageView2Unit = (ImageView) findViewById(R.id.imageViewSetting2Unit);
		imageView3Mode = (ImageView) findViewById(R.id.imageViewSetting3Mode);
		imageView4Help = (ImageView) findViewById(R.id.imageViewSetting4Help);
		imageView5Buy = (ImageView) findViewById(R.id.imageViewSetting5Buy);

		// 添加“点击事件”监听器
//	   10（主4的设备连接） 11（主4的默认单位）  12（主4的健康秤模式） 13（主4的帮助）
		//设备连接
		imageView1Device.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//跳转到“设备连接”子页面
				MainTabs.mTabHost.setCurrentTab(10);
			}
		});
		imageView2Unit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//跳转到“默认单位”子页面
				MainTabs.mTabHost.setCurrentTab(11);
			}
		});
		imageView3Mode.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//跳转到“健康秤模式”子页面
				MainTabs.mTabHost.setCurrentTab(12);
			}
		});
		imageView4Help.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//跳转到“帮助”子页面
				MainTabs.mTabHost.setCurrentTab(13);
			}
		});
		imageView5Buy.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//打开网站
				Intent intent = new Intent();
				intent.setData(Uri.parse("http://www.huachao.com"));
				intent.setAction(Intent.ACTION_VIEW);
				startActivity(intent); //启动浏览器
			}
		});
		
		SysApplication.getInstance().addActivity(this); 
		
	}
	
	// 记录上一次按下的时间，若连续按下则退出程序
	private long lastClickTime = 0;

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// System.out.println("................back");
		if (lastClickTime <= 0) {
			Toast.makeText(this, getResources().getString(R.string.ToastPressAgain), Toast.LENGTH_SHORT).show();
			lastClickTime = System.currentTimeMillis();
		} else {
			long currentClickTime = System.currentTimeMillis();
			if ((currentClickTime - lastClickTime) < 1000) {
				// 退出
				// finish();
				// 关闭整个程序
				SysApplication.getInstance().exit();
			} else {
				Toast.makeText(this, getResources().getString(R.string.ToastPressAgain), Toast.LENGTH_SHORT).show();
				lastClickTime = System.currentTimeMillis();
			}
		}
	}
}

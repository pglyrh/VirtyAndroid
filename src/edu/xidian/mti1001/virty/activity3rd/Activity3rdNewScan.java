package edu.xidian.mti1001.virty.activity3rd;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.Toast;
import edu.xidian.mti1001.virty.welcome.R;
import edu.xidian.mti1001.virtyandroid.MainTabs;
import edu.xidian.mti1001.virtyandroid.SysApplication;

public class Activity3rdNewScan extends Activity implements OnClickListener {
	// 定义四个“按钮”控件
	private RelativeLayout layoutHealthTodayRecipe, layoutFitnessScheme,
			layoutFitnessInfo, layoutHealthPedometer;
	
	Resources resources;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_3rd_news_scan);
		
		//获得控件
		layoutHealthTodayRecipe = (RelativeLayout) findViewById(R.id.layoutHealthTodayRecipe);
		layoutFitnessScheme = (RelativeLayout) findViewById(R.id.layoutFitnessScheme);
		layoutFitnessInfo = (RelativeLayout) findViewById(R.id.layoutFitnessInfo);
		layoutHealthPedometer = (RelativeLayout) findViewById(R.id.layoutHealthPedometer);
		
		//添加监听器
		layoutHealthTodayRecipe.setOnClickListener(this);
		layoutFitnessScheme.setOnClickListener(this);
		layoutFitnessInfo.setOnClickListener(this);
		layoutHealthPedometer.setOnClickListener(this);
		SysApplication.getInstance().addActivity(this);
		
		resources = getResources();
	}

//	7（主3的今日食谱）  8（主3的健身方案）  9（主3的健康资讯）
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		//今日食谱
		case R.id.layoutHealthTodayRecipe:
			//跳转到“今日食谱”子页面
			MainTabs.mTabHost.setCurrentTab(7);
			break;
		
		//健身方案
		case R.id.layoutFitnessScheme:
			//跳转到“健身方案”子页面
			MainTabs.mTabHost.setCurrentTab(8);
			break;
			
		//健康咨询
		case R.id.layoutFitnessInfo:
			//跳转到“健康资讯”子页面
			MainTabs.mTabHost.setCurrentTab(9);
			break;
			
		//计步器
		case R.id.layoutHealthPedometer:
			//跳转到“计步器”子页面
			startActivity(new Intent(this, Activity3rdNewScanPedome.class));
			break;

		default:
			break;
		}
	}

	// 记录上一次按下的时间，若连续按下则退出程序
	private long lastClickTime = 0;

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// System.out.println("................back");
		if (lastClickTime <= 0) {
			Toast.makeText(this, resources.getString(R.string.ToastPressAgain), Toast.LENGTH_SHORT).show();
			lastClickTime = System.currentTimeMillis();
		} else {
			long currentClickTime = System.currentTimeMillis();
			if ((currentClickTime - lastClickTime) < 1000) {
				// 退出
				// finish();
				// 关闭整个程序
				SysApplication.getInstance().exit();
			} else {
				Toast.makeText(this, resources.getString(R.string.ToastPressAgain), Toast.LENGTH_SHORT).show();
				lastClickTime = System.currentTimeMillis();
			}
		}
	}
}

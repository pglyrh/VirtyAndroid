package edu.xidian.mti1001.virty.activity4th;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import edu.xidian.mti1001.virty.welcome.R;
import edu.xidian.mti1001.virtyandroid.MainTabs;
import edu.xidian.mti1001.virtyandroid.SysApplication;

public class Activity4thSettingHelp extends Activity implements OnClickListener {
	// 上部“返回”按钮
	private Button buttonBack;
	// WebView显示帮助文档html
	private ListView help;
	// ListView的适配器
	private HelpListViewAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 绑定“帮助”布局
		setContentView(R.layout.activity_4th_setting_help);

		// 获得控件
		buttonBack = (Button) findViewById(R.id.button4thHelpBack);
		help = (ListView) findViewById(R.id.listView4thHelp);

		// 添加监听器
		buttonBack.setOnClickListener(this);

		adapter = new HelpListViewAdapter(this);
		help.setAdapter(adapter);

		SysApplication.getInstance().addActivity(this);
	}

	// 返回按钮
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// super.onBackPressed();
		// 跳转到主4“设置”页面
		MainTabs.mTabHost.setCurrentTab(3);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		// 返回按钮
		case R.id.button4thHelpBack:
			// 跳转到主4“设置”页面
			MainTabs.mTabHost.setCurrentTab(3);
			break;

		default:
			break;
		}
	}
}

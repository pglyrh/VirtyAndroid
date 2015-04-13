package edu.xidian.mti1001.virty.activity3rd;

//import java.net.IDN;
import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import edu.xidian.mti1001.virty.activity1st.Activity1stUserList;
import edu.xidian.mti1001.virty.welcome.R;
import edu.xidian.mti1001.virtyandroid.MainTabs;
import edu.xidian.mti1001.virtyandroid.SysApplication;

public class Activity3rdNewScanFitnessScheme extends Activity implements
		OnClickListener {
	// 上部两个“返回”按钮
	private Button buttonBack;
	// 显示用户名
	private TextView textViewUserName;
	//健身项目频率、名字、数量
	private TextView gym1f, gym1n, gym1q;
	private TextView gym2f, gym2n, gym2q;
	private TextView home1f, home1n, home1q;
	private TextView home2f, home2n, home2q;
	// 选择的用户
	private String chooseUserName = "";
	// 用户性别
	private int sex;
	// 用户id
	public static int ID = 0;
	
	//健身列表
	HealthSchemeArray healthSchemeArray;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_3rd_news_scan_fitness_scheme);

		healthSchemeArray = new HealthSchemeArray();
		
		// 获得控件
		buttonBack = (Button) findViewById(R.id.button3rdFitnessSchemeBack);
		textViewUserName = (TextView) findViewById(R.id.textViewFitnessSchemeUserName);

		//健身项目
		gym1f = (TextView) findViewById(R.id.textViewFitnessSchemeGym1Frequency);
		gym1n = (TextView) findViewById(R.id.textViewFitnessSchemeGym1Name);
		gym1q = (TextView) findViewById(R.id.textViewFitnessSchemeGym1Quantity);
		gym2f = (TextView) findViewById(R.id.textViewFitnessSchemeGym2Frequency);
		gym2n = (TextView) findViewById(R.id.textViewFitnessSchemeGym2Name);
		gym2q = (TextView) findViewById(R.id.textViewFitnessSchemeGym2Quantity);
		home1f = (TextView) findViewById(R.id.textViewFitnessSchemeHome1Frequency);
		home1n = (TextView) findViewById(R.id.textViewFitnessSchemeHome1Name);
		home1q = (TextView) findViewById(R.id.textViewFitnessSchemeHome1Quantity);
		home2f = (TextView) findViewById(R.id.textViewFitnessSchemeHome2Frequency);
		home2n = (TextView) findViewById(R.id.textViewFitnessSchemeHome2Name);
		home2q = (TextView) findViewById(R.id.textViewFitnessSchemeHome2Quantity);
		

		// 添加监听器
		buttonBack.setOnClickListener(this);
		SysApplication.getInstance().addActivity(this); 
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		// 获得string.xml中的内容
		Resources res = getResources();
		String string = res.getString(R.string.Activity3HealthInfoPlan);
		
		// 判空
		if (Activity1stUserList.CHOOSE_USER_NAME == null) {
			// 设置姓名
			textViewUserName.setText("" + string);
		}else {
			// 更新用户名
			// 设置姓名
			chooseUserName = Activity1stUserList.CHOOSE_USER_NAME;
			textViewUserName.setText("" + chooseUserName + string);
			//制定计划
			customScheme();
		}
	}
	
	// 7（主3的今日食谱） 8（主3的健身方案） 9（主3的健康资讯） 10（主3的计步器）
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		// 今日食谱
		case R.id.button3rdFitnessSchemeBack:
			// 跳转到主3“健康时讯”页面
			MainTabs.mTabHost.setCurrentTab(2);
			break;

		default:
			break;
		}
	}
	
	//定制健身方案
	public void customScheme(){
		//获得性别
		if (Activity1stUserList.SEX.equals("男")) {
			this.sex = 0;
		}else if (Activity1stUserList.SEX.equals("女")) {
			this.sex = 1;
		}
		
//		System.out.println("id..........."+ID);
		
		//根据用户存入数据给定的id。。制定方案。。此处若有算法，应该可根据身体指标
		// 不可随机生成，一个用户只能是其中一个
		// 先假设都是取第一个方案
		int i = ID%4;
		// 获得健身房		
		HealthScheme healthScheme = healthSchemeArray.getSchemes(sex, 0).get(i);
		gym1f.setText(healthScheme.getFrequency());
		gym1n.setText(healthScheme.getSchemeName());
		gym1q.setText(healthScheme.getQuantity());
		healthScheme = healthSchemeArray.getSchemes(sex, 0).get((i+1)%4);
		gym2f.setText(healthScheme.getFrequency());
		gym2n.setText(healthScheme.getSchemeName());
		gym2q.setText(healthScheme.getQuantity());
		// 获得家具		
		healthScheme = healthSchemeArray.getSchemes(sex, 1).get(i);
		home1f.setText(healthScheme.getFrequency());
		home1n.setText(healthScheme.getSchemeName());
		home1q.setText(healthScheme.getQuantity());
		healthScheme = healthSchemeArray.getSchemes(sex, 1).get((i+1)%4);
		home2f.setText(healthScheme.getFrequency());
		home2n.setText(healthScheme.getSchemeName());
		home2q.setText(healthScheme.getQuantity());
	}
	
	@Override
	public void onBackPressed() {
//		super.onBackPressed();
		// 跳转到主3“健康时讯”页面
		MainTabs.mTabHost.setCurrentTab(2);
	}

}

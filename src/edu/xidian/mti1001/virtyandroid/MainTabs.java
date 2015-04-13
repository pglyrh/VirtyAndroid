package edu.xidian.mti1001.virtyandroid;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TabWidget;
import edu.xidian.mti1001.virty.activity1st.Activity1stUserList;
import edu.xidian.mti1001.virty.activity1st.Activity1stUserListAdd;
import edu.xidian.mti1001.virty.activity1st.Activity1stUserListEdit;
import edu.xidian.mti1001.virty.activity1st.Activity1stUserListEditList;
import edu.xidian.mti1001.virty.activity2nd.Activity2ndWeightRecord;
import edu.xidian.mti1001.virty.activity3rd.Activity3rdNewScan;
import edu.xidian.mti1001.virty.activity3rd.Activity3rdNewScanFitnessInfo;
import edu.xidian.mti1001.virty.activity3rd.Activity3rdNewScanFitnessScheme;
import edu.xidian.mti1001.virty.activity3rd.Activity3rdNewScanTodayRecipe;
import edu.xidian.mti1001.virty.activity4th.Activity4thSetting;
import edu.xidian.mti1001.virty.activity4th.Activity4thSettingDefaultUnit;
import edu.xidian.mti1001.virty.activity4th.Activity4thSettingDeviceConnect;
import edu.xidian.mti1001.virty.activity4th.Activity4thSettingHelp;
import edu.xidian.mti1001.virty.activity4th.Activity4thSettingMode;
//import android.support.v4.app.FragmentActivity;
import edu.xidian.mti1001.virty.welcome.R;

//现在用TabHost+Fragment替代
public class MainTabs extends TabActivity {
	//设置为静态变量，为了能够在其他类中使用，因为需要底部导航一直存在
	public static TabHost mTabHost;
	public static TabHost getmTabHost() { 
        return mTabHost; 
    }
	public static Button[] mainButtons;
	public static Button[] getmainButtons() { 
		return mainButtons; 
	}
	private Button buttonTab1, buttonTab2, buttonTab3,buttonTab4;
//	private Button[] mainButtons= new Button[]{buttonTab1, buttonTab2, buttonTab3,buttonTab4};
//	private Button[] mainButtons;
    
    @Override
    protected void onCreate(Bundle arg0) {
    	// TODO Auto-generated method stub
    	super.onCreate(arg0);
    	setContentView(R.layout.main_tabs); 
        mTabHost = getTabHost(); 
        final TabWidget tabWidget = mTabHost.getTabWidget(); 
        tabWidget.setStripEnabled(false);// 圆角边线不启用 
        
//      mTabHost.addTab(mTabHost.newTabSpec("TAG1").setIndicator("0").setContent(new Intent(this, Activity1stUserList.class)));
//      跳转页面：0（主1用户）  1（主2称重）  2（主3健康时讯）  3（主4设置）   4（主1的编辑用户(列表)）  5（主1的添加用户） 6（5的编辑用户）
//            7（主3的今日食谱）  8（主3的健身方案）  9（主3的健康资讯）
//  		   10（主4的设备连接） 11（主4的默认单位）  12（主4的健康秤模式） 13（主4的帮助）
        //添加n个tab选项卡，定义他们的tab名，指示名，目标屏对应的类 
        mTabHost.addTab(mTabHost.newTabSpec("TAG1").setIndicator("0").setContent(new Intent(this, Activity1stUserList.class))); 
        mTabHost.addTab(mTabHost.newTabSpec("TAG2").setIndicator("1").setContent(new Intent(this, Activity2ndWeightRecord.class))); 
        mTabHost.addTab(mTabHost.newTabSpec("TAG3").setIndicator("2").setContent(new Intent(this, Activity3rdNewScan.class))); 
        mTabHost.addTab(mTabHost.newTabSpec("TAG4").setIndicator("3").setContent(new Intent(this, Activity4thSetting.class)));
        
        //暂时解决第一页点击“添加”、“修改”，导航条不消失的问题
        mTabHost.addTab(mTabHost.newTabSpec("TAG11").setIndicator("4").setContent(new Intent(this, Activity1stUserListEditList.class)));
        mTabHost.addTab(mTabHost.newTabSpec("TAG12").setIndicator("5").setContent(new Intent(this, Activity1stUserListAdd.class)));
        mTabHost.addTab(mTabHost.newTabSpec("TAG13").setIndicator("6").setContent(new Intent(this, Activity1stUserListEdit.class)));
        
        //主3的跳转
        //今日食谱
        mTabHost.addTab(mTabHost.newTabSpec("TAG31").setIndicator("7").setContent(new Intent(this, Activity3rdNewScanTodayRecipe.class)));
        //健身方案
        mTabHost.addTab(mTabHost.newTabSpec("TAG32").setIndicator("8").setContent(new Intent(this, Activity3rdNewScanFitnessScheme.class)));
        //健康资讯
        mTabHost.addTab(mTabHost.newTabSpec("TAG33").setIndicator("9").setContent(new Intent(this, Activity3rdNewScanFitnessInfo.class)));
//        mTabHost.addTab(mTabHost.newTabSpec("TAG34").setIndicator("10").setContent(new Intent(this, Activity1stUserListEdit.class)));
        
        //主4的跳转
        //设备连接
        mTabHost.addTab(mTabHost.newTabSpec("TAG41").setIndicator("10").setContent(new Intent(this, Activity4thSettingDeviceConnect.class)));
        //默认单位
        mTabHost.addTab(mTabHost.newTabSpec("TAG42").setIndicator("11").setContent(new Intent(this, Activity4thSettingDefaultUnit.class)));
        //健康秤模式
        mTabHost.addTab(mTabHost.newTabSpec("TAG43").setIndicator("12").setContent(new Intent(this, Activity4thSettingMode.class)));
        //帮助
        mTabHost.addTab(mTabHost.newTabSpec("TAG44").setIndicator("13").setContent(new Intent(this, Activity4thSettingHelp.class)));
        
        // 视觉上,用单选按钮替代TabWidget  
        buttonTab1 = (Button) findViewById(R.id.buttonTab1); 
        buttonTab2 = (Button) findViewById(R.id.buttonTab2); 
        buttonTab3 = (Button) findViewById(R.id.buttonTab3); 
        buttonTab4 = (Button) findViewById(R.id.buttonTab4);
        
        mainButtons= new Button[]{buttonTab1,buttonTab2,buttonTab3,buttonTab4};
        
        //为每一个按钮添加点击事件监视器，并实现（跳转到相应页面）
		buttonTab1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				mTabHost.setCurrentTab(0);
				setButtonSelected(0);
			}
		});
		buttonTab2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				mTabHost.setCurrentTab(1);
				setButtonSelected(1);
			}
		});
		buttonTab3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				mTabHost.setCurrentTab(2);
				setButtonSelected(2);
			}
		});
		buttonTab4.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				mTabHost.setCurrentTab(3);
				setButtonSelected(3);
			}
		});
           
        // 设置当前显示哪一个标签 
        setButtonSelected(0);
        
        // 遍历tabWidget每个标签，设置背景图片 无 
        for (int i = 0; i < tabWidget.getChildCount(); i++) { 
            View vv = tabWidget.getChildAt(i); 
            vv.getLayoutParams().height = 45; 
            // vv.getLayoutParams().width = 65; 
            vv.setBackgroundDrawable(null); 
        } 
//      findViewById(R.id.tab_icon_brand).setOnClickListener(this); 
        SysApplication.getInstance().addActivity(this); 
    }
    
    //以下的函数让button选中时的状态为selected，其他按钮为false
    //返回主页面的函数，都不再简单用mTabHost.setCurrentTab(i)，改用以下函数
    public static void setButtonSelected(int i){
    	mTabHost.setCurrentTab(i);
    	//根据传入的参数i，令i为true其余为false
    	for (int j = 0; j < mainButtons.length; j++) {
			if (i == j) {
				mainButtons[j].setSelected(true);
			}else {				
				mainButtons[j].setSelected(false);				
			}
		}
//    	System.out.println("maintab: "+mainButtons.length);
    }
}

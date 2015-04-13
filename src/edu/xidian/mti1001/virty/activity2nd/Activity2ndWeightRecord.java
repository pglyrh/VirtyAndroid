package edu.xidian.mti1001.virty.activity2nd;

import java.util.ArrayList;

import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Toast;
import edu.xidian.mti1001.virty.activity4th.Activity4thSettingMode;
import edu.xidian.mti1001.virty.welcome.R;
import edu.xidian.mti1001.virty.welcome.WelcomePagerViewerAdapter;
import edu.xidian.mti1001.virtyandroid.SysApplication;

public class Activity2ndWeightRecord extends Activity {
    private ViewPager pager = null;
    private WelcomePagerViewerAdapter adapter;
    //@deprecated Use the new {@link Fragment} and {@link FragmentManager} APIs
    LocalActivityManager manager = null;

    //切换的list链表，选择不同的体重秤模式，list中内容不同
    ArrayList<View> list;
    
    Resources resources;
    //模式，0代表体重秤，1代表脂肪秤
//    public static int MODE = 0;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_2nd_weight_record);
		manager = new LocalActivityManager(this , true);
        manager.dispatchCreate(savedInstanceState);

		initPagerViewer();
		SysApplication.getInstance().addActivity(this); 
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		resources = getResources();
		//必须调用以下方法，不然管理的子activity会不调用onResume
		manager.dispatchResume();
		initPagerViewer();
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
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
//		manager.dispatchPause(isFinishing());
		System.out.println("activity2nd on pause............");
		Activity2ndWeightRecordFat.front = false;
	}
//	
//	@Override
//	protected void onStop() {
//		// TODO Auto-generated method stub
//		super.onStop();
//		manager.dispatchStop();
//	}
//	
//	@Override
//	protected void onDestroy() {
//		// TODO Auto-generated method stub
//		super.onDestroy();
//		manager.dispatchDestroy(isFinishing());
//	}
	
	private View getView(String id, Intent intent) {
        return manager.startActivity(id, intent).getDecorView();
    }
	
	
	private void initPagerViewer() {
		// TODO Auto-generated method stub
        pager = (ViewPager) findViewById(R.id.viewPageActivityWeightRecord);
        
//        System.out.println("---------------------mode:"+Activity4thSettingMode.mode);
        
        if (Activity4thSettingMode.mode) {			
        	//普通体重秤
        	list = new ArrayList<View>();
        	Intent intent = new Intent(Activity2ndWeightRecord.this, Activity2ndWeightRecordBmi.class);
        	list.add(getView("BMI", intent));
        	Intent intent2 = new Intent(Activity2ndWeightRecord.this, Activity2ndWeightRecordBmiTrend.class);
        	list.add(getView("TREND", intent2));
        	Intent intent3 = new Intent(Activity2ndWeightRecord.this, Activity2ndWeightRecordBmiHistory.class);
        	list.add(getView("HISTORY", intent3));
        	//startActivity(id, intent).getDecorView();
		}else {			
			//Fat秤
			list = new ArrayList<View>();
        	Intent intent = new Intent(Activity2ndWeightRecord.this, Activity2ndWeightRecordFat.class);
        	list.add(getView("BMI", intent));
        	Intent intent2 = new Intent(Activity2ndWeightRecord.this, Activity2ndWeightRecordFatTrend.class);
        	list.add(getView("TREND", intent2));
        	Intent intent3 = new Intent(Activity2ndWeightRecord.this, Activity2ndWeightRecordFatHistory.class);
        	list.add(getView("HISTORY", intent3));
		}

        pager.setAdapter(new WelcomePagerViewerAdapter(list));
        pager.setCurrentItem(0);
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
}

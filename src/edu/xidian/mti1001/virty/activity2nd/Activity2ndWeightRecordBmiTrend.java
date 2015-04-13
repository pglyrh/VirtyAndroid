package edu.xidian.mti1001.virty.activity2nd;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import edu.xidian.mti1001.virty.activity1st.Activity1stUserList;
import edu.xidian.mti1001.virty.activity4th.Activity4thSettingDefaultUnit;
import edu.xidian.mti1001.virty.welcome.R;
import edu.xidian.mti1001.virtyandroid.ScreenShot;
import edu.xidian.mti1001.virtyandroid.SysApplication;
import edu.xidian.mti1001.virtyandroid.UserDatabaseHelper;

public class Activity2ndWeightRecordBmiTrend extends Activity implements OnClickListener{
	// 分享按钮
	private Button buttonShare;
	// 上排（周期）
	private ImageView[] periods;
	private ImageView buttonBmiTrendWeek, buttonBmiTrendMonth,
			buttonBmiTrendSeanson, buttonBmiTrendYear;
	//单位textView
	private TextView textViewBmiTrendUnit;
	private boolean unit;
	//时间戳，分享图片时候使用
	private String timeStamp;	
	//绘制趋势
//	//画图区域
	public static ShowTrend showTrend;
	
	Resources resources;
	
//	private RelativeLayout relativeLayoutBackground;
//	private ImageView imageViewBmiTrendBackground;
//	//长、宽
//	private int height, width;
	
	// 数据库对象
	public static UserDatabaseHelper db;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_2nd_weight_record_bmi_trend);
		buttonBmiTrendWeek = (ImageView) findViewById(R.id.buttonBmiTrendWeek);
		buttonBmiTrendMonth = (ImageView) findViewById(R.id.buttonBmiTrendMonth);
		buttonBmiTrendSeanson = (ImageView) findViewById(R.id.buttonBmiTrendSeason);
		buttonBmiTrendYear = (ImageView) findViewById(R.id.buttonBmiTrendYear);
		buttonShare = (Button) findViewById(R.id.buttonBmiTrendShare);
		textViewBmiTrendUnit = (TextView) findViewById(R.id.textViewBmiTrendUnit);
		//获取绘图区域类
		showTrend = (ShowTrend) findViewById(R.id.showTrend);
		
		//添加监听器		
		buttonBmiTrendWeek.setOnClickListener(this);
		buttonBmiTrendMonth.setOnClickListener(this);
		buttonBmiTrendSeanson.setOnClickListener(this);
		buttonBmiTrendYear.setOnClickListener(this);
		buttonShare.setOnClickListener(this);
		
		//将按钮添加到数组中
		periods = new ImageView[]{buttonBmiTrendWeek, buttonBmiTrendMonth, 
				buttonBmiTrendSeanson,buttonBmiTrendYear};
		
		//初始按钮状态
		setPeriodButtonSelected(0);
		
		//添加到管理类，用于销毁
		SysApplication.getInstance().addActivity(this); 
		
		//数据库初始化
		db = new UserDatabaseHelper(this, "VirtyDB", null, 1);
//		imageViewBmiTrendBackground = (ImageView) findViewById(R.id.imageViewBmiTrendBackground);
//		relativeLayoutBackground = (RelativeLayout) findViewById(R.id.relativeLayoutBmiTrendBackground);
//		int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED); 
//		int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED); 
//		relativeLayoutBackground.measure(w, h); 
////		height =relativeLayoutBackground.getMeasuredHeight(); 
////		width =relativeLayoutBackground.getMeasuredWidth(); 
//		height =showTrend.getMeasuredHeight(); 
//		width =showTrend.getMeasuredWidth(); 
//		System.out.println("height: "+height+" width: "+width);

	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		resources = getResources();
		refresh();
	}
	
	// 刷新页面
	private void refresh(){
		// 获得默认单位并更新单位
		unit = Activity4thSettingDefaultUnit.unit;
		if (unit) {
			// kg
			textViewBmiTrendUnit.setText("(kg)");
		}else {
			// lb
			textViewBmiTrendUnit.setText("(lb)");
		}
		// 初始化数据
		getRecord();
	}
	
	//以下的函数让button（周期）选中时的状态为selected，其他按钮为false
    public void setPeriodButtonSelected(int i){
    	//根据传入的参数i，令i为true其余为false
    	for (int j = 0; j < periods.length; j++) {
    		if (i == j) {
    			periods[j].setSelected(true);
    			//更改showTrend周期
    			showTrend.setPeriod(i);
    		}else {				
    			periods[j].setSelected(false);				
    		}
    	}
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		//分享
		case R.id.buttonBmiTrendShare:
			// 取得外部存储所在目录
			File dir;
			Intent intent = new Intent(Intent.ACTION_SEND);
			getCurrentTime();
			String fileName = "BmiTrendShare"+timeStamp+".png";
			//屏幕截图
//			Bitmap bitmap = ScreenShot.takeScreenShot(this);
			ScreenShot.shoot(this, fileName);
//			ScreenShot.takeScreenShot(LayoutInflater.View.);
			// 取得外部存储所在目录
			dir = Environment.getExternalStorageDirectory();
			String path = dir.getAbsolutePath() + "/virty/"+fileName;
			File f = new File(path);
			// 确定文件是否存在
			if (f != null && f.exists() && f.isFile()) {
				// Toast.makeText(this, "f"+f.getPath(),
				// Toast.LENGTH_SHORT).show();
				// 分享图片
				intent.setType("image/*");
				Uri u = Uri.fromFile(f);
				intent.putExtra(Intent.EXTRA_STREAM, u);
			} else {
				// 纯文本
				intent.setType("text/plain");
			}
			intent.putExtra(Intent.EXTRA_SUBJECT, "Share");
			intent.putExtra(Intent.EXTRA_TEXT, "#健康可以衡量，华潮健康秤#");
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(Intent.createChooser(intent, resources.getString(R.string.Share)));
			break;

		case R.id.buttonBmiTrendWeek:
			setPeriodButtonSelected(0);
			break;
			
		case R.id.buttonBmiTrendMonth:
			setPeriodButtonSelected(1);
			break;
			
		case R.id.buttonBmiTrendSeason:
			setPeriodButtonSelected(2);
			break;
			
		case R.id.buttonBmiTrendYear:
			setPeriodButtonSelected(3);
			break;
		default:
			break;
		}
	}
	
	// 获得当前日期、时间
	public void getCurrentTime() {
		SimpleDateFormat formatterTimeStamp = new SimpleDateFormat(
				"yyyyMMddHHmm", Locale.CHINESE);
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		timeStamp = formatterTimeStamp.format(curDate);
		// Toast.makeText(this, "date: "+dateString+" time: "+timeString,
		// Toast.LENGTH_SHORT).show();
	}
	
	//获得用户数据
	public static void getRecord() {
		SQLiteDatabase dbReadDatabase = db.getReadableDatabase();
		// query(String table, String[] columns, String selection,String[]
		// selectionArgs,String groupBy, String having,String orderBy)
		//先将数据清空
		showTrend.getDatas().clear();
		//设置单位
		showTrend.setUnit(Activity4thSettingDefaultUnit.unit);
		if (Activity1stUserList.CHOOSE_USER_NAME == null) {
			// 未选择任何用户
//			showTrend.getDatas().clear();
		} else {
			//选择了用户，需要的数据为date和weight
			Cursor cursor = dbReadDatabase.query("userHealthRecord", null, "name=?",
					new String[] { Activity1stUserList.CHOOSE_USER_NAME }, null, null, null);
			if (cursor.getCount()>0) {
				cursor.moveToFirst();
				while (!cursor.isAfterLast()) {
					TrendCellData history = new TrendCellData(
							cursor.getString(cursor.getColumnIndex("date")),
							cursor.getFloat(cursor.getColumnIndex("weight")));
//					System.out.println("............................date: "+history.date);
					showTrend.getDatas().add(history);
//					data.add(history);
					cursor.moveToNext();
				}
			}
		}			
		//更新画布区域
		showTrend.invalidate();

		// 关闭数据库
		dbReadDatabase.close();
	}
}

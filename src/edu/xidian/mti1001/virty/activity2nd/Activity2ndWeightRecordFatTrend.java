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

public class Activity2ndWeightRecordFatTrend extends Activity implements
		OnClickListener {
	// 分享按钮
	private Button buttonShare;
	// 底部一排按钮组（各种指标）
	private Button[] segments;
	private Button buttonSeg1, buttonSeg2, buttonSeg3, buttonSeg4, buttonSeg5,
			buttonSeg6;
	// 上排（周期）
	private ImageView[] periods;
	private ImageView buttonFatTrendWeek, buttonFatTrendMonth,
			buttonFatTrendSeanson, buttonFatTrendYear;

	private String timeStamp;
	//单位textView
	private TextView textViewFatTrendUnit;
	// 绘制趋势
	// 画图区域
	public static ShowFatTrend showFatTrend;
	// 数据库对象
	public static UserDatabaseHelper db;

	Resources resources;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_2nd_weight_record_fat_trend);

		// 获得控件
		buttonShare = (Button) findViewById(R.id.buttonFatTrendShare);
		buttonSeg1 = (Button) findViewById(R.id.buttonFatTrendSeg1);
		buttonSeg2 = (Button) findViewById(R.id.buttonFatTrendSeg2);
		buttonSeg3 = (Button) findViewById(R.id.buttonFatTrendSeg3);
		buttonSeg4 = (Button) findViewById(R.id.buttonFatTrendSeg4);
		buttonSeg5 = (Button) findViewById(R.id.buttonFatTrendSeg5);
		buttonSeg6 = (Button) findViewById(R.id.buttonFatTrendSeg6);

		buttonFatTrendWeek = (ImageView) findViewById(R.id.buttonFatTrendWeek);
		buttonFatTrendMonth = (ImageView) findViewById(R.id.buttonFatTrendMonth);
		buttonFatTrendSeanson = (ImageView) findViewById(R.id.buttonFatTrendSeason);
		buttonFatTrendYear = (ImageView) findViewById(R.id.buttonFatTrendYear);

		// 添加监听器
		buttonSeg1.setOnClickListener(this);
		buttonSeg2.setOnClickListener(this);
		buttonSeg3.setOnClickListener(this);
		buttonSeg4.setOnClickListener(this);
		buttonSeg5.setOnClickListener(this);
		buttonSeg6.setOnClickListener(this);
		buttonShare.setOnClickListener(this);

		buttonFatTrendWeek.setOnClickListener(this);
		buttonFatTrendMonth.setOnClickListener(this);
		buttonFatTrendSeanson.setOnClickListener(this);
		buttonFatTrendYear.setOnClickListener(this);

		// 将按钮添加到数组中
		segments = new Button[] { buttonSeg1, buttonSeg2, buttonSeg3,
				buttonSeg4, buttonSeg5, buttonSeg6 };
		periods = new ImageView[] { buttonFatTrendWeek, buttonFatTrendMonth,
				buttonFatTrendSeanson, buttonFatTrendYear };

		textViewFatTrendUnit = (TextView) findViewById(R.id.textViewFatTrendUnit);
		// 获取绘图区域类
		showFatTrend = (ShowFatTrend) findViewById(R.id.showFatTrend);
		// 数据库初始化
		db = new UserDatabaseHelper(this, "VirtyDB", null, 1);
		
		// 初始按钮状态
		setSegButtonSelected(0);
		setPeriodButtonSelected(0);
		SysApplication.getInstance().addActivity(this);
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
//		// 根据选择的显示指标更改单位
		if (showFatTrend.getHealthItem() == 0) {
			// 体重
			// 获得默认单位并更新单位
			boolean unit = Activity4thSettingDefaultUnit.unit;
			if (unit) {
				// kg
				textViewFatTrendUnit.setText("(kg)");
			}else {
				// lb
				textViewFatTrendUnit.setText("(lb)");
			}			
		}
//		else if (showFatTrend.getHealthItem() == 4) {
//			//骨量
//			// kg
//			textViewFatTrendUnit.setText("(kg)");
//		}else if (showFatTrend.getHealthItem() == 5) {
//			//基础代谢
//			textViewFatTrendUnit.setText("(kcal)");
//		}else {
//			textViewFatTrendUnit.setText("(%)");
//		}
		// 初始化数据
		getRecord();
	}
	
	//获得用户数据
	public static void getRecord() {
		SQLiteDatabase dbReadDatabase = db.getReadableDatabase();
		// query(String table, String[] columns, String selection,String[]
		// selectionArgs,String groupBy, String having,String orderBy)
		// 先将数据清空
		showFatTrend.getDatas().clear();
		// 设置单位
		showFatTrend.setUnit(Activity4thSettingDefaultUnit.unit);
		if (Activity1stUserList.CHOOSE_USER_NAME == null) {
			// 未选择任何用户
			// showTrend.getDatas().clear();
		} else {
			// 在此处更改周期问题，以下为按日显示
			// 选择了用户，需要的数据为date和weight
			Cursor cursor = dbReadDatabase.query("userHealthRecord", null,
					"name=?",
					new String[] { Activity1stUserList.CHOOSE_USER_NAME },
					null, null, null);
			if (cursor.getCount() > 0) {
				cursor.moveToFirst();
				while (!cursor.isAfterLast()) {
					// 成员属性，日期、体重（kg记录）、脂肪、水分、肌肉、骨量、基础代谢
					TrendFatCellData history = new TrendFatCellData(
							cursor.getString(cursor.getColumnIndex("date")),
							cursor.getFloat(cursor.getColumnIndex("weight")),
							cursor.getFloat(cursor.getColumnIndex("fat")),
							cursor.getFloat(cursor.getColumnIndex("water")),
							cursor.getFloat(cursor.getColumnIndex("muscle")),
							cursor.getFloat(cursor.getColumnIndex("bone")),
							cursor.getInt(cursor.getColumnIndex("metabolism")));
					// System.out.println("............................date: "+history.date);
					showFatTrend.getDatas().add(history);
					// data.add(history);
					cursor.moveToNext();
				}
			}
		}
		// 更新画布区域
		showFatTrend.invalidate();

		// 关闭数据库
		dbReadDatabase.close();
	}
	
	// 以下的函数让button（指标seg）选中时的状态为selected，其他按钮为false
	public void setSegButtonSelected(int i) {
		// 根据传入的参数i，令i为true其余为false
		for (int j = 0; j < segments.length; j++) {
			if (i == j) {
				segments[j].setSelected(true);
				//更改趋势图显示的项目
				showFatTrend.setHealthItem(i);
				//更新趋势
				getRecord();
			} else {
				segments[j].setSelected(false);
			}
		}
		// 根据选择的显示指标更改单位
		if (showFatTrend.getHealthItem() == 0) {
			// 体重
			// 获得默认单位并更新单位
			boolean unit = Activity4thSettingDefaultUnit.unit;
			if (unit) {
				// kg
				textViewFatTrendUnit.setText("(kg)");
			}else {
				// lb
				textViewFatTrendUnit.setText("(lb)");
			}			
		}else if (showFatTrend.getHealthItem() == 4) {
			//骨量
			// kg
			textViewFatTrendUnit.setText("(kg)");
		}else if (showFatTrend.getHealthItem() == 5) {
			//基础代谢
			textViewFatTrendUnit.setText("(kcal)");
		}else {
			textViewFatTrendUnit.setText("(%)");
		}
	}

	// 以下的函数让button（周期）选中时的状态为selected，其他按钮为false
	public void setPeriodButtonSelected(int i) {
		// 根据传入的参数i，令i为true其余为false
		for (int j = 0; j < periods.length; j++) {
			if (i == j) {
				//更改趋势图显示的周期
				showFatTrend.setPeriod(i);
				periods[j].setSelected(true);
			} else {
				periods[j].setSelected(false);
			}
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
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.buttonFatTrendShare:
			// 取得外部存储所在目录
			File dir;
			Intent intent = new Intent(Intent.ACTION_SEND);
			getCurrentTime();
			String fileName = "FatTrendShare" + timeStamp + ".png";
			// 屏幕截图
			ScreenShot.shoot(this, fileName);
			// ScreenShot.takeScreenShot(LayoutInflater.View.);
			// 取得外部存储所在目录
			dir = Environment.getExternalStorageDirectory();
			String path = dir.getAbsolutePath() + "/virty/" + fileName;
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

		case R.id.buttonFatTrendSeg1:
			setSegButtonSelected(0);
			break;

		case R.id.buttonFatTrendSeg2:
			setSegButtonSelected(1);
			break;
		case R.id.buttonFatTrendSeg3:
			setSegButtonSelected(2);
			break;

		case R.id.buttonFatTrendSeg4:
			setSegButtonSelected(3);
			break;

		case R.id.buttonFatTrendSeg5:
			setSegButtonSelected(4);
			break;

		case R.id.buttonFatTrendSeg6:
			setSegButtonSelected(5);
			break;

		case R.id.buttonFatTrendWeek:
			setPeriodButtonSelected(0);
			break;

		case R.id.buttonFatTrendMonth:
			setPeriodButtonSelected(1);
			break;

		case R.id.buttonFatTrendSeason:
			setPeriodButtonSelected(2);
			break;

		case R.id.buttonFatTrendYear:
			setPeriodButtonSelected(3);
			break;
		default:
			break;
		}
	}
}

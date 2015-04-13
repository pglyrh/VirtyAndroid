package edu.xidian.mti1001.virty.activity2nd;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;
import edu.xidian.mti1001.virty.activity1st.Activity1stUserList;
import edu.xidian.mti1001.virty.activity4th.Activity4thSettingDefaultUnit;
import edu.xidian.mti1001.virty.welcome.R;
import edu.xidian.mti1001.virtyandroid.ScreenShot;
import edu.xidian.mti1001.virtyandroid.SysApplication;
import edu.xidian.mti1001.virtyandroid.UserDatabaseHelper;

public class Activity2ndWeightRecordBmi extends Activity implements
		OnClickListener {
	// 分享按钮
	private Button buttonShare;
	// 体重设置按钮
	public static Button buttonWeightSet;
	// 用户名
	public static TextView textView2ndBmiUserName, textView2ndBmiUserInfo;
//	// Bmi值
//	public static TextView textView2ndBmiBall;
//	// Bmi球
//	public static ImageView imageView2ndBmiBall;
	//bmi图形对象
	public static BmiView bmiView;
	// NumberPicker
	NumberPicker numberPickerWeight1, numberPickerWeight2;
	// bmi值
	private float bmiValue;
	private String bmi;
	// 默认单位
	private boolean unit;
	private String unitOfWeight;
	// 取得外部存储所在目录
	private File dir;

	// 存入数据库的记录
	// //用户
	private String name;
	// 时间
	private String dateString, timeString;
	private String timeStamp;
	// 设置的体重/称重，目标体重
	private float weight, targetWeight;

	// 数据库对象
	private UserDatabaseHelper db;
	private SQLiteDatabase dbReadDatabase;
	private SQLiteDatabase dbWriteDatabase;
	// 数据库指针
	private Cursor cursor, userCursor;

	// Resource
	Resources resources;
	boolean lflag = true;

	// 标志位，确定此次是否有称重
	
	
	//handle，主线程才能刷新视图，否则是不安全的
	private static final int COMPLETED = 0;  

	private static Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == COMPLETED) {
				bmiView.invalidate();
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
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_2nd_weight_record_bmi);

		// 获得控件
		bmiView = (BmiView) findViewById(R.id.showBmi);
		buttonShare = (Button) findViewById(R.id.button2ndBmiShare);
		buttonWeightSet = (Button) findViewById(R.id.button2ndBmiWeightRecordMeter);
		textView2ndBmiUserName = (TextView) findViewById(R.id.textView2ndBmiUserName);
		textView2ndBmiUserInfo = (TextView) findViewById(R.id.textView2ndBmiUserInfo);
//		textView2ndBmiBall = (TextView) findViewById(R.id.textView2ndBmiBall);
//		imageView2ndBmiBall = (ImageView) findViewById(R.id.imageView2ndBmiBall);
		buttonShare.setOnClickListener(this);
		buttonWeightSet.setOnClickListener(this);
		//
		// name = Activity1stUserList.CHOOSE_USER_NAME;
		// initInfo();
		// 新建一个数据库Helper
		db = new UserDatabaseHelper(this, "VirtyDB", null, 1);

		// 体重按钮显示清空为0
		// buttonWeightSet.setText(weight+"");
		SysApplication.getInstance().addActivity(this); 
	}

	@Override
	protected void onResume() {
		super.onResume();
		name = Activity1stUserList.CHOOSE_USER_NAME;
		// 获得单位
		unit = Activity4thSettingDefaultUnit.unit;
		if (unit) {
			unitOfWeight = "kg";
		} else {
			unitOfWeight = "lb";
		}
		
		resources = getResources();
		if (Locale.getDefault().getLanguage().toLowerCase().contains("zh")) {
			// 简体中文
			lflag = true;
		}else {
			lflag = false;
		}
		initInfo();
		// Toast.makeText(this, "onresume", Toast.LENGTH_SHORT).show();
		new Thread(bmiView).start();
		bmiView.setMeterNumber(0.0+"");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		// 分享功能，有待改进。。。分享的应该是当前屏幕截图
		case R.id.button2ndBmiShare:
			Intent intent = new Intent(Intent.ACTION_SEND);
			getCurrentTime();
			String fileName = "BmiShare"+timeStamp+".png";
			//屏幕截图
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

		// 设置体重按钮
		case R.id.button2ndBmiWeightRecordMeter:
			// 滚轮显示体重
			// 获得对话框布局
			LayoutInflater inflater2 = getLayoutInflater();
			View layout2 = inflater2.inflate(
					R.layout.activity_1st_user_list_number_picker_weight,
					(ViewGroup) findViewById(R.id.dialog_number_picker_weight));
			// 获得对话框中的数字选择器
			numberPickerWeight1 = (NumberPicker) layout2
					.findViewById(R.id.numberPickerWeight1);
			numberPickerWeight2 = (NumberPicker) layout2
					.findViewById(R.id.numberPickerWeight2);
			// 指定最大值和最小值
			numberPickerWeight1.setMinValue(20);
			numberPickerWeight1.setMaxValue(100);
			numberPickerWeight1.setValue(60);
			numberPickerWeight2.setMinValue(0);
			numberPickerWeight2.setMaxValue(9);
			if (Activity1stUserList.CHOOSE_USER_NAME != null && 
					!Activity1stUserList.CHOOSE_USER_NAME.isEmpty()) {
				// //弹出对话框，context必须用getParent获得，否则会
				// android.view.WindowManager$BadTokenException: Unable to add
				// window
				new AlertDialog.Builder(getParent())
						.setTitle(resources.getString(R.string.DialogSelectWeight))
						.setView(layout2)
						.setPositiveButton(resources.getString(R.string.DialogSure),
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										int w1 = numberPickerWeight1.getValue();
										int w2 = numberPickerWeight2.getValue();
										String w = "" + w1 + "." + w2;
										// System.out.println("weight string: "+w);
										weight = Float.parseFloat(w);
										float weightTrans = weight;
										// 获得体重单位
										if (unit) {
											// kg
										} else {
											// lb，转换体重
											weightTrans = (float) (weightTrans * 2.20462);
											BigDecimal b = new BigDecimal(
													weightTrans);
											weightTrans = b.setScale(1,
													BigDecimal.ROUND_HALF_UP)
													.floatValue();
										}
										// 获得/设置当前时间
										getCurrentTime();
										// 保存数据库
										saveEditUser();
										initInfo();
										// 显示体重
//										buttonWeightSet.setText(weightTrans
//												+ unitOfWeight);
										bmiView.setMeterNumber(weightTrans+"");
										//更新历史记录
										Activity2ndWeightRecordBmiHistory.nowWeight = weightTrans;
										Activity2ndWeightRecordBmiHistory.textView2ndBmiHistoryNowWeight.
											setText(weightTrans+unitOfWeight);
										//更新趋势
										Activity2ndWeightRecordBmiTrend.getRecord();
									}
								}).setNegativeButton(resources.getString(R.string.DialogCancel), null).show();
			}else {
				//提示用户需要选择一个user
				Toast.makeText(this, resources.getString(R.string.ToastSelectUser), Toast.LENGTH_SHORT).show();
			}

			// 保存数据库

			break;

		default:
			break;
		}
	}

	// 获得当前日期、时间
	public void getCurrentTime() {
		SimpleDateFormat formatterDate = new SimpleDateFormat("yyyy/MM/dd",
				Locale.CHINESE);
		SimpleDateFormat formatterTime = new SimpleDateFormat("HH:mm",
				Locale.CHINESE);
		SimpleDateFormat formatterTimeStamp = new SimpleDateFormat("yyyyMMddHHmm",
				Locale.CHINESE);
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		dateString = formatterDate.format(curDate);
		timeString = formatterTime.format(curDate);
		timeStamp = formatterTimeStamp.format(curDate);
		// Toast.makeText(this, "date: "+dateString+" time: "+timeString,
		// Toast.LENGTH_SHORT).show();
	}

	// 将更改的数据保存到数据库中
	// 建立表单，保存用户的各项健康指标（名、日期、事件、体重、脂肪率、水分率、肌肉率、骨量、基础代谢）
	/*
	 * db.execSQL("CREATE TABLE userHealthRecord(" +
	 * "_id INTEGER PRIMARY KEY AUTOINCREMENT," + "name TEXT DEFAULT NONE," +
	 * "date TEXT DEFAULT NONE," + "time TEXT DEFAULT NONE," +
	 * "weight FLOAT DEFAULT 0.0," + "fat FLOAT DEFAULT 0.0," +
	 * "water FLOAT DEFAULT 0.0," + "muscle FLOAT DEFAULT 0.0," +
	 * "bone FLOAT DEFAULT 0.0," + "metabolism INTEGER DEFAULT 0)");
	 */
	private void saveEditUser() {
		// 可写入的数据库
		dbWriteDatabase = db.getWritableDatabase();
		// 插入数据
		// 构建数据
		ContentValues contentValues = new ContentValues();
		contentValues.put("name", name);
		contentValues.put("date", dateString);
		contentValues.put("time", timeString);
		contentValues.put("weight", weight);
		// 插入表中
		dbWriteDatabase.insert("userHealthRecord", null, contentValues);

		// CustomListViewAdapter.data.add(0, new BmiHistoryListCellData(
		// cursor.getString(cursor.getColumnIndex("date")),
		// cursor.getString(cursor.getColumnIndex("time")),
		// cursor.getFloat(cursor.getColumnIndex("weight"))));
		Activity2ndWeightRecordBmiHistory.adapter.initRecord();
		Activity2ndWeightRecordBmiHistory.listView2ndBmiHistory.
		setAdapter(Activity2ndWeightRecordBmiHistory.adapter);
		// 关闭数据库
		dbWriteDatabase.close();
	}

	// 自定义函数，获得当前用户最后一次称重记录
	private void initInfo() {
		dbReadDatabase = db.getReadableDatabase();
		// query(String table, String[] columns, String selection,String[]
		// selectionArgs,String groupBy, String having,String orderBy)
		//记录cursor
		cursor = dbReadDatabase.query("userHealthRecord", null, "name=?",
				new String[] { name + "" }, null, null, null);
		
		dbReadDatabase = db.getReadableDatabase();
		//用户cursor
		userCursor = dbReadDatabase.query("user", null, "name=?",
				new String[] { name + "" }, null, null, null);

//		System.out.println("usercursor count....."+userCursor.getCount());
		
		if (cursor.getCount() > 0) {
			// 包含了数据
			// 获得最后一条
			cursor.moveToLast();
			if (userCursor.getCount()>0) {
				userCursor.moveToLast();				
			}
//			refreshInfo(cursor,userCursor);
		} else {
			cursor = null;
//			userCursor = null;
			if (userCursor.getCount()>0) {
				userCursor.moveToLast();				
			}
		}
		refreshInfo(cursor, userCursor);

		// 关闭数据库
		dbReadDatabase.close();
	}

	// 刷新页面
	public void refreshInfo(Cursor cursor, Cursor userCursor) {
		// 将用户更改为首页中选择的用户
		textView2ndBmiUserName.setText(name);
		// //体重按钮显示清空为0
		// buttonWeightSet.setText("0.0");
		// 如果有最近称重值，则显示。。。。若没有，则为。。。。
		String info;
		if (cursor == null) {
			// 体重按钮显示清空为0
//			buttonWeightSet.setText("0.0");
			bmiView.setMeterNumber(0.0+"");
			info = resources.getString(R.string.Activity2BmiInfo);
			//更改球
			bmiView.setBallNumber("21.0");
			// 设置球形
			bmiView.setBallImage(BitmapFactory.decodeResource(getResources(), R.drawable.weight_record_bmi_green));
			//转盘
			new Thread(bmiView).start();
//			//更改球
//			textView2ndBmiBall.setText("21.0");
//			// 设置球形
//			imageView2ndBmiBall
//			.setImageResource(R.drawable.weight_record_bmi_green);
			//更改history
			Activity2ndWeightRecordBmiHistory.nowWeight = 0;
			Activity2ndWeightRecordBmiHistory.textView2ndBmiHistoryNowWeight.
				setText(Activity2ndWeightRecordBmiHistory.nowWeight+unitOfWeight);
		} else {
			// 将数据库中的值赋给本地变量
//			dateString = cursor.getString(cursor.getColumnIndex("date"));
//			timeString = cursor.getString(cursor.getColumnIndex("time"));
			weight = cursor.getFloat(cursor.getColumnIndex("weight"));
//			targetWeight = Activity1stUserList.TARGET_WEIGHT;
			targetWeight = userCursor.getFloat(userCursor.getColumnIndex("targetWeight"));
			// 计算Bmi值
			calculateBmi();
			// 计算当前体重和目标体重之间的差值
			float delta = weight - targetWeight;
			// 转换单位
			float transWeight = weight;
			// 获得体重单位
			if (unit) {
				// kg
			} else {
				// lb，转换体重
				transWeight = (float) (transWeight * 2.20462);
				BigDecimal b = new BigDecimal(transWeight);
				transWeight = b.setScale(1, BigDecimal.ROUND_HALF_UP)
						.floatValue();
				delta = (float) (delta * 2.20462);
				b = new BigDecimal(delta);
				delta = b.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
			}
			// 体重按钮
//			buttonWeightSet.setText(transWeight + unitOfWeight);
			bmiView.setMeterNumber(transWeight+"");
			Activity2ndWeightRecordBmiHistory.nowWeight = transWeight;
			
			if (lflag) {
				info = String.format(
						"您的体重值为%.1f%s，比目标体重%s%.1f%s。本次称重BMI指数为%.1f，%s。"
								+ "请注意饮食并保持适量的运动。", transWeight, unitOfWeight,
						delta > 0 ? "重了" : "轻了", Math.abs(delta), unitOfWeight,
						bmiValue, bmi);
			}else {
				info = String.format(
						"Your weight value is %.1f%s. It is %.1f%s %s than your target weight. BMI parameter is %.1f. %s.", 
						transWeight, unitOfWeight, Math.abs(delta), unitOfWeight, delta > 0 ? "heavier" : "lighter",
						bmiValue, bmi);
			}
			
		}
		// 设置
		textView2ndBmiUserInfo.setText(info);
	}

	// 计算bmi
	public void calculateBmi() {
		float f = (float) (weight / Math.pow(
				((float) Activity1stUserList.HEIGHT / 100), 2));
		// Toast.makeText(this, "bmi "+f, Toast.LENGTH_SHORT).show();
		BigDecimal b = new BigDecimal(f);
		bmiValue = b.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
		// 查看范围
		if (bmiValue < 18.5) {
			bmi = resources.getString(R.string.Activity2BmiValueUnderweight);
			// 设置球形
			bmiView.setBallImage(BitmapFactory.decodeResource(getResources(), R.drawable.weight_record_bmi_blue));
//			// 设置球形
//			imageView2ndBmiBall
//					.setImageResource(R.drawable.weight_record_bmi_blue);
		} else if (18.5 <= bmiValue && bmiValue < 25) {
			bmi = resources.getString(R.string.Activity2BmiValueModerate);
			// 设置球形
			bmiView.setBallImage(BitmapFactory.decodeResource(getResources(), R.drawable.weight_record_bmi_green));
//			// 设置球形
//			imageView2ndBmiBall
//					.setImageResource(R.drawable.weight_record_bmi_green);
		} else if (25 <= bmiValue && bmiValue < 28) {
			bmi = resources.getString(R.string.Activity2BmiValueOverweight);
			// 设置球形
			bmiView.setBallImage(BitmapFactory.decodeResource(getResources(), R.drawable.weight_record_bmi_orange));
			// 设置球形
//			imageView2ndBmiBall
//					.setImageResource(R.drawable.weight_record_bmi_orange);
		} else if (28 <= bmiValue && bmiValue <= 32) {
			bmi = resources.getString(R.string.Activity2BmiValueObesity);
			// 设置球形
			bmiView.setBallImage(BitmapFactory.decodeResource(getResources(), R.drawable.weight_record_bmi_pink));
			// 设置球形
//			imageView2ndBmiBall
//					.setImageResource(R.drawable.weight_record_bmi_pink);
		} else if (bmiValue >= 32) {
			bmi = resources.getString(R.string.Activity2BmiValueObese);
			// 设置球形
			bmiView.setBallImage(BitmapFactory.decodeResource(getResources(), R.drawable.weight_record_bmi_red));
//			// 设置球形
//			imageView2ndBmiBall
//					.setImageResource(R.drawable.weight_record_bmi_red);
		}
		//更改球
		bmiView.setBallNumber(bmiValue + "");
		//转盘
		new Thread(bmiView).start();
//		// 更改值
//		textView2ndBmiBall.setText(bmiValue + "");
	}
}

package edu.xidian.mti1001.virty.activity2nd;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.SeekBar;
import android.widget.TextView;
import edu.xidian.mti1001.virty.activity1st.Activity1stUserList;
import edu.xidian.mti1001.virty.activity4th.Activity4thSettingDefaultUnit;
import edu.xidian.mti1001.virty.bluetooth.BluetoothLeClass;
import edu.xidian.mti1001.virty.welcome.R;
import edu.xidian.mti1001.virtyandroid.ScreenShot;
import edu.xidian.mti1001.virtyandroid.SysApplication;
import edu.xidian.mti1001.virtyandroid.UserDatabaseHelper;

public class Activity2ndWeightRecordFat extends Activity implements
		OnClickListener {
	//测试按钮
	private Button buttonTestFat;
	//分享按钮
	private Button buttonShare;
	// 弹出详细信息对话框
	private ImageView imageViewPopDetail;
	//ball中的数据
	public static TextView textViewItem, textViewNumber, textViewUnit;
//	private TextView textViewItem, textViewNumber, textViewUnit;
	//外围
	private static RingView ringView;
	
	// 获得最后一次称重的各项参数
	// 存入数据库的记录
	// //用户
	private static String name, sex;
	private static int age, height;
	// 时间
	private static String dateString, timeString;
	private static String timeStamp;
	// 人体阻抗
	private static float resistor;
	private float pickResistor;
	// 设置的体重/称重
	private static float weight = 0;
	private float pickWeight = 0;
	// 脂肪
	private static float fat = 0;
	// 水分
	private static float water = 0;
	// 肌肉
	private static float muscle = 0;
	// 骨量
	private static float bone = 0;
	// 基础代谢
	private static int metabolism = 0;
	// 测量结果与范围的比较
	private static String compareFat = "---";
	private static String compareWater = "---";
	private static String compareMuscle = "---";
	private static String compareBone = "---";
	private String normalFat, normalWater, normalMuscle, normalBone;

	// 数据库对象
	private static UserDatabaseHelper db;
	private static SQLiteDatabase dbReadDatabase1, dbReadDatabase2;
	private static SQLiteDatabase dbWriteDatabase;
	boolean confirmSave = false;
	boolean changeMuch = false;
	Context paContext;
	// 数据库指针
	private static Cursor cursorRecord;
	private static Cursor cursorUser;

	// 默认单位
	private String unitOfWeight;
	Resources resources;
	
	// 当前页面是否课件
	public static boolean front = false;
	
	// 接收蓝牙传递的数据
	//handle，主线程才能刷新视图，否则是不安全的
	public static Handler BleHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				// 说明体重有变化
				// 计算 保存
				weight = (float)BluetoothLeClass.bleWeight;
				resistor = (float)BluetoothLeClass.bleResistor;
				compute();
//				System.out.println("handleMessage.............");
				saveToUserHealth();
			}
		}
	};  
	
	public static Handler getBleHandler(){
		return BleHandler;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_2nd_weight_record_fat);
		
		buttonTestFat = (Button) findViewById(R.id.buttonFatTest);
		buttonTestFat.setOnClickListener(this);
		
		buttonShare = (Button) findViewById(R.id.button2ndFatShare);
		buttonShare.setOnClickListener(this);
		imageViewPopDetail = (ImageView) findViewById(R.id.imageView2ndFatDetail);
		imageViewPopDetail.setOnClickListener(this);
		
		ringView = (RingView) findViewById(R.id.showFatCircle);
		
		//ball中TextView
		textViewItem = (TextView) findViewById(R.id.textViewFatBallItem);
		textViewNumber = (TextView) findViewById(R.id.textViewFatBallNumber);
		textViewUnit = (TextView) findViewById(R.id.textViewFatBallUnit);

		// 新建一个数据库Helper
		db = new UserDatabaseHelper(this, "VirtyDB", null, 1);

		SysApplication.getInstance().addActivity(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 可见
		front = true;
		System.out.println("on resume front.............."+front);
		resources = getResources();
		name = Activity1stUserList.CHOOSE_USER_NAME;
		// 获得单位
		if (Activity4thSettingDefaultUnit.unit) {
			unitOfWeight = "kg";
		} else {
			unitOfWeight = "lb";
		}
		initInfo();
		// 显示数据，刷新页面后，显示数据为0
		String datas[] = new String[6];
		for (int i = 0; i < datas.length; i++) {
			datas[i] = new String();
		}
		datas[0] = String.format("%.1f",0.0);
		datas[1] = String.format("%.1f",0.0);
		datas[2] = String.format("%.1f",0.0);
		datas[3] = String.format("%.1f",0.0);
		datas[4] = String.format("%.1f",0.0);
		datas[5] = String.valueOf(0);
		ringView.setDatas(datas);
		// Toast.makeText(this, "onresume", Toast.LENGTH_SHORT).show();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
//		front = false;
		System.out.println("on pause front.............."+front);
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		System.out.println("on stop front.............."+front);
	}

	// 自定义函数，获得当前用户最后一次称重记录
	private static void initInfo() {
		dbReadDatabase1 = db.getReadableDatabase();
		// query(String table, String[] columns, String selection,String[]
		// selectionArgs,String groupBy, String having,String orderBy)
		//查询记录数据库
		cursorRecord = dbReadDatabase1.query("userHealthRecord", null,
				"name=?", new String[] { name + "" }, null, null, null);
		
		dbReadDatabase2 = db.getReadableDatabase();
		
		//查询用户数据库
		cursorUser = dbReadDatabase2.query("user", null, "name=?",
				new String[] { name + "" }, null, null, null);
		
		// 获得记录
		if (cursorRecord.getCount() > 0) {
			// 包含了数据
			// 获得最后一条
			cursorRecord.moveToLast();
			cursorUser.moveToLast();
		} else {
			cursorRecord = null;
//			cursorUser = null;
			if (cursorUser.getCount() > 0) {
				cursorUser.moveToLast();				
			}else {
				cursorUser = null;
			}
		}
		refreshInfo(cursorRecord, cursorUser);
		
		// 关闭数据库
		dbReadDatabase1.close();
		dbReadDatabase2.close();
	}

	// 刷新页面
	public static void refreshInfo(Cursor cursorRecord, Cursor cursorUser) {
		if (cursorUser != null) {			
			// 将用户更改为首页中选择的用户
			name = Activity1stUserList.CHOOSE_USER_NAME;
			// 获得用户信息
			sex = cursorUser.getString(cursorUser.getColumnIndex("sex"));
//			age = cursorUser.getInt(cursorUser.getColumnIndex("age"));
			age = Calendar.getInstance().get(Calendar.YEAR)-cursorUser.getInt(cursorUser.getColumnIndex("age"));
//			System.out.println("refresh....................age"+age);
			height = cursorUser.getInt(cursorUser.getColumnIndex("height")); 
		}

		// 获得记录
		// //体重按钮显示清空为0
		// buttonWeightSet.setText("0.0");
		// 如果有最近称重值，则显示。。。。若没有，则为。。。。
		if (cursorRecord == null) {
			// 体重按钮显示清空为0
			weight = 0;
			fat = 0;
			water = 0;
			muscle = 0;
			bone = 0;
			metabolism = 0;
			compareFat = "---";
			compareWater = "---";
			compareMuscle = "---";
			compareBone = "---";
		} else {
			// 将数据库中的值赋给本地变量
			// dateString = cursor.getString(cursor.getColumnIndex("date"));
			// timeString = cursor.getString(cursor.getColumnIndex("time"));
			weight = cursorRecord.getFloat(cursorRecord
					.getColumnIndex("weight"));
			fat = cursorRecord.getFloat(cursorRecord.getColumnIndex("fat"));
			water = cursorRecord.getFloat(cursorRecord.getColumnIndex("water"));
			muscle = cursorRecord.getFloat(cursorRecord
					.getColumnIndex("muscle"));
			bone = cursorRecord.getFloat(cursorRecord.getColumnIndex("bone"));
			metabolism = cursorRecord.getInt(cursorRecord
					.getColumnIndex("metabolism"));
		}
		
		//更改ball中的值.....根据direct，主要是更改数值
		String datas[] = new String[6];
		for (int i = 0; i < datas.length; i++) {
			datas[i] = new String();
		}
		// 体重
		if (Activity4thSettingDefaultUnit.unit) {
			// kg
			datas[0] = String.format("%.1f", weight);
		} else {
			// lb
			float transWeight = (float) (weight * 2.20462);
			BigDecimal b = new BigDecimal(transWeight);
			transWeight = b.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
			datas[0] = String.format("%.1f", transWeight);
		}
//		datas[0] = String.format("%.1f", weight);
		datas[1] = String.format("%.1f",fat);
		datas[2] = String.format("%.1f",water);
		datas[3] = String.format("%.1f",muscle);
		datas[4] = String.format("%.1f",bone);
		datas[5] = String.valueOf(metabolism);
//		datas[0] = String.valueOf(weight);
//		datas[1] = String.valueOf(fat);
//		datas[2] = String.valueOf(water);
//		datas[3] = String.valueOf(muscle);
//		datas[4] = String.valueOf(bone);
//		datas[5] = String.valueOf(metabolism);
		// 显示数据
		ringView.setDatas(datas);
	}

	// 设置正常范围（）及比较结果
	public void getNormalAndCompare() {
		// 判断性别
		if (sex.equals("男")) {
			// 男
			// 判断年龄
			if (age > 30) {
				// 正常值范围
				normalFat = "19.6 ~ 24.0%";
				normalWater = "53.5 ~ 56.5%";
				normalMuscle = "44.5 ~ 47.6%";
				normalBone = "  1.8 ~ 4.0kg";
				// 比较
				// 脂肪
				if (fat <= 0) {
					compareFat = "---";
				} else if (fat < 19.6) {
//					compareFat = "偏少";
					compareFat = resources.getString(R.string.Activity2FatCompareLess);
				} else if (fat > 24) {
//					compareFat = resources.getString(R.string.Activity2FatCompareMore);
					compareFat = resources.getString(R.string.Activity2FatCompareMore);
				} else {
//					compareFat = resources.getString(R.string.Activity2FatCompareModerate);
					compareFat = resources.getString(R.string.Activity2FatCompareModerate);
				}
				// 水分
				if (water <= 0) {
					compareWater = "---";
				}else if (water < 53.5) {
					compareWater = resources.getString(R.string.Activity2FatCompareLess);;
				} else if (water > 56.5) {
					compareWater = resources.getString(R.string.Activity2FatCompareMore);
				} else {
					compareWater = resources.getString(R.string.Activity2FatCompareModerate);
				}
				// 肌肉
				if (muscle <= 0) {
					compareMuscle = "---";
				} else if (muscle < 44.5) {
					compareMuscle = resources.getString(R.string.Activity2FatCompareLess);;
				} else if (muscle > 47.6) {
					compareMuscle = resources.getString(R.string.Activity2FatCompareMore);
				} else {
					compareMuscle = resources.getString(R.string.Activity2FatCompareModerate);
				}
				// 骨量
				if (bone <= 0) {
					compareBone = "---";
				} else if (bone < 1.8) {
					compareBone = resources.getString(R.string.Activity2FatCompareLess);;
				} else if (bone > 4) {
					compareBone = resources.getString(R.string.Activity2FatCompareMore);
				} else {
					compareBone = resources.getString(R.string.Activity2FatCompareModerate);
				}
			} else {
				// 小于30
				normalFat = "15.6 ~ 20.0%";
				normalWater = "56.0 ~ 59.5%";
				normalMuscle = "42.5 ~ 46.0%";
				normalBone = "  1.5 ~ 3.5kg";
				// 脂肪
				if (fat <= 0) {
					compareFat = "---";
				} else if (fat < 15.6) {
					compareFat = resources.getString(R.string.Activity2FatCompareLess);;
				} else if (fat > 20) {
					compareFat = resources.getString(R.string.Activity2FatCompareMore);
				} else {
					compareFat = resources.getString(R.string.Activity2FatCompareModerate);
				}
				// 水分
				if (water <= 0) {
					compareWater = "---";
				}else if (water < 56) {
					compareWater = resources.getString(R.string.Activity2FatCompareLess);;
				} else if (water > 59.5) {
					compareWater = resources.getString(R.string.Activity2FatCompareMore);
				} else {
					compareWater = resources.getString(R.string.Activity2FatCompareModerate);
				}
				// 肌肉
				if (muscle <= 0) {
					compareMuscle = "---";
				} else if (muscle < 42.5) {
					compareMuscle = resources.getString(R.string.Activity2FatCompareLess);;
				} else if (muscle > 46) {
					compareMuscle = resources.getString(R.string.Activity2FatCompareMore);
				} else {
					compareMuscle = resources.getString(R.string.Activity2FatCompareModerate);
				}
				// 骨量
				if (bone <= 0) {
					compareBone = "---";
				} else if (bone < 1.5) {
					compareBone = resources.getString(R.string.Activity2FatCompareLess);;
				} else if (bone > 3.5) {
					compareBone = resources.getString(R.string.Activity2FatCompareMore);
				} else {
					compareBone = resources.getString(R.string.Activity2FatCompareModerate);
				}
			}
		} else {
			// 女
			// 判断年龄
			if (age > 30) {
				// 正常值范围
				normalFat = "25.1 ~ 30.0%";
				normalWater = "48.5 ~ 52.5%";
				normalMuscle = "40.5 ~ 43.5%";
/*				//bone需要单位转换
				if (Activity4thSettingDefaultUnit.unit) {
					normalBone = "  1.8 ~ 4.0kg";
				}else {
					normalBone = "  1.8 ~ 4.0kg"; 
				}*/
				normalBone = "  1.8 ~ 4.0kg";
				// 比较
				// 脂肪
				if (fat <= 0) {
					compareFat = "---";
				} else if (fat < 25.1) {
					compareFat = resources.getString(R.string.Activity2FatCompareLess);;
				} else if (fat > 30) {
					compareFat = resources.getString(R.string.Activity2FatCompareMore);
				} else {
					compareFat = resources.getString(R.string.Activity2FatCompareModerate);
				}
				// 水分
				if (water <= 0) {
					compareWater = "---";
				} else if (water < 48.5) {
					compareWater = resources.getString(R.string.Activity2FatCompareLess);;
				} else if (water > 52.5) {
					compareWater = resources.getString(R.string.Activity2FatCompareMore);
				} else {
					compareWater = resources.getString(R.string.Activity2FatCompareModerate);
				}
				// 肌肉
				if (muscle <= 0) {
					compareMuscle = "---";
				} else if (muscle < 40.5) {
					compareMuscle = resources.getString(R.string.Activity2FatCompareLess);;
				} else if (muscle > 43.5) {
					compareMuscle = resources.getString(R.string.Activity2FatCompareMore);
				} else {
					compareMuscle = resources.getString(R.string.Activity2FatCompareModerate);
				}
				// 骨量
				if (bone <= 0) {
					compareBone = "---";
				} else if (bone < 1.8) {
					compareBone = resources.getString(R.string.Activity2FatCompareLess);;
				} else if (bone > 4) {
					compareBone = resources.getString(R.string.Activity2FatCompareMore);
				} else {
					compareBone = resources.getString(R.string.Activity2FatCompareModerate);
				}
			} else {
				// 小于30
				normalFat = "20.6 ~ 25.0%";
				normalWater = "52.5 ~ 55.5%";
				normalMuscle = "39.3 ~ 42.8%";
				normalBone = "  1.5 ~ 3.5kg";
				// 脂肪
				if (fat <= 0) {
					compareFat = "---";
				} else if (fat < 20.6) {
					compareFat = resources.getString(R.string.Activity2FatCompareLess);;
				} else if (fat > 25) {
					compareFat = resources.getString(R.string.Activity2FatCompareMore);
				} else {
					compareFat = resources.getString(R.string.Activity2FatCompareModerate);
				}
				// 水分
				if (water <= 0) {
					compareWater = "---";
				} else if (water < 52.5) {
					compareWater = resources.getString(R.string.Activity2FatCompareLess);;
				} else if (water > 55.5) {
					compareWater = resources.getString(R.string.Activity2FatCompareMore);
				} else {
					compareWater = resources.getString(R.string.Activity2FatCompareModerate);
				}
				// 肌肉，肌肉量计算值已经为百分比
				if (muscle <= 0) {
					compareMuscle = "---";
				} else if (muscle < 39.3) {
					compareMuscle = resources.getString(R.string.Activity2FatCompareLess);;
				} else if (muscle > 42.8) {
					compareMuscle = resources.getString(R.string.Activity2FatCompareMore);
				} else {
					compareMuscle = resources.getString(R.string.Activity2FatCompareModerate);
				}
				// 骨量
				if (bone <= 0) {
					compareBone = "---";
				} else if (bone < 1.5) {
					compareBone = resources.getString(R.string.Activity2FatCompareLess);;
				} else if (bone > 3.5) {
					compareBone = resources.getString(R.string.Activity2FatCompareMore);
				} else {
					compareBone = resources.getString(R.string.Activity2FatCompareModerate);
				}
			}
		}
	}

	//计算各项指标
	public static void compute(){
		final double A = 2.877;
		final double B = 0.0009;
		final double C = 0.392;
		final double D = 0.00095;
		final double E = 4.5;
		final double F = 0.069;
		//性别，男性为0，女性为1
		int sexInt = (sex.equals("男"))?0:1;
		// 瘦体重
		double lbm = A+B*height*height+C*weight-D*resistor-E*sexInt-F*age;
		//脂肪含量(以百分数保存)
		fat = (float) ((weight - lbm)/weight)*100;
		//水分(以百分数保存)
		water = (float) ((lbm*0.73)/weight)*100;
		//肌肉
		if (sexInt == 0) {
			muscle = (float) (24.4+(334+7.78*height-9.8*age)/weight);
		}else {
			muscle = (float) (24.4+(7.74*height-318-9.8*age)/weight);
		}
		//骨量
		//年龄判断，此处有问题，需更改
		if (age > 35) {
			bone = (float) ((water/100)*0.18-2*sexInt+9-4*age/35);
		}else {
			bone = (float) ((water/100)*0.18-2*sexInt+5*age/35);			
		}
		//基础代谢
		if (sexInt == 0) {
			metabolism = (int) (13.7*weight+5*height-6.8*age+66);
		}else {
			metabolism = (int) (9.6*weight+1.8*height-4.7*age+655);
		}
	}
	
	// 获得当前日期、时间
	public static void getCurrentTime() {
		SimpleDateFormat formatterDate = new SimpleDateFormat("yyyy/MM/dd",
				Locale.CHINESE);
		SimpleDateFormat formatterTime = new SimpleDateFormat("HH:mm",
				Locale.CHINESE);
		SimpleDateFormat formatterTimeStamp = new SimpleDateFormat(
				"yyyyMMddHHmm", Locale.CHINESE);
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		dateString = formatterDate.format(curDate);
		timeString = formatterTime.format(curDate);
		timeStamp = formatterTimeStamp.format(curDate);
		// Toast.makeText(this, "date: "+dateString+" time: "+timeString,
		// Toast.LENGTH_SHORT).show();
	}
	
	private static void saveToUserHealth() {
		getCurrentTime();
		// 可写入的数据库
		dbWriteDatabase = db.getWritableDatabase();
		// 插入数据
		// 构建数据
		ContentValues contentValues = new ContentValues();
		contentValues.put("name", name);
		contentValues.put("date", dateString);
		contentValues.put("time", timeString);
		contentValues.put("weight", weight);
		//存入fat、water、muscle、bone、metabolism
		contentValues.put("fat", fat);
		contentValues.put("water", water);
		contentValues.put("muscle", muscle);
		contentValues.put("bone", bone);
		contentValues.put("metabolism", metabolism);
		// 插入表中
		dbWriteDatabase.insert("userHealthRecord", null, contentValues);

		// CustomListViewAdapter.data.add(0, new BmiHistoryListCellData(
		// cursor.getString(cursor.getColumnIndex("date")),
		// cursor.getString(cursor.getColumnIndex("time")),
		// cursor.getFloat(cursor.getColumnIndex("weight"))));
//		Activity2ndWeightRecordBmiHistory.adapter.initRecord();
//		Activity2ndWeightRecordBmiHistory.listView2ndBmiHistory.
//		setAdapter(Activity2ndWeightRecordBmiHistory.adapter);

		//更改ball
		initInfo();
		
		//更新历史记录
		Activity2ndWeightRecordFatHistory.adapter.initRecord();
		Activity2ndWeightRecordFatHistory.listView2ndFatHistory.
		setAdapter(Activity2ndWeightRecordFatHistory.adapter);
		//更新趋势
		Activity2ndWeightRecordFatTrend.getRecord();
		// 关闭数据库
		dbWriteDatabase.close();
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageView2ndFatDetail:
			// System.out.println("...............fat detail");
			// 弹出detail框
			// 获得对话框布局
			LayoutInflater inflater = getLayoutInflater();
			View layout = inflater.inflate(
					R.layout.activity_2nd_weight_record_fat_detail_dialog,
					(ViewGroup) findViewById(R.id.dialog_fat_detail));
			// 弹出对话框
			final AlertDialog alertDialog = new AlertDialog.Builder(getParent())
					.setView(layout).show();
			// 获得dialog中的控件
			ImageView imageViewDialogSure = (ImageView) layout
					.findViewById(R.id.imageViewDialogFatDetailSure);
			TextView textViewDialogUser = (TextView) layout
					.findViewById(R.id.textViewDialogFatDetailUser);
			TextView textViewDialogWeight = (TextView) layout
					.findViewById(R.id.textViewDialogFatDetailWeight);
			TextView textViewDialogFat = (TextView) layout
					.findViewById(R.id.textViewDialogFatDetailFat);
			TextView textViewDialogFatNormal = (TextView) layout
					.findViewById(R.id.textViewDialogFatDetailFatNormal);
			TextView textViewDialogFatC = (TextView) layout
					.findViewById(R.id.textViewDialogFatDetailFatCompare);
			TextView textViewDialogWater = (TextView) layout
					.findViewById(R.id.textViewDialogFatDetailWater);
			TextView textViewDialogWaterNormal = (TextView) layout
					.findViewById(R.id.textViewDialogFatDetailWaterNormal);
			TextView textViewDialogWaterC = (TextView) layout
					.findViewById(R.id.textViewDialogFatDetailWaterCompare);
			TextView textViewDialogMuscle = (TextView) layout
					.findViewById(R.id.textViewDialogFatDetailMuscle);
			TextView textViewDialogMuscleNormal = (TextView) layout
					.findViewById(R.id.textViewDialogFatDetailMuscleNormal);
			TextView textViewDialogMuscleC = (TextView) layout
					.findViewById(R.id.textViewDialogFatDetailMuscleCompare);
			TextView textViewDialogBone = (TextView) layout
					.findViewById(R.id.textViewDialogFatDetailBone);
			TextView textViewDialogBoneNormal = (TextView) layout
					.findViewById(R.id.textViewDialogFatDetailBoneNormal);
			TextView textViewDialogBoneC = (TextView) layout
					.findViewById(R.id.textViewDialogFatDetailBoneCompare);
			TextView textViewDialogMetabolism = (TextView) layout
					.findViewById(R.id.textViewDialogFatDetailMetabolism);

			// dialog中内容展示
			// name
			textViewDialogUser.setText(name);
			// 体重
			if (Activity4thSettingDefaultUnit.unit) {
				// kg
				textViewDialogWeight.setText(weight + "kg");
			} else {
				// lb
				float transWeight = (float) (weight * 2.20462);
				BigDecimal b = new BigDecimal(transWeight);
				transWeight = b.setScale(1, BigDecimal.ROUND_HALF_UP)
						.floatValue();
				textViewDialogWeight.setText(transWeight + "lb");
			}
//			if (cursorRecord != null) {
//				// 设置正常范围的值，及比较结果
//				getNormalAndCompare();				
//			}
			// 计算初始值和比较结果
			getNormalAndCompare();
			// 脂肪
			textViewDialogFat.setText(String.format("%.1f%s", fat, "%"));
			textViewDialogFatNormal.setText(normalFat + "");
			textViewDialogFatC.setText(compareFat + "");
			// 水分
			textViewDialogWater.setText(String.format("%.1f%s", water,
					"%"));
			textViewDialogWaterNormal.setText(normalWater + "");
			textViewDialogWaterC.setText(compareWater + "");
			// 肌肉，算得的结果已经是百分比了
			textViewDialogMuscle.setText(String.format("%.1f%s", muscle,
					"%"));
			textViewDialogMuscleNormal.setText(normalMuscle + "");
			textViewDialogMuscleC.setText(compareMuscle + "");
			// 骨量
//			if (Activity4thSettingDefaultUnit.unit) {
//				// kg
//				textViewDialogBone.setText(bone + "kg");
//			} else {
//				// lb
//				float transBone = (float) (bone * 2.20462);
//				BigDecimal b = new BigDecimal(transBone);
//				transBone = b.setScale(1, BigDecimal.ROUND_HALF_UP)
//						.floatValue();
//				textViewDialogBone.setText(transBone + "lb");
//			}			
			textViewDialogBone.setText(String.format("%.1f%s", bone,"kg"));
			textViewDialogBoneNormal.setText(normalBone + "");
			textViewDialogBoneC.setText(compareBone + "");
			// 基础代谢
			textViewDialogMetabolism.setText(metabolism + "kcal");

			// 添加 “确定” 事件，对话框消失
			imageViewDialogSure.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					alertDialog.dismiss();
				}
			});
			break;
		
		case R.id.buttonFatTest:
			// 弹出对话框，输入体重和人体阻抗
			LayoutInflater inflater2 = getLayoutInflater();
			final View layout2 = inflater2.inflate(
					R.layout.activity_2nd_weight_record_fat_manual_dialog,
					(ViewGroup) findViewById(R.id.dialog_fat_manual));
			// 弹出对话框
			final AlertDialog alertDialogInput = new AlertDialog.Builder(
					getParent()).setView(layout2).show();
			paContext = getParent();
			// 获得对话框中的控件
			final TextView textViewPickWeight = (TextView)
					layout2.findViewById(R.id.textViewFatManualPickWeight);
			// 体重滚轮
			NumberPicker numberPickerWeight1 = (NumberPicker)
					layout2.findViewById(R.id.numberPickerFatManualWeight);
			//获得对话框中的数字选择器
			//指定最大值和最小值
	        numberPickerWeight1.setMinValue(20);
	        numberPickerWeight1.setMaxValue(100);
	        numberPickerWeight1.setValue(55);
	        textViewPickWeight.setText(""+(float)numberPickerWeight1.getValue());
	        pickWeight = numberPickerWeight1.getValue();
	        //体重选择
	        numberPickerWeight1.setOnValueChangedListener(new OnValueChangeListener(){

	            @Override
	            public void onValueChange(NumberPicker picker, int oldVal,
	                    int newVal) {
	                pickWeight=newVal;
	                textViewPickWeight.setText(""+pickWeight);
	            }	            
	        });
	        
	        // 体阻选择
	        SeekBar seekBar = (SeekBar) layout2.findViewById(R.id.seekBarFatManualResistor);
			seekBar.setMax(50);
			//初始值
			seekBar.setProgress(10);
			// 体阻显示
			final TextView textViewSeekBarResistor = (TextView) 
					layout2.findViewById(R.id.textViewSeekBarFatManualResistor);
			textViewSeekBarResistor.setText(String.format("%.1f", 1.0));
			pickResistor = (float) 1.0;
			seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
					
				}
				
				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {
					
				}
				
				@Override
				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromUser) {
					//progress范围是100，总共代表5k，每一个单位代表0.05k
					pickResistor = (float)(progress*((float)5/50));
					textViewSeekBarResistor.setText(String.format("%.1f", pickResistor));
				}
			});
	        		
			// 获得dialog中的控件
			ImageView imageViewManualDialogSure = (ImageView) layout2
					.findViewById(R.id.imageViewDialogFatManualSure);
			ImageView imageViewManualDialogCancel = (ImageView) layout2
					.findViewById(R.id.imageViewDialogFatManualCancel);

			LayoutInflater inflaterChange = getLayoutInflater();
			final View layoutChange = inflaterChange.inflate(
					R.layout.activity_2nd_weight_record_large_change_dialog,
					(ViewGroup) findViewById(R.id.layout_dialog_weight_change));
			// 弹出对话框
			final AlertDialog.Builder alertChangeDialogBuilder = new AlertDialog.Builder(
					Activity2ndWeightRecordFat.this.getParent()).setView(layoutChange);
			// 确定按钮，保存信息
			imageViewManualDialogSure.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// 体重相差超过10kg
					System.out.println("weight: " + weight);
					System.out.println("pickWeight: " + pickWeight);
					System.out.println("Math.abs(pickWeight - weight): "
							+ Math.abs(pickWeight - weight));
					if (Math.abs(pickWeight - weight) > 10) {
						changeMuch = true;					
					} else {
						confirmSave = true;
						changeMuch = false;
						if (confirmSave) {
							//获取选择的体重和体阻
							weight = pickWeight;					
							resistor = pickResistor;
							// 选择的体重和阻抗需大于0
							if (weight > 0 && resistor > 0) {
								compute();						
							}else {
								fat = 0;
								water = 0;
								muscle = 0;
								bone = 0;
								metabolism = 0;
							}
							//打印一下信息核对
//						System.out.println(String.format("................weight: %.1f", weight));
//						System.out.println(String.format("................resistor: %.1f", resistor));
//						System.out.println(String.format("................weight: %.2f%s", weight,"kg"));
//						System.out.println(String.format("................fat: %.1f%s", fat*100,"%"));
//						System.out.println(String.format("................water: %.1f%s", water*100,"%"));
//						//muscle的值记录的已经是百分比了
//						System.out.println(String.format("................muscle: %.1f%s", muscle,"%"));
//						System.out.println(String.format("................weight: %.1f%s", bone,"kg"));
//						System.out.println(String.format("................metabolism: %d%s", metabolism,"kcal"));
							saveToUserHealth();			
						}
					}				
					alertDialogInput.dismiss();
					
					// 体重变化大
					if (changeMuch) {
						System.out.println("...........change  "+changeMuch);
//						LayoutInflater inflaterChange = getLayoutInflater();
//						final View layoutChange = inflaterChange.inflate(
//								R.layout.activity_2nd_weight_record_large_change_dialog,
//								(ViewGroup) findViewById(R.id.layout_dialog_weight_change));
//						// 弹出对话框
//						final AlertDialog alertChangeDialog = new AlertDialog.Builder(
//								Activity2ndWeightRecordFat.this.getParent()).setView(layoutChange).show();
						final AlertDialog alertChangeDialog = alertChangeDialogBuilder.show();
						// 获得dialog中的控件
						ImageView imageViewChangeDialogSure = (ImageView) layoutChange
								.findViewById(R.id.imageViewDialogChangeMuchSure);
						ImageView imageViewChangeDialogCancel = (ImageView) layoutChange
								.findViewById(R.id.imageViewDialogChangeMuchCancel);

						//取消储存
						imageViewChangeDialogCancel.setOnClickListener(new OnClickListener() {							
							@Override
							public void onClick(View arg0) {
								confirmSave = false;
								System.out.println(".......不储存");
								alertChangeDialog.dismiss();
							}
						});
						
						//确认储存
						imageViewChangeDialogSure.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View arg0) {
								confirmSave = true;
								System.out.println(".......储存");
								alertChangeDialog.dismiss();
								if (confirmSave) {
									//获取选择的体重和体阻
									weight = pickWeight;					
									resistor = pickResistor;
									// 选择的体重和阻抗需大于0
									if (weight > 0 && resistor > 0) {
										compute();						
									}else {
										fat = 0;
										water = 0;
										muscle = 0;
										bone = 0;
										metabolism = 0;
									}
									//打印一下信息核对
//								System.out.println(String.format("................weight: %.1f", weight));
//								System.out.println(String.format("................resistor: %.1f", resistor));
//								System.out.println(String.format("................weight: %.2f%s", weight,"kg"));
//								System.out.println(String.format("................fat: %.1f%s", fat*100,"%"));
//								System.out.println(String.format("................water: %.1f%s", water*100,"%"));
//								//muscle的值记录的已经是百分比了
//								System.out.println(String.format("................muscle: %.1f%s", muscle,"%"));
//								System.out.println(String.format("................weight: %.1f%s", bone,"kg"));
//								System.out.println(String.format("................metabolism: %d%s", metabolism,"kcal"));
									saveToUserHealth();			
								}
							}
						});
						
					}
				}
			});
 
			// 取消按钮，关闭对话框
			imageViewManualDialogCancel.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					changeMuch = false;
					confirmSave = false;
					alertDialogInput.dismiss();
				}
			});
			
			break;
		
		//分享按钮
		case R.id.button2ndFatShare:
			// 取得外部存储所在目录
			File dir;
			Intent intent = new Intent(Intent.ACTION_SEND);
			getCurrentTime();
			String fileName = "FatShare"+timeStamp+".png";
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
			
		default:
			break;
		}
	}
}

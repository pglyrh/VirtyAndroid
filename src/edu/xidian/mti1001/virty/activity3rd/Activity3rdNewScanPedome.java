package edu.xidian.mti1001.virty.activity3rd;

import java.math.BigDecimal;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import edu.xidian.mti1001.virty.welcome.R;
import edu.xidian.mti1001.virtyandroid.SysApplication;
import edu.xidian.mti1001.virtyandroid.UserDatabaseHelper;

public class Activity3rdNewScanPedome extends Activity implements
		OnClickListener, SensorEventListener {
	// 上部两个“返回”按钮，“on/off”、“设置步数”
	private Button buttonBack;
	private ImageView buttonSwitcher, buttonSet;
	// 显示目标步数、完成百分比和当前步数
	private TextView textViewNumber, textViewPercent, textViewCurrent;

	// 开关状态，true为on
	private boolean on = false;
	// 目标步数、当前步数
	private int target, current;
	// 完成百分比
	private float percentOfCompletion;

	// 传感器管理器
	private SensorManager sm;
	// 与方向有关的值
	private float lastPoint;
	// 是否第一次调用传感器
	private boolean flag = true;
	// 语言
//	private boolean lflag = true;
	Resources resources;

	// 数据库对象
	// 初级版本不分客户，高级版本分客户
	private UserDatabaseHelper db;
	private SQLiteDatabase dbReadDatabase;
	private SQLiteDatabase dbWriteDatabase;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_3rd_news_scan_pedome);

		// 获得控件
		buttonBack = (Button) findViewById(R.id.button3rdPedomeBack);
		buttonSwitcher = (ImageView) findViewById(R.id.button3rdPedomeOnOff);
		buttonSet = (ImageView) findViewById(R.id.button3rdPedomeSet);
		textViewNumber = (TextView) findViewById(R.id.textView3rdPedomeNum);
		textViewPercent = (TextView) findViewById(R.id.textView3rdPedomePercent);
		textViewCurrent = (TextView) findViewById(R.id.textView3rdPedomeCurrent);

		// 添加监听器
		buttonBack.setOnClickListener(this);
		buttonSet.setOnClickListener(this);
		buttonSwitcher.setOnClickListener(this);

		// 新建一个数据库Helper
		db = new UserDatabaseHelper(this, "VirtyDB", null, 1);
		
		dbReadDatabase = db.getReadableDatabase();
//    	query(String table, String[] columns, String selection,String[] selectionArgs,
//    			String groupBy, String having,String orderBy)
        Cursor cursor = dbReadDatabase.query("pedo", null, null, null, null, null, null);
        if (cursor.getCount()>0) {
			
        	cursor.moveToPosition(0);
        	
        	//将数据库中的值赋给本地变量
        	current = cursor.getInt(cursor.getColumnIndex("current"));
        	target = cursor.getInt(cursor.getColumnIndex("target"));
        	percentOfCompletion = cursor.getFloat(cursor.getColumnIndex("percent"));
        	
        	refreshText();
		}
        
        //关闭数据库
        dbReadDatabase.close();
        
        // 语言
        resources = getResources();

/*		String language = Locale.getDefault().getLanguage().toLowerCase();
		if (language.contains("zh")) {
			// 简体中文
			lflag = true;
		}else if (language.contains("en")) {
			// 英文
			lflag = false;
		}
*/        
        SysApplication.getInstance().addActivity(this); 
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		//程序暂停或关闭的时候，将数据保存入数据库中
		//可写入的数据库
        dbWriteDatabase = db.getWritableDatabase();
        //插入数据
        //构建数据
        ContentValues contentValues = new ContentValues();
        contentValues.put("target", target);
        contentValues.put("current", current);
        contentValues.put("percent", percentOfCompletion);
        //更改数据 dbWriteDatabase.update(table, values, whereClause, whereArgs)
        dbWriteDatabase.update("pedo", contentValues, null, null);
        
        //关闭数据库
        dbWriteDatabase.close();
	}
	
	// 刷新文本
	void refreshText() {
		textViewCurrent.setText(current + "");
		if (target > 0) {
			textViewNumber.setText(target + resources.getString(R.string.ActivityHealthInfo4PedometerStep));
		}else {
			// 设置步数小于0，显示未设置
			textViewNumber.setText(resources.getString(R.string.ActivityHealthInfo4PedometerSetting));

		}	
		textViewPercent.setText(resources.getString(R.string.ActivityHealthInfo4PedometerDone) + percentOfCompletion + "%");
		
		// textViewPercent.setText(percentOfCompletion+"");
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		// 返回按钮
		case R.id.button3rdPedomeBack:
			// 跳转到主3“健康时讯”页面
			onBackPressed();
			break;

		// 开关按钮
		case R.id.button3rdPedomeOnOff:
			// 打开/关闭计步器
			// 打开计步器
			if (!on) {
				sm = (SensorManager) getSystemService(SENSOR_SERVICE);
				// 注册方向传感器，返回是否注册成功
				on = sm.registerListener(this,
						sm.getDefaultSensor(Sensor.TYPE_ORIENTATION),
						SensorManager.SENSOR_DELAY_NORMAL);

				if (!on) {
					Toast.makeText(this, resources.getString(R.string.ToastPedometerNotSupport), Toast.LENGTH_SHORT)
					.show();
				}else {
					// 更改图标状态
					buttonSwitcher.setSelected(true);
					buttonSet.setSelected(true);
				}
			} else {
				// 更改图标状态
				buttonSwitcher.setSelected(false);
				buttonSet.setSelected(false);
				// 关闭计步器
				on = !on;
				// 注销方向传感器
				sm.unregisterListener(this);
			}
			break;

		// 设置步数按钮
		case R.id.button3rdPedomeSet:
			// 设置目标计步数
			// 判断开关是否为开
			if (on) {
				String title = "";
				String positive = "";
				String negative = "";
				//查看语言环境
				title = resources.getString(R.string.ActivityHealthInfo4PedometerSetTarget);
				positive = resources.getString(R.string.DialogSure);
				negative = resources.getString(R.string.DialogCancel);
//				if (lflag) {
//					title = "设置目标步数";
//					positive = "确定";
//					negative = "取消";
//				}else {
//					title = "Set Target Steps";
//					positive = "Sure";
//					negative = "Cancel";
//				}
				
				final EditText editText = new EditText(this);
				// 弹出对话框，设置步数
				new AlertDialog.Builder(Activity3rdNewScanPedome.this)
						.setTitle(title)
						.setView(editText)
						.setPositiveButton(positive,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
										// 数据获取
										// 保存新目标值
										if (editText.getText() != null && 
												!editText.getText().toString().isEmpty()) {										
											target = Integer.parseInt(editText
													.getText().toString());
											textViewNumber.setText(target + "");
											// 其余值置空
											current = 0;
											percentOfCompletion = 0;
											// 更新视图
											refreshText();
										}
									}
								}).setNegativeButton(negative, null).show();
			}
			break;

		default:
			break;
		}
	}

	// 传感器发生变化时，调用
	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		if (flag) {
			lastPoint = event.values[1];
			flag = false;
		}
		// 当两个values[1]值之差的绝对值大于8时认为走了一步
		if (Math.abs(event.values[1] - lastPoint) > 8) {
			// 保存最后一步时的values[1]的峰值
			lastPoint = event.values[1];
			// 更改值
			current++;
			// target需大于0才会计算percent
			if (target > 0) {
				float f = ((float) current / target) * 100;
				BigDecimal b = new BigDecimal(f);
				percentOfCompletion = b.setScale(2, BigDecimal.ROUND_HALF_UP)
						.floatValue();
				// System.out.println("completion: "+percentOfCompletion);
			}
			// 刷新列表
			refreshText();
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		// 跳转到主3“健康时讯”页面
//		MainTabs.mTabHost.setCurrentTab(2);
	}
}
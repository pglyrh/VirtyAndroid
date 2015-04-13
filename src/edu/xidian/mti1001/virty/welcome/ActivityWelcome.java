package edu.xidian.mti1001.virty.welcome;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import edu.xidian.mti1001.virty.activity4th.Activity4thSettingDefaultUnit;
import edu.xidian.mti1001.virty.activity4th.Activity4thSettingMode;
import edu.xidian.mti1001.virtyandroid.ActivityDeviceSetting;
import edu.xidian.mti1001.virtyandroid.SysApplication;
import edu.xidian.mti1001.virtyandroid.UserDatabaseHelper;

@SuppressLint("InflateParams")
public class ActivityWelcome extends Activity {

	// 利用ViewPager实现欢迎图片切换
	private ViewPager viewPager = null;
	private WelcomePagerViewerAdapter adapter;
	// "启动按钮"
	private Button userGuideButton;
	// 记录按钮状态
	private SharedPreferences sp;
	// key常量
	private static final String KEY_FIRST_BUTTON_PRESSED = "firstButtonPressed";
	// 模式
	private static final String KEY_MODE_BUTTON_PRESSED = "modeButtonPressed";
	// 单位
	private static final String KEY_UNIT_BUTTON_PRESSED = "unitButtonPressed";

	// 取得外部存储所在目录
	private File dir;
	private String filepath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);

		// 获得SharedPrerences中，是不是第一次使用，并且根据返回值进行相应的操作
		sp = getSharedPreferences("virtySP", Context.MODE_PRIVATE);
		// 第一次启动
		boolean notFirst = sp.getBoolean(KEY_FIRST_BUTTON_PRESSED, false);
		// 健康秤模式
		boolean mode = sp.getBoolean(KEY_MODE_BUTTON_PRESSED, true);
		// 默认单位
		boolean unit = sp.getBoolean(KEY_UNIT_BUTTON_PRESSED, true);

		if (notFirst) {
			// 打开一个新的Activity
			// startActivity(new Intent(this, ActivityDeviceSetting.class));
			// startActivity(new Intent(this, ActivityUserList.class));
			Activity4thSettingMode.mode = mode;
			Activity4thSettingDefaultUnit.unit = unit;
//			startActivity(new Intent(this, MainTabs.class));
			startActivity(new Intent(this, WelcomeSplashScreen.class));
		} else {
			// 初始化切换页面，展现第一次欢迎页面
			initPagerViewer();
			// 写入一个用户数据
			saveAUser();
		}
		
//		saveToSdcard();
		SysApplication.getInstance().addActivity(this); 
	}

	// 存入一个用户
	public void saveAUser(){
		// 数据库对象
		UserDatabaseHelper db;
		SQLiteDatabase dbWriteDatabase, dbReadDatabase;
		
		// 新建一个数据库Helper
		db = new UserDatabaseHelper(this, "VirtyDB", null, 1);
		
		// 可写入的数据库
		dbWriteDatabase = db.getWritableDatabase();
		// 插入数据
		// 构建数据
		ContentValues contentValues = new ContentValues();
		contentValues.put("name", "user");
		contentValues.put("sex", "男");
		// 有一些的存入数据不为String，需要进行处理
		// put的第二个参数可以不为string，为其相应的类型
		contentValues.put("age", 1989);
		contentValues.put("height", 175);
		contentValues.put("targetWeight", 65.0);
		// 更改数据 dbWriteDatabase.insert(table, nullColumnHack, values)
		// 插入表中
		dbWriteDatabase.insert("user", null, contentValues);

		// 关闭数据库
		dbWriteDatabase.close();
	}
	
	// 程序初始化，将需要的图片保存到sdcard中
	public void saveToSdcard() {
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			// 将图片写入sd card中
			// 取得外部存储所在目录
			dir = Environment.getExternalStorageDirectory();

			// 创建文件夹
			String dirPath = dir.getAbsolutePath() + "/virty";
			File path1 = new File(dirPath);
			if (!path1.exists()) {
				path1.mkdirs();
			}

			// 保存文件
			filepath = dir.getAbsolutePath() + "/virty/welcome1.png";
			Drawable d = getResources().getDrawable(R.drawable.welcome1); // xxx根据自己的情况获取drawable
			BitmapDrawable bd = (BitmapDrawable) d;
			Bitmap bm = bd.getBitmap();
			try {
				saveBitmapToFile(bm, filepath);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	// 将指定图片保存入指定文件夹中
	public void saveBitmapToFile(Bitmap bitmap, String _file)
			throws IOException {
		BufferedOutputStream os = null;
		try {
			File file = new File(_file);
			// String _filePath_file.replace(File.separatorChar +
			// file.getName(), "");
			int end = _file.lastIndexOf(File.separator);
			String _filePath = _file.substring(0, end);
			File filePath = new File(_filePath);
			if (!filePath.exists()) {
				filePath.mkdirs();
			}
			file.createNewFile();
			os = new BufferedOutputStream(new FileOutputStream(file));
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
				}
			}
		}
	}

	@SuppressLint("InflateParams")
	private void initPagerViewer() {
		// 获得ViewPager
		viewPager = (ViewPager) findViewById(R.id.viewPageWelcome);

		// 利用ViewPager设置切换页面
		final ArrayList<View> listViews = new ArrayList<View>();
		LayoutInflater layoutInflater = getLayoutInflater();
		// 加入layout页面
		listViews.add(layoutInflater.inflate(R.layout.welcome_1st, null));
		listViews.add(layoutInflater.inflate(R.layout.welcome_2nd, null));
		listViews.add(layoutInflater.inflate(R.layout.welcome_3rd, null));
		listViews.add(layoutInflater.inflate(R.layout.welcome_4th, null));

		adapter = new WelcomePagerViewerAdapter(listViews);
		viewPager.setAdapter(adapter);
		viewPager.setCurrentItem(0);

		// 添加页面切换监听器，当页面切换到第4页（arg0为3）时，对button进行操作
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				// System.out.println("this is page "+arg0);
				if (arg0 == 3) {
					// 最后一个页面按钮操作
					lastPageInViewer();
				}
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

	private void lastPageInViewer() {
		// 获得按钮
		userGuideButton = (Button) findViewById(R.id.buttonUserGuide);

		// 添加按钮监听器
		userGuideButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// 利用SharedPrefrece记录按钮是否为第一次按下
				Editor e = sp.edit();
				e.putBoolean(KEY_FIRST_BUTTON_PRESSED,
						userGuideButton.isPressed());

				// 必须提交，某则只是表示将要存储
				e.commit();

				// 跳转至一个新的Activity
				startActivity(new Intent(ActivityWelcome.this,
						ActivityDeviceSetting.class));
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
